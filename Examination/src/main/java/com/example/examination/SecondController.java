package com.example.examination;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Movie;
import models.db.Database;

import java.io.IOException;
import java.net.URL;


public class SecondController {

    @FXML
    public ImageView movieSeasonImage;
    @FXML
    public Button movieSeasonInterest;
    @FXML
    public Text movieSeasonTitle;
    @FXML
    public Text movieSeasonDescription;

    @FXML
    protected void onBackButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(mainScene);

            currentStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCatalogButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_page.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(mainScene);

            currentStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onMovieGenerateButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movie_generate.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(mainScene);

            currentStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCatchMoodButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("catch_mood.fxml"));
            Parent root = loader.load();
            Scene mainScene = new Scene(root);

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(mainScene);

            currentStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        Movie movie = Database.getMovieSeason();
        Image image = null;
        URL imageURL = getClass().getResource("/image/" + movie.getId() + ".jpg");
        if (imageURL != null) {
            image = new Image(String.valueOf(imageURL));
        } else {
            image = new Image(String.valueOf(getClass().getResource("/image/not-image.png")));
        }
        movieSeasonImage.setImage(image);
        movieSeasonTitle.setText(movie.getTitle());
        movieSeasonDescription.setText(movie.getDescription().substring(0, Math.min(movie.getDescription().length(), 200)));

        movieSeasonInterest.setOnAction(event -> showDetails(movie,(Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));

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