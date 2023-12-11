package com.example.examination;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Pane root = loader.<Pane>load();

            Scene scene = new Scene(root, 457, 650);

            primaryStage.setResizable(false);

            primaryStage.setScene(scene);

            primaryStage.setTitle("FilmFinder");

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
