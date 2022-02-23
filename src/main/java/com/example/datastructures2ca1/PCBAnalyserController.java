package com.example.datastructures2ca1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;



public class PCBAnalyserController {
private PixelReader pixelReader;
private PixelWriter pixelWriter;
    @FXML
private ImageView image1;
    @FXML
    private ImageView image2;

    @FXML
    private Label Brightness,Saturation,Hue, Red, Green,Blue;


    public void openImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg","*.jpeg", "*.gif"));
        Image selectedImage = new Image(String.valueOf(fileChooser.showOpenDialog(null)));
        if (selectedImage != null) {
            image1.setImage(selectedImage);

        }
    }



private boolean blackIdentify(Double red,Double green, Double blue, Double hue,Double redVal, Double greenVal, Double blueVal, Double hueVal){
    return red >  - 0.15 && red < redVal + 0.15 && green > greenVal - 0.15 && green < greenVal + 0.15 && blue > blueVal - 0.15 && blue < blueVal + 0.15 && hue > hueVal - 2 && hue < hueVal + 2;
}
public void clickedImage (MouseEvent mouseEvent) {

            Image inputImage = image1.getImage();

            PixelReader pr = inputImage.getPixelReader();
            WritableImage writableImage = new WritableImage(pr, (int) inputImage.getWidth(), (int) inputImage.getHeight());
            int width = (int) inputImage.getWidth();
            int height = (int) inputImage.getHeight();
            PixelWriter pw = writableImage.getPixelWriter();


             image1.setOnMouseClicked(e -> {
                 int x = (int) image1.getX();
                 int y = (int) image1.getY();

            System.out.println("Hue : " + pr.getColor(x,y).getHue());
            System.out.println("Saturation : " + pr.getColor(x,y).getSaturation());
            System.out.println("Brightness : " + pr.getColor(x,y).getBrightness());

            Red.setText(String.format("%.2f", pr.getColor(x,y).getRed()));
            Green.setText(String.format("%.2f", pr.getColor(x,y).getGreen()));
            Blue.setText(String.format("%.2f", pr.getColor(x,y).getBlue()));
            Hue.setText(String.format("%.2f", pr.getColor(x,y).getHue()));
            Brightness.setText(String.format("%.2f", pr.getColor(x,y).getBrightness()));
            Saturation.setText(String.format("%.2f", pr.getColor(x,y).getSaturation()));


            for (int h = 0; h < height; h++) {
                for (int w = 0; w < width; w++) {

                    Color color = pr.getColor(w,h);
                    var red = color.getRed();
                    var blue  = color.getBlue();
                    var green = color.getGreen();
                    var hue = color.getHue();
                    var sat = color.getSaturation();
                    var bright = color.getBrightness();
                    var redValue = Double.parseDouble(Red.getText());
                    var greenValue = Double.parseDouble(Green.getText());
                    var blueValue = Double.parseDouble(Blue.getText());
                    var hueValue = Double.parseDouble(Hue.getText());
                    var satValue = Double.parseDouble(Saturation.getText());
                    var brightValue = Double.parseDouble(Brightness.getText());

                    if (blackIdentify(red, green, blue, hue, redValue,greenValue,blueValue,hueValue)) {
                        pw.setColor(w, h, Color.color(0,0,0));
                    }
                    else{
                        pw.setColor(w,h,Color.color(1,1,1));
                }
                }
            }
            image2.setImage(writableImage);
        });
   }


   /*public void clickedImage (MouseEvent mouseEvent) {
        image1.setOnMouseClicked(e -> {

            Image inputImage = image1.getImage();

            int x = (int) e.getX();
            int y = (int) e.getY();
            PixelReader pr = inputImage.getPixelReader();
            WritableImage writableImage = new WritableImage(pr, (int) inputImage.getWidth(), (int) inputImage.getHeight());
            PixelWriter pw = writableImage.getPixelWriter();


            System.out.println("Hue : " + pr.getColor(x,y).getHue());
            System.out.println("Saturation : " + pr.getColor(x,y).getSaturation());
            System.out.println("Brightness : " + pr.getColor(x,y).getBrightness());

            Brightness.setText(String.format("%.2f", pr.getColor(x,y).getBrightness()));
            Saturation.setText(String.format("%.2f", pr.getColor(x,y).getSaturation()));
            Hue.setText(String.format("%.2f", pr.getColor(x,y).getHue()));
            Red.setText(String.format("%.2f", pr.getColor(x,y).getRed()));
            Green.setText(String.format("%.2f", pr.getColor(x,y).getGreen()));
            Blue.setText(String.format("%.2f", pr.getColor(x,y).getBlue()));


            for (int h = 0; h < inputImage.getHeight(); h++) {
                for (int w = 0; w < inputImage.getWidth(); w++) {
                    Color color = pr.getColor(w,h);
                    var red = color.getRed();
                    var blue  = color.getBlue();
                    var green = color.getGreen();
                    var hue = color.getHue();
                    var sat = color.getSaturation();
                    var bright = color.getBrightness();
                    var redValue = Double.parseDouble(Red.getText());
                    var greenValue = Double.parseDouble(Green.getText());
                    var blueValue = Double.parseDouble(Blue.getText());
                    var hueValue = Double.parseDouble(Hue.getText());
                    var satValue = Double.parseDouble(Saturation.getText());
                    var brightValue = Double.parseDouble(Brightness.getText());

                    if ((red > redValue - 0.8) && (red < redValue + 0.8) &&
                            ( blue > blueValue  - 0.8) && (blue < blueValue  + 0.8) &&
                            ( green > greenValue - 0.8) && (green < greenValue + 0.8) &&
                            (hue > hueValue - 360) && (hue < hueValue + 360) &&
                            (sat > satValue - 0.1) && (sat < satValue +0.1) &&
                            (bright > brightValue -0.1) && (bright < brightValue +0.1)) {
                        pw.setColor(w, h, Color.BLACK);
                    }
                    else{
                        pw.setColor(w,h,Color.WHITE);
                }
                }
            }
            image2.setImage(writableImage);
        });
   }*/

}