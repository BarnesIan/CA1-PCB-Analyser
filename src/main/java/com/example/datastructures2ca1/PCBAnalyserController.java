package com.example.datastructures2ca1;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;


public class PCBAnalyserController {

    @FXML
    private ImageView mainImage;
    @FXML
    private ImageView image2;
    @FXML
    private Label counter;

    @FXML
    private Label Brightness1, Saturation1, Hue1, Red1, Green1, Blue1;

    private PixelReader preader;

    Rectangle rect;
    public ArrayList<Integer> selectedObjects = new ArrayList<>();
     int[] pixels;
     //public DisjointSetNode djs1 = new DisjointSetNode();
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

            pixels = new int[(int)(image.getWidth() * image.getHeight())];
        }
    }

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

                    var redValue = Double.parseDouble(Red1.getText());
                    var greenValue = Double.parseDouble(Green1.getText());
                    var blueValue = Double.parseDouble(Blue1.getText());
                    var hueValue = Double.parseDouble(Hue1.getText());
                    var satValue = Double.parseDouble(Saturation1.getText());
                    var brightValue = Double.parseDouble(Brightness1.getText());

                    if (blackIdentify(red, green, blue, hue, redValue, greenValue, blueValue, hueValue)) {
                        pw.setColor(w, h, Color.color(0, 0, 0));
                        pixels[i] = i;


                    } else {
                        pw.setColor(w, h, Color.color(1, 1, 1));
                        pixels[i] =0;
                    }
                    i++;
                }
            }
            image2.setImage(writableImage);
            createDisjointSet();
        });
    }

    private static boolean blackIdentify(Double red, Double green, Double blue, Double hue, Double redVal, Double greenVal, Double blueVal, Double hueVal) {
        return red > redVal - 0.15 && red < redVal + 0.15 && green > greenVal - 0.15 && green < greenVal + 0.15 && blue > blueVal - 0.15 &&
                blue < blueVal + 0.15 && hue > hueVal - 3 && hue < hueVal + 3;
    }

    public boolean blackPixels(Color color){
        double average = (color.getRed() + color.getGreen() + color.getBlue() /3);
        if(average <= 0.5){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Iterative method of find
     */
    public static int find(int[] a, int id){
        if(a[id] ==0){
            id = 0;
            return id;
        }
        while(a[id]!=id ) {
            id = a[id];
        }
        return id;
    }
    /**
     * Recursive method of find that uses the ternary operator where it recursively calls itself
     * Feed this method a set and the id
     */
    public static int findRec(int[] a, int id){
        return a[id] == id ? id : findRec(a,a[id]);
    }

    public void union(int[] a, int p, int q){
        a[find(a,q)] = find(a,p);
    }


    public void drawRectangle() {
            int selectedObjects = 0;
            Image image = mainImage.getImage();
            Image bwImage = image2.getImage();
            for (int p = 0; p < pixels.length; p++) {
                if (pixels[p] != 0 && !djs.contains(find(pixels, p))) {
                    djs.add(find(pixels, p));
                }
            }
            for (int id : djs) {
                selectedObjects++;
                double maxHeight = -1;
                double minHeight = -1;
                double left = -1;
                double right = -1;
                for (int i = 0; i < pixels.length; i++) {
                    int x = i % (int) image.getWidth();
                    int y = i / (int) image.getWidth();

                    if (pixels[i] != 0 && find(pixels, i) == id) {
                        if (maxHeight == -1) {
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

                Rectangle rect = new Rectangle(left, maxHeight, right - left, minHeight - maxHeight);
                // Rectangle rect = new Rectangle(left,right - left,maxHeight,minHeight -maxHeight);
                rect.setTranslateX(mainImage.getTranslateX());
                rect.setTranslateY(mainImage.getTranslateY());

                ((AnchorPane) mainImage.getParent()).getChildren().add(rect);

                rect.setStrokeWidth(4);
                rect.setStroke(Color.FIREBRICK);
                rect.setFill(Color.TRANSPARENT);

            }
        counter.setText("Selected Objects: " + selectedObjects);

        }


    public void createDisjointSet(){
        Image image = mainImage.getImage();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for(int y = 0; y < height; y ++){
           for(int x = 0;x < width; x ++){
               if(pixels[y * width + x] != 0 && pixels[y * width + x + 1] != 0){
                   union(pixels,y * width + x, y * width + x + 1);
               }
               if(y < height -1 && pixels[y * height + x] !=0 && pixels[y * width + x + width] != 0){
                   union(pixels, y * height +x, y * width + x + width);
               }
           }
        }
    }






}