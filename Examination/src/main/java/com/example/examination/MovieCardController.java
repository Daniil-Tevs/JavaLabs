package com.example.examination;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class MovieCardController {

    @FXML
    protected ImageView movieImage;
    @FXML
    protected Label movieTitle;
    @FXML
    protected TextArea movieDescription;
    @FXML
    protected Text movieYear;
    @FXML
    protected Text movieCountry;

    public void setMovieData(String url, String title, String description, String year, String country) {
        movieImage.setImage(new Image(url));
        movieTitle.setText(title);
        movieDescription.setText(description);
        movieYear.setText(year);
        movieCountry.setText(country);
    }

}
