package com.example.datastructures2ca1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;



public class PCBAnalyserController {

    @FXML
private ImageView image;
    @FXML
    private ImageView image2;


    public void openImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg","*.jpeg", "*.gif"));
        Image selectedImage = new Image(String.valueOf(fileChooser.showOpenDialog(null)));
        if (selectedImage != null) {
            image.setImage(selectedImage);

        }
    }



    public WritableImage clickedImage (MouseEvent e) {
        //image.setOnMousePressed(mousePressed -> e);
        int width = (int) e.getX();
        int height = (int) e.getY();
        PixelReader pr = image.getImage().getPixelReader();
        WritableImage writableImage = new WritableImage(pr, width, height);
        PixelWriter pw = writableImage.getPixelWriter();
        Color col = pr.getColor(width, height);

        System.out.println("Hue : " + col.getHue());
        System.out.println("Saturation : " + col.getSaturation());
        System.out.println("Brightness : " + col.getBrightness());


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (col.getHue() < 160 || col.getHue() > 100) {
                    pw.setColor(x, y, Color.WHITE);
                    Color color = pr.getColor(x, y);
                    pw.setColor(x, y, color);
                    writableImage.getPixelWriter().setColor(x,y,color);

                }
            }
        }
        return writableImage;
    }






}