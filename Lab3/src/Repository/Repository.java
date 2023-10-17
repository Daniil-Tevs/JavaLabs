package Repository;

import Model.FilmRoom;
import Model.User;
import Model.Admin;
import Model.Cinema;

import java.util.List;

public abstract class Repository {
    protected static String userFilePath;
    protected static String adminFilePath;
    protected static String cinemaFilePath;
    protected static String filmRoomFilePath;
    protected static String movieFilePath;

    public abstract List<User> getUserList();
    public abstract List<Admin> getAdminList();
    public abstract List<Cinema> getCinemaList();

    public abstract boolean saveUser(User user);
    public abstract boolean saveCinema(Cinema cinema);
    public abstract boolean saveFilmRoom(String cinemaName, FilmRoom filmRoom);

    Repository(String newUserFilePath, String newAdminFilePath, String newCinemaFilePath, String newFilmRoomFilePath, String newMovieFilePath){
        userFilePath     = newUserFilePath;
        adminFilePath    = newAdminFilePath;
        movieFilePath    = newMovieFilePath;
        cinemaFilePath   = newCinemaFilePath;
        filmRoomFilePath = newFilmRoomFilePath;
    }

    public void setUserFilePath(String path) {
        userFilePath = path;
    }
    public void setCinemaFilePath(String path) {
        cinemaFilePath = path;
    }

}
