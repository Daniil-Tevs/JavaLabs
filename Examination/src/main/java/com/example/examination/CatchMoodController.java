package com.example.examination;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Movie;
import models.db.Database;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CatchMoodController{

    @FXML
    protected ScrollPane moviesScroll;

    @FXML
    protected VBox moviesBox;

    @FXML
    private Pane categoryPane;
    @FXML
    protected void onBackButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("second_page.fxml"));
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

    private void handleRadioButtonAction(String selectedMood) throws IOException {
        List<Movie> generatedMovies = Database.getMovieListByMood(selectedMood);

        for (Movie m : generatedMovies) {
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

    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();

        // Create and add Text element
        Text categoryText = new Text("Category");
        categoryText.setFont(new Font("Algerian", 24.0));
        categoryText.setLayoutX(21.0);
        categoryText.setLayoutY(35.0);
        categoryPane.getChildren().add(categoryText);

        List<String> categories = Database.getMoodList();
        // Create and add RadioButtons dynamically based on the categories list
        double layoutY = 79.0;
        for (String category : categories) {
            RadioButton radioButton = new RadioButton(category);
            radioButton.setLayoutX(14.0);
            radioButton.setLayoutY(layoutY);
            radioButton.setMnemonicParsing(false);
            radioButton.setTextFill(Paint.valueOf("#ffedcd"));
            radioButton.setFont(new Font("Bookman Old Style Italic", 18.0));
            radioButton.setCursor(javafx.scene.Cursor.HAND);
            radioButton.setOnAction(event -> {
                try {
                    handleRadioButtonAction(category);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            radioButton.setToggleGroup(toggleGroup);
            categoryPane.getChildren().add(radioButton);

            layoutY += 52.0; // Adjust the vertical spacing
        }
    }
}
