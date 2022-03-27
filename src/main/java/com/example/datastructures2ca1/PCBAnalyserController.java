package com.example.datastructures2ca1;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
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
    private ImageView image2, blackandwhite;
    @FXML
    private Label counter, component;


    @FXML
    private Label Brightness1, Saturation1, Hue1, Red1, Green1, Blue1;

    @FXML
    private Slider brightnessSlider, saturation;

    private PixelReader preader;
    int[] pixels;
    public ArrayList<Integer> djs = new ArrayList<>();


    public void initialize(){
        Tooltip button = new Tooltip("Draw rectangles on Disjoint Sets");
        Tooltip.install(counter,button);
        counter.setTooltip(button);
    }
    public void openImage(ActionEvent ActionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File filePath = fileChooser.showOpenDialog(null);

        //Sets the image size to 512x512 regardless of how big the image actually is, Makes it easier to run the project for
        if (filePath != null) {
            Image image = new Image("file:///" + filePath, 512, 512, false, false);
            mainImage.setImage(image);

            pixels = new int[(int) (image.getWidth() * image.getHeight())];
        }
    }

    /**
     * B&W Conversion Method
     */

    public void clickedImage(MouseEvent event) {

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
                    component.setText("Selected Component: " + determineComponent(redValue,greenValue,blueValue));

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

    /**
     *Identifies the areas to change for black and what to change for white depending on what is clicked on. This is where tolerances can be set and uses SAT and BRIGHT Sliders.
     */
    private static boolean blackIdentify(Double red, Double green, Double blue, Double hue, Double redVal, Double greenVal, Double blueVal, Double hueVal, Double bright, Double brightValue, Double sat, Double satValue) {
        return red > redVal - 0.3 && red < redVal + 0.3 && green > greenVal - 0.3 && green < greenVal + 0.3 && blue > blueVal - 0.3 &&
                blue < blueVal + 0.3 && hue > hueVal - 8 && hue < hueVal + 8 && bright > brightValue - 0.9 && bright < brightValue + 0.9 && sat > satValue - 0.5 && sat < satValue + 0.5;
    }

    public String determineComponent(Double red, Double green, Double blue){
        String component = "";
        if((red <= 0.33 && red >= 0.12) && (green <= 0.30 && green >= 0.12) && (blue <= 0.30 && blue >= 0.12 )){
            component = "Integrated Circuit";
        }else if((red <= 0.98 && red >= 0.84) && (green <= 0.65 && green >= 0.39) &&(blue <=0.30 && blue >=0.1 )){
            component = "Capacitor";

        }
        else if((red <= 0.94 && red >= 0.75) && (green <= 0.78 && green >= 0.64) && (blue <=0.6 && blue >= 0.50)){
            component = "C13 Resistor";
        }
        else{
            component = "Unknown";
        }
        return component;
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

    /**
     * Find iterative method with compression, taken from notes
     */
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

    /**
     * Method used for finding and adding the black pixels to the Disjoint Set
     */
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
        Image  secondImage = image2.getImage();
        PixelReader pr = secondImage.getPixelReader();
        WritableImage writableImage = new WritableImage(pr, (int) secondImage.getWidth(), (int) secondImage.getHeight());
        setUpClusters();

        for (int data : djs) {

            int maxHeight = 0;
            int minHeight = 0;
            int left = 0;
            int right = 0;
            for (int i = 0; i < pixels.length; i++) {
                int x = i % (int) image.getWidth();
                int y = i / (int) image.getWidth();

                if (pixels[i] != 0 && find(pixels, i) == data) {
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

            int biggestArea = 0;
            int Area = (right - left) *(minHeight - maxHeight);
            if (Area > biggestArea){
                biggestArea = Area;

            }
            Rectangle rect;
            //Helps clean the selected objects by getting the area of each disjoint set and only adds to the counter if above a certain area
            if ((right - left) * (minHeight - maxHeight) > 100) {

                selectedObjects++;
                 rect = new Rectangle(left, maxHeight, right - left, minHeight - maxHeight);
                // Rectangle rect = new Rectangle(left,right - left,maxHeight,minHeight -maxHeight);
                rect.setTranslateX(mainImage.getTranslateX());
                rect.setTranslateY(mainImage.getTranslateY());
                rect.setStrokeWidth(2);
                rect.setStroke(Color.FIREBRICK);
                rect.setFill(Color.TRANSPARENT);

                ((AnchorPane) mainImage.getParent()).getChildren().add(rect);

                Tooltip area = new Tooltip("Estimated Area: " + Area + " pixels" + "\n" + getComponent());
                Tooltip.install(rect, area);
                area.setWidth(200);
                area.setHeight(1500);

            }


        }
        blackandwhite.setImage(writableImage);
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

    public void drawDisjoints(){
        int selectedObjects = 0;
        Image image = mainImage.getImage();
        Image  secondImage = image2.getImage();
        PixelReader pr = secondImage.getPixelReader();
        WritableImage writableImage = new WritableImage(pr, (int) secondImage.getWidth(), (int) secondImage.getHeight());
        setUpClusters();

        for (int data : djs) {

            int maxHeight = 0;
            int minHeight = 0;
            int left = 0;
            int right = 0;
            for (int i = 0; i < pixels.length; i++) {
                int x = i % (int) image.getWidth();
                int y = i / (int) image.getWidth();

                if (pixels[i] != 0 && find(pixels, i) == data) {
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

            int biggestArea = 0;
            int Area = (right - left) * (minHeight - maxHeight);
            if (Area > biggestArea) {
                biggestArea = Area;

            }
        }

    }

    /**
     * Not working
     */

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

    public void SampleSingleDJS(){

    }


    public void ColorSingleDJS(ActionEvent event) {

        preader = image2.getImage().getPixelReader();
        Image image = image2.getImage();
        WritableImage writableImage = new WritableImage(preader, (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pw = writableImage.getPixelWriter();

       setUpClusters();

        for (int data : djs) {

            Random rand = new Random();
            int r = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            var randomColor = Color.rgb(r,g,b);
            for (int i = 0; i < pixels.length; i++) {
                int x = i % (int) image.getWidth();
                int y = i / (int) image.getWidth();

                if (pixels[i] != 0 && find(pixels, i) == data) {
                    pw.setColor(x, y, randomColor);
                } else if (pixels[i] == 0 && find(pixels, i) !=i) {
                    pw.setColor(x, y, Color.WHITE);
                }
            }
            image2.setImage(writableImage);
            }
        }


      /*  public String getComponent(int area) {
            String component = "";
            if (area < 99999 &&  area > 9999 ) {
                component = "Integrated Circuit";
            } else if (area < 9998 && area > 999) {
                component = "Capacitor";
            }
                else if(area < 998 && area > 400){
                    component = "Resistor";
                }

         return component;
        }*/

        public String getComponent(){
        return component.getText();
        }
        public void CloseProgram(){

        }
    }


