package com.example.examination;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.Movie;

import java.net.URL;
import java.util.ResourceBundle;

public class DetailController implements Initializable {
    @FXML
    private Label titleLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label countryLabel;

    @FXML
    private Label descriptionLabel;

    private Movie movie;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        updateLabels();
    }

    private void updateLabels() {
        titleLabel.setText("Название: " + movie.getTitle());
        yearLabel.setText("Год: " + Integer.toString(movie.getYear()));
        countryLabel.setText("Страна: " + movie.getCountry());
        descriptionLabel.setText(movie.getDescription());

    }
}
