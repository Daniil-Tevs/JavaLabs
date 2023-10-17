package Model;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private final String cinemaName;
    private List<FilmRoom> filmRooms;

    public Cinema(String name){
        cinemaName = name;
        this.filmRooms = new ArrayList<>();
    }

    public void setFilmRooms(List<FilmRoom> halls){
        filmRooms = halls;
    }

    public void addFilmRoom(FilmRoom hall){
        filmRooms.add(hall);
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public List<FilmRoom> getFilmRooms() {
        return filmRooms;
    }
}
