module com.example.datastructures2ca1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.datastructures2ca1 to javafx.fxml;
    exports com.example.datastructures2ca1;
}