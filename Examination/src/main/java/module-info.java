module com.example.examination {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.examination to javafx.fxml;
    exports com.example.examination;
}