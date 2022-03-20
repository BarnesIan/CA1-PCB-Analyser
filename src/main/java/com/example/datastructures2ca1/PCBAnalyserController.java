package com.example.datastructures2ca1;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;


public class PCBAnalyserController {

    @FXML
    private ImageView mainImage;
    @FXML
    private ImageView image2;
    @FXML
    private Label counter;

    @FXML
    private Label Brightness1, Saturation1, Hue1, Red1, Green1, Blue1;

    @FXML
    private Slider brightnessSlider, saturation, nr;


    private Color selectedColor;

    private PixelReader preader;

    Rectangle rect;
    public ArrayList<Integer> selectedObjects = new ArrayList<>();
    int[] pixels;
    public ArrayList<Integer> djs = new ArrayList<>();


    public void openImage(ActionEvent ActionEvent) {
        //Stage stage = (Stage) ((Node) ActionEvent.getTarget()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File filePath = fileChooser.showOpenDialog(null);
        // Image selectedImage = new Image(String.valueOf(fileChooser.showOpenDialog(null)));

        //  Image image = new Image("",512,512,false,false);

        if (filePath != null) {
            Image image = new Image("file:///" + filePath, 512, 512, false, false);
            mainImage.setImage(image);

            pixels = new int[(int) (image.getWidth() * image.getHeight())];
        }
    }

    public void clickedImage(MouseEvent event) {

        //double finalHue = getSelectedColor().getHue();
        preader = mainImage.getImage().getPixelReader();
        Image inputImage = mainImage.getImage();

        WritableImage writableImage = new WritableImage(preader, (int) inputImage.getWidth(), (int) inputImage.getHeight());
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        PixelWriter pw = writableImage.getPixelWriter();


        mainImage.setOnMouseClicked(e -> {
            int x = (int) e.getX();
            int y = (int) e.getY();


            Red1.setText(String.format("%.2f", preader.getColor(x, y).getRed()));
            Green1.setText(String.format("%.2f", preader.getColor(x, y).getGreen()));
            Blue1.setText(String.format("%.2f", preader.getColor(x, y).getBlue()));
            Hue1.setText(String.format("%.2f", preader.getColor(x, y).getHue()));
            Brightness1.setText(String.format("%.2f", preader.getColor(x, y).getBrightness()));
            Saturation1.setText(String.format("%.2f", preader.getColor(x, y).getSaturation()));

            int i = 0;
            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {

                    Color color = preader.getColor(w, h);
                    var red = color.getRed();
                    var blue = color.getBlue();
                    var green = color.getGreen();
                    var hue = color.getHue();
                    var sat = color.getSaturation();
                    var bright = color.getBrightness();
                    brightnessSlider.getValue();
                    saturation.getValue();
                    var redValue = Double.parseDouble(Red1.getText());
                    var greenValue = Double.parseDouble(Green1.getText());
                    var blueValue = Double.parseDouble(Blue1.getText());
                    var hueValue = Double.parseDouble(Hue1.getText());
                    double satValue = saturation.getValue();
                    double brightValue = brightnessSlider.getValue();


                    if (blackIdentify(red, green, blue, hue, redValue, greenValue, blueValue, hueValue, bright, brightValue, sat, satValue)) {
                        pw.setColor(w, h, Color.color(0, 0, 0));
                        pixels[i] = i;

                    } else {
                        pw.setColor(w, h, Color.color(1, 1, 1));
                        pixels[i] = 0;
                    }
                    i++;
                }
            }
            image2.setImage(writableImage);
            createDisjointSet();
        });
    }

    private static boolean blackIdentify(Double red, Double green, Double blue, Double hue, Double redVal, Double greenVal, Double blueVal, Double hueVal, Double bright, Double brightValue, Double sat, Double satValue) {
        return red > redVal - 0.3 && red < redVal + 0.3 && green > greenVal - 0.3 && green < greenVal + 0.3 && blue > blueVal - 0.3 &&
                blue < blueVal + 0.3 && hue > hueVal - 8 && hue < hueVal + 8 && bright > brightValue - 0.9 && bright < brightValue + 0.9 && sat > satValue - 0.5 && sat < satValue + 0.5;
    }


    /**
     * Iterative method of find
     */
    public static int find(int[] a, int id) {
        if (a[id] == 0) {
            id = 0;
            return id;
        }
        while (a[id] != id) {
            id = a[id];
        }
        return id;
    }

    /**
     * Recursive method of find that uses the ternary operator where it recursively calls itself
     * Feed this method a set and the id
     */
    public static int findRec(int[] a, int id) {
        return a[id] == id ? id : findRec(a, a[id]);
    }

    public static int findCompress(int[] a, int id) {
        while (a[id] != id) {
            a[id] = a[a[id]]; //Compress path
            id = a[id];
        }
        return id;
    }

    public void union(int[] a, int p, int q) {
        a[findCompress(a, q)] = findCompress(a, p);
    }

    public void setUpClusters() {
        for (int p = 0; p < pixels.length; p++) {
            if (pixels[p] != 0 && !djs.contains(findCompress(pixels, p))) {
                djs.add(findCompress(pixels, p));
            }
        }
    }

    public void drawRectangle() {

        int selectedObjects = 0;
        Image image = mainImage.getImage();

        setUpClusters();

        for (int data : djs) {

            int maxHeight = 0;
            int minHeight = 0;
            int left = 0;
            int right = 0;
            for (int i = 0; i < pixels.length; i++) {
                int x = i % (int) image.getWidth();
                int y = i / (int) image.getWidth();

                if (pixels[i] != -1 && find(pixels, i) == data) {
                    if (maxHeight == 0) {
                        maxHeight = minHeight = y;
                        left = right = x;
                    } else {
                        if (x < left)
                            left = x;
                        if (x > right)

                            right = x;
                        if (y > minHeight)
                            minHeight = y;
                    }
                }
            }
            //Helps clean the selected objects by getting the area of each disjoint set and only adds to the counter if above a certain area
            if ((right - left) * (minHeight - maxHeight) > 100) {
                selectedObjects++;
                Rectangle rect = new Rectangle(left - 5, maxHeight + 5, right - left + 5, minHeight - maxHeight);
                // Rectangle rect = new Rectangle(left,right - left,maxHeight,minHeight -maxHeight);
                rect.setTranslateX(mainImage.getTranslateX());
                rect.setTranslateY(mainImage.getTranslateY());

                ((AnchorPane) mainImage.getParent()).getChildren().add(rect);

                rect.setStrokeWidth(2);
                rect.setStroke(Color.FIREBRICK);
                rect.setFill(Color.TRANSPARENT);
            }
        }
        counter.setText("Selected Objects: " + selectedObjects);

    }

    public void createDisjointSet() {
        Image image = mainImage.getImage();

        int width = (int) image.getWidth();
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] != 0) { // if an index is black
                if ((i + 1) % width != 0 && pixels[i + 1] != 0)
                    union(pixels, i, i + 1); // union that index with the index to the right
                if (i + width < pixels.length && pixels[i + width] != 0)
                    union(pixels, i, i + width); // union index with the index below
            }

        }

    }

    public void ClearRectangles(ActionEvent event) {
        int width = (int) mainImage.getImage().getWidth();
        int height = (int) mainImage.getImage().getHeight();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                if (preader.getColor(w, h).equals(Color.FIREBRICK)) ;
                WritableImage writableImage = new WritableImage(preader, width, height);


            }

        }
    }

    public void Color(ActionEvent event) {

        preader = image2.getImage().getPixelReader();
        Image image = mainImage.getImage();
        WritableImage writableImage = new WritableImage(preader, (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pw = writableImage.getPixelWriter();

        for (int p = 0; p < pixels.length; p++) {
            if (pixels[p] != 0 && !djs.contains(findCompress(pixels, p))) {
                djs.add(findCompress(pixels, p));
            }
        }
        for (int data : djs) {
            int maxHeight = 0;
            int minHeight = 0;
            int left = 0;
            int right = 0;
            for (int i = 0; i < pixels.length; i++) {
                int x = i % (int) image.getWidth();
                int y = i / (int) image.getWidth();

                if (pixels[i] != -1 && find(pixels, i) == data) {
                    if (maxHeight == 0) {
                        maxHeight = minHeight = y;
                        left = right = x;
                    } else {
                        if (x < left)
                            left = x;
                        if (x > right)

                            right = x;
                        if (y > minHeight)
                            minHeight = y;
                    }
                }
                    Random rand = new Random();
                    float r = rand.nextFloat();
                    float g = rand.nextFloat();
                    float b = rand.nextFloat();
                    image2.setImage(writableImage);
                    pw.setColor(x, y, Color.color(r, g, b));

                }
            }
        }
    }


}