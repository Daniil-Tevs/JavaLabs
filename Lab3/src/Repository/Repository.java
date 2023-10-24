package Repository;

import Model.*;

import java.util.List;

public abstract class Repository {
    protected static String userFilePath;
    protected static String adminFilePath;
    protected static String cinemaFilePath;
    protected static String filmRoomFilePath;
    protected static String sessionFilePath;
    protected static String scheduleFilePath;
    protected static String filmFilePath;

    public abstract List<User> getUserList();
    public abstract List<Admin> getAdminList();
    public abstract List<Cinema> getCinemaList();
    public abstract List<Film> getFilmList();
    public abstract List<Session> getSessionList(List<Film> filmList);
    public abstract ScheduleCinema getScheduleList(List<Session> sessionList);


    public abstract boolean saveUser(User user);
    public abstract boolean saveCinema(Cinema cinema);
    public abstract boolean saveFilmRoom(String cinemaName, FilmRoom filmRoom);
    public abstract boolean saveFilm(List<Film> filmList);
    public abstract boolean saveSession(List<Session> sessionList);
    public abstract boolean saveSchedule(ScheduleCinema schedule);

    public abstract boolean removeCinema(Cinema cinema);
    public abstract boolean removeFilmRoom(String cinemaName, FilmRoom filmRoom);

    Repository(String newUserFilePath, String newAdminFilePath, String newCinemaFilePath, String newFilmRoomFilePath,
               String newMovieFilePath, String newSessionFilePath, String newScheduleFilePath){
        userFilePath     = newUserFilePath;
        adminFilePath    = newAdminFilePath;
        filmFilePath     = newMovieFilePath;
        cinemaFilePath   = newCinemaFilePath;
        filmRoomFilePath = newFilmRoomFilePath;
        sessionFilePath  = newSessionFilePath;
        scheduleFilePath = newScheduleFilePath;
    }

    public void setUserFilePath(String path) {
        userFilePath = path;
    }
    public void setCinemaFilePath(String path) {
        cinemaFilePath = path;
    }

}
