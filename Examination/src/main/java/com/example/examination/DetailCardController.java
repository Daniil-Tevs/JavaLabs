package com.example.examination;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Movie;

import java.io.IOException;
import java.net.URL;

public class DetailCardController {
    public ImageView movieImage;
    public Text movieYear;
    public Text movieDuration;
    public Text movieCountry;
    public Text movieDescription;
    public Label movieTitle;

    @FXML
    protected void onBackButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("second_page.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(mainScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setMovieDetails(Movie movie) {
        Image image = null;
        URL imageURL = getClass().getResource("/image/" + movie.getId() + ".jpg");
        if (imageURL != null) {
            image = new Image(String.valueOf(imageURL));
        } else {
            image = new Image(String.valueOf(getClass().getResource("/image/not-image.png")));
        }
        movieImage.setImage(image);
        movieYear.setText(Integer.toString(movie.getYear()));

        movieTitle.setText(movie.getTitle());
        movieDuration.setText(Integer.toString(movie.getDuration()));
        movieCountry.setText(movie.getCountry());
        movieDescription.setText(movie.getDescription().substring(0, Math.min(movie.getDescription().length(), 100)));

    }
}
