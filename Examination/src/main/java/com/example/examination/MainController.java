package com.example.examination;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.text.Text;

public class MainController {
    @FXML
    private Text animatedText;
    public void initialize() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(animatedText.textProperty(), "Find the film that suits your mood")),
                new KeyFrame(Duration.seconds(3), new KeyValue(animatedText.textProperty(), "Find an optimal film for the interests of two people")),
                new KeyFrame(Duration.seconds(6), new KeyValue(animatedText.textProperty(), "Another text to display"))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


    @FXML
    protected void onButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("second_page.fxml"));
            Parent root = loader.load();

            Scene nextScene = new Scene(root);

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(nextScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}