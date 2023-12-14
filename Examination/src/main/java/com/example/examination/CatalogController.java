package com.example.examination;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.db.Database;
import models.Genre;
import models.Movie;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogController {

    @FXML
    private TextField movieSearch;

    @FXML
    private ChoiceBox<String> yearChoiceBox;

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    @FXML
    private ChoiceBox<String> countryChoiceBox;

    @FXML
    private ScrollPane scrollPaneMovies;

    @FXML
    protected void onFilterButton(ActionEvent event) {
        String searchTitle = movieSearch.getText();
        String selectedYear = yearChoiceBox.getValue();
        String selectedGenre = genreChoiceBox.getValue();
        String selectedCountry = countryChoiceBox.getValue();

        System.out.println(searchTitle);

        List<Movie> movieList = Database.search(searchTitle, selectedYear, selectedGenre, selectedCountry);

        VBox cardsContainer = new VBox();
        cardsContainer.setSpacing(20);

        for (Movie movie : movieList) {
            HBox card = createMovieCard(movie);
            cardsContainer.getChildren().add(card);
        }

        scrollPaneMovies.setContent(cardsContainer);
    }

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


    public void initialize() {
        List<String> yearsCommands = new ArrayList<>(){};
        yearsCommands.add("Все");
        yearsCommands.addAll(Database.getYearList());

        ObservableList<String> years = FXCollections.observableArrayList(
                yearsCommands.toArray(new String[0])
        );


        List<String> genreCommands = new ArrayList<>(){};
        genreCommands.add("Все");
        genreCommands.addAll(Database.getGenreList()
                .stream()
                .map(Genre::getTitle).toList());

        ObservableList<String> genres = FXCollections.observableArrayList(
                genreCommands.toArray(new String[0])
        );

        List<String> countryCommands = new ArrayList<>(){};
        countryCommands.add("Все");
        countryCommands.addAll(Database.getCountryList());

        ObservableList<String> countries = FXCollections.observableArrayList(
                countryCommands.toArray(new String[0])
        );

        yearChoiceBox.setItems(years);
        genreChoiceBox.setItems(genres);
        countryChoiceBox.setItems(countries);

        List<Movie> movieList = Database.getMovieList();
        VBox cardsContainer = new VBox();
        cardsContainer.setSpacing(20);
        cardsContainer.setPrefWidth(580);

        for (Movie movie : movieList) {
            HBox card = createMovieCard(movie);
            cardsContainer.getChildren().add(card);
        }

        scrollPaneMovies.setContent(cardsContainer);
    }


    private HBox createMovieCard(Movie movie) {
        HBox card = new HBox();
        card.setSpacing(10);

        ImageView imageView = null;
        URL imageURL = getClass().getResource("/image/" + movie.getId() + ".jpg");
        if (imageURL != null) {
            imageView = new ImageView(new Image(String.valueOf(imageURL)));
        } else {
            imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/image/not-image.png"))));
        }

        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        VBox dataBox = new VBox();
        dataBox.setSpacing(5);

        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label descriptionLabel = new Label(movie.getDescription());
        Label yearLabel = new Label("Год: " + Integer.toString(movie.getYear()));
        Label countryLabel = new Label("Страна: " + movie.getCountry());

        dataBox.getChildren().addAll(titleLabel, yearLabel, countryLabel, descriptionLabel);

        Button detailsButton = new Button("Подробнее");
        detailsButton.setOnAction(event -> showDetails(movie)); // Метод для обработки нажатия на кнопку

        card.getChildren().addAll(imageView, dataBox, detailsButton);

        return card;
    }

    private void showDetails(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detail_page.fxml"));
            Parent root = loader.load();

            DetailController detailController = loader.getController();
            detailController.setMovie(movie); // Передаем фильм в контроллер деталей

            // Открываем новое окно
            Stage stage = new Stage();
            stage.setTitle("Детали фильма");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}