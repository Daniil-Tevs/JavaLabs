package Model;
import java.util.UUID;

public class Session {
    private String sessionId;
    private String cinema;
    private String filmRoom;
    private Film film;

    public Session(Film newFilm, String newCinema, String newFilmRoom, String newSessionId){
        if(newSessionId == null)
            sessionId = UUID.randomUUID().toString();
        else
            sessionId = newSessionId;
        cinema = newCinema;
        filmRoom = newFilmRoom;
        film = newFilm;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getCinema() {
        return cinema;
    }

    public Film getFilm() {
        return film;
    }

    public String getFilmRoom() {
        return filmRoom;
    }

    public void setFilm(Film newFilm){
        film = newFilm;
    }

    public void setFilmRoom(String newFilmRoom){
        filmRoom = newFilmRoom;
    }

    public void setCinema(String newCinema){
        cinema = newCinema;
    }

    @Override
    public String toString() {
        return cinema + ' ' + filmRoom + '\n' + film.toString();
    }
}
