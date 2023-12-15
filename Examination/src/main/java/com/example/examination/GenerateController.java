package com.example.examination;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Movie;
import models.db.Database;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GenerateController {

    @FXML
    protected ScrollPane moviesScroll;

    @FXML
    protected VBox moviesBox;

    @FXML
    protected TextField firstGenre;

    @FXML
    protected TextField secondGenre;

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

    @FXML
    public void initialize() {

    }

    @FXML
    protected void onGenerateMovie() throws IOException {
        List<String> genreList = new ArrayList<>();
        genreList.add(firstGenre.getText());
        genreList.add(secondGenre.getText());

        List<Movie> generatedMovies = Database.getMovieListByGenres(genreList);

        for (Movie m:generatedMovies) {
            System.out.println(m.getTitle());
        }

        moviesBox.getChildren().clear();


        for (Movie movie : generatedMovies) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("movie_card.fxml"));
            Pane cartMovie = loader.load();
            MovieCardController controller = loader.getController();

            String url = String.valueOf(getClass().getResource("/image/not-image.png"));

            URL imageURL = getClass().getResource("/image/" + movie.getId() + ".jpg");
            if (imageURL != null)
                url = String.valueOf(imageURL);


            controller.setMovieData(url, movie.getTitle(), movie.getDescription(), Integer.toString(movie.getYear()), movie.getCountry());

            moviesBox.getChildren().add(cartMovie);
        }


    }
}
