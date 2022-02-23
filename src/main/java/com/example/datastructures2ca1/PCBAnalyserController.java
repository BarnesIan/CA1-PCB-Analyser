package com.example.datastructures2ca1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


public class PCBAnalyserController {
    @FXML
    private ImageView mainImage;
    @FXML
    private ImageView image2;

    @FXML
    private Label Brightness1, Saturation1, Hue1, Red1, Green1, Blue1;

    private PixelReader preader;


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
                    } else {
                        pw.setColor(w, h, Color.color(1, 1, 1));
                    }
                }
            }
            image2.setImage(writableImage);
        });
    }

    private static boolean blackIdentify(Double red, Double green, Double blue, Double hue, Double redVal, Double greenVal, Double blueVal, Double hueVal) {
        return red > redVal - 0.15 && red < redVal + 0.15 && green > greenVal - 0.15 && green < greenVal + 0.15 && blue > blueVal - 0.15 &&
                blue < blueVal + 0.15 && hue > hueVal - 3 && hue < hueVal + 3;
    }



}