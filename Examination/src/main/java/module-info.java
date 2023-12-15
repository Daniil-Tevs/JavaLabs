module com.example.examination {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires javafx.media;


    opens com.example.examination to javafx.fxml;
    exports com.example.examination;
}