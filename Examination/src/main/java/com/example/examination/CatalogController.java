package com.example.examination;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
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
import javafx.scene.text.Text;
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

        List<Movie> movieList = Database.search(searchTitle, selectedYear, selectedGenre, selectedCountry);

        if (!movieList.isEmpty()){
            VBox cardsContainer = new VBox();
            cardsContainer.setSpacing(30);
            cardsContainer.setPrefWidth(578);

            for (Movie movie : movieList) {
                HBox card = createMovieCard(movie);
                cardsContainer.getChildren().add(card);
            }
            scrollPaneMovies.setContent(cardsContainer);
        }
        else{
            Label notFound = new Label("Ooops! Not found :( ");
            notFound.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
            ImageView imageView = null;
            URL imageURL = getClass().getResource("/com/example/examination/filmfinder.png");
            if (imageURL != null) {
                imageView = new ImageView(new Image(String.valueOf(imageURL)));
            } else {
                imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/image/not-image.png"))));
            }
            VBox notFoundContent = new VBox();
            notFoundContent.getChildren().add(notFound);
            notFoundContent.getChildren().add(imageView);

            notFoundContent.setPadding(new Insets(80,0,0,170));
            scrollPaneMovies.setContent(notFoundContent);
        }
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
        cardsContainer.setSpacing(30);
        cardsContainer.setPrefWidth(578);

        for (Movie movie : movieList) {
            HBox card = createMovieCard(movie);

            cardsContainer.getChildren().add(card);
        }

        scrollPaneMovies.setContent(cardsContainer);
    }

    private HBox createMovieCard(Movie movie) {
        HBox card = new HBox();
        card.setSpacing(10);
        card.setPadding(new Insets(15,15,15,15));

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
        titleLabel.setStyle(" -fx-font-weight: bold; -fx-font-size: 18;  ");

        Label descriptionLabel = new Label(movie.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(400);
        descriptionLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 13px; -fx-fill: #000000; ");

        Label yearLabel = new Label();
        Text yearTextPart1 = new Text("Year: ");
        Text yearTextPart2 = new Text(Integer.toString(movie.getYear()));
        yearTextPart1.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #000000; -fx-font-weight: bold;");
        yearTextPart2.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #000000; ");
        yearLabel.setGraphic(new HBox(yearTextPart1, yearTextPart2));

        Label countryLabel = new Label();
        Text countryTextPart1 = new Text("Country: ");
        Text countryTextPart2 = new Text(movie.getCountry());
        countryTextPart1.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #000000; -fx-font-weight: bold;");
        countryTextPart2.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-fill: #000000; ");
        countryLabel.setGraphic(new HBox(countryTextPart1, countryTextPart2));

        dataBox.getChildren().addAll(titleLabel, yearLabel, countryLabel, descriptionLabel);


        StackPane stackPane = new StackPane();
        Text detailsText = new Text("Details");
        detailsText.setStyle("-fx-font-weight: bold;");

        Button detailsButton = new Button();
        detailsButton.setGraphic(detailsText);

        stackPane.getChildren().add(detailsButton);

        detailsButton.setLayoutX(-15);
        detailsButton.setLayoutY(5);
        detailsButton.setTranslateX(-15);
        detailsButton.setTranslateY(5);
        detailsButton.setMinWidth(100);
        detailsButton.setMinHeight(40);
        detailsButton.setStyle("-fx-background-color: #ead2a0;");

        detailsButton.setOnAction(event -> showDetails(movie));

        card.getChildren().addAll(imageView, dataBox, detailsButton);

        return card;
    }

    private void showDetails(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detail_page.fxml"));
            Parent root = loader.load();

            DetailController detailController = loader.getController();
            detailController.setMovie(movie);

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