package Model;
import java.util.UUID;
public class Film {
    private final String filmId;
    private String filmName;
    private String filmCountry;
    private int filmDuration;
    private int filmYear;

    public Film(String newFilmName, String newFilmCountry, int newFilmDuration, int newFilmYear, String newFilmId){
        if(newFilmId == null)
            filmId = UUID.randomUUID().toString();
        else
            filmId = newFilmId;

        filmName = newFilmName;
        filmCountry = newFilmCountry;
        filmDuration = newFilmDuration;
        filmYear = newFilmYear;
    }


    public String getFilmId() {
        return filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getFilmCountry() {
        return filmCountry;
    }
    public int getFilmDuration() {
        return filmDuration;
    }

    public int getFilmYear() {
        return filmYear;
    }

    public String getFilmToFile() {
        return filmId + '~' + filmName + '~' + filmCountry + '~' + Integer.toString(filmDuration) + '~' + Integer.toString(filmYear);
    }



    public void setFilmName(String newFilmName) {
        filmName = newFilmName;
    }

    public void setFilmCountry(String newFilmCountry) {
        filmCountry = newFilmCountry;
    }
    public void setFilmDuration(int newFilmDuration) {
        filmDuration = newFilmDuration;
    }

    public void setFilmYear(int newFilmYear) {
        filmYear = newFilmYear;
    }


    @Override
    public String toString() {
        return filmId + " - " + filmName + "\nYear: " + filmYear + "\nCountry: " + filmCountry + "\nDuration: " + filmDuration + "\n";
    }
}
