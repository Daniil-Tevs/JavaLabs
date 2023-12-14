package com.example.examination;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;

public class CatalogController {

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
    private ChoiceBox<String> yearChoiceBox;

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    @FXML
    private ChoiceBox<String> countryChoiceBox;

    public void initialize() {
        // Пример инициализации ChoiceBox со строками параметров
        ObservableList<String> years = FXCollections.observableArrayList(
                "2023",
                "2022",
                "2021"
                // Добавьте другие года, если необходимо
        );

        ObservableList<String> genres = FXCollections.observableArrayList(
                "Драма",
                "Комедия",
                "Боевик"
                // Добавьте другие жанры, если необходимо
        );

        ObservableList<String> countries = FXCollections.observableArrayList(
                "США",
                "Великобритания",
                "Франция"
                // Добавьте другие страны, если необходимо
        );

        yearChoiceBox.setItems(years);
        genreChoiceBox.setItems(genres);
        countryChoiceBox.setItems(countries);

    }


}