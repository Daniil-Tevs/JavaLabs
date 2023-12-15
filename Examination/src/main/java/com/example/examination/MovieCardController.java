package com.example.examination;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Movie;


import java.io.IOException;

public class MovieCardController {

    @FXML
    protected ImageView movieImage;
    @FXML
    private Button showMovie;
    @FXML
    protected Label movieTitle;
    @FXML
    protected TextArea movieDescription;
    @FXML
    protected Text movieYear;
    @FXML
    protected Text movieCountry;

    public void setMovieData(String url, Movie movie) {
        movieImage.setImage(new Image(url));
        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getDescription());
        movieYear.setText(Integer.toString(movie.getYear()));
        movieCountry.setText(movie.getCountry());

        showMovie.setOnAction(event -> showDetails(movie,(Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));
    }

    private void showDetails(Movie movie, Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/examination/detail_card.fxml"));
            Parent root = loader.load();

            DetailCardController detailsController = loader.getController();

            detailsController.setMovieDetails(movie);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
