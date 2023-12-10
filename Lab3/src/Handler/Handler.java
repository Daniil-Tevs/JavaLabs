package Handler;

import Model.*;
import Repository.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

abstract class Handler {
    static ScheduleCinema schedule;
    static List<Film> filmList = new ArrayList<>();
    static List<Cinema> cinemaList = new ArrayList<>();
    static List<Session> sessionList = new ArrayList<>();
    static protected final Scanner scanner = new Scanner(System.in);

    static protected void getData(Repository repo) {
        cinemaList = repo.getCinemaList();
        filmList = repo.getFilmList();
        sessionList = repo.getSessionList(filmList);
        schedule = repo.getScheduleList(sessionList);
    }
    static protected int chooseOperate(List<String> operations) {
        System.out.println("Список операций:");

        int index = 0;

        for (String operation : operations) {
            System.out.println(++index + " - " + operation);
        }
        return scanner.nextInt();
    }



    static protected Cinema getCinema(String... args) {
        String cinemaName;
        if (args.length == 0) {
            System.out.println("Введите название кинотеатра");
            cinemaName = scanner.next();
        } else {
            cinemaName = args[0];
        }

        for (Cinema cinema : cinemaList) {
            if (cinemaName.equals(cinema.getCinemaName()))
                return cinema;
        }
        return null;
    }
    static protected Film getFilmById(String idFilm) {
        for (Film film : filmList) {
            if (film.getFilmId().equals(idFilm)) {
                return film;
            }
        }
        return null;
    }
    static protected FilmRoom getFilmRoom(String filmRoomId, Cinema cinema) {
        for (FilmRoom room : cinema.getFilmRooms()) {
            if (Objects.equals(room.getFilmRoomId(), filmRoomId))
                return room;
        }
        return null;
    }

    static protected Session getSession(String sessionId) {
        for (Session session : sessionList) {
            if (Objects.equals(session.getSessionId(), sessionId))
                return session;
        }
        return null;
    }

    static protected Session getSession(Film film, String cinemaName, String filmRoomId, Repository repo) {
        for (Session session : sessionList) {
            if (Objects.equals(session.getCinema(), cinemaName) && session.getFilm() == film && Objects.equals(session.getFilmRoom(), filmRoomId)) {
                return session;
            }
        }
        Session session = new Session(film, cinemaName, filmRoomId, null);
        sessionList.add(session);
        if (repo.saveSession(sessionList)) {
            return session;
        } else {
            sessionList.remove(session);
            return null;
        }
    }



    static protected void printCinemaList(){
        for (Cinema cinema : cinemaList) {
            System.out.println(cinema);
        }
    }
    static protected void printFilms() {
        for (Film film : filmList) {
            System.out.println(film);
        }
    }
    static protected void printSchedule() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Map<String, Map<String, List<Object[]>>> scheduleMap = schedule.getSchedule();

        for (int i = 0; i < 30; i++) {
            String formattedDate = dateFormat.format(calendar.getTime());
            if (scheduleMap.containsKey(formattedDate)) {
                System.out.println("ДАТА: " + formattedDate);

                LocalTime time = LocalTime.of(0, 0);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                while (!time.equals(LocalTime.of(23, 50))) {
                    String formattedTime = time.format(formatter);
                    if (scheduleMap.get(formattedDate).containsKey(formattedTime)) {
                        System.out.println("ВРЕМЯ: " + formattedTime);
                        for (Object[] paramSession : scheduleMap.get(formattedDate).get(formattedTime)) {
                            Session session = (Session) paramSession[0];
                            Cinema cinema = getCinema(session.getCinema());

                            if (cinema != null) {
                                System.out.println(cinema.getCinemaName());
                                for (FilmRoom room : cinema.getFilmRooms()) {
                                    if (Objects.equals(room.getFilmRoomId(), session.getFilmRoom())) {
                                        System.out.println(room.getFilmRoomId());
                                        if (!(boolean) paramSession[1]) {
                                            List<int[]> places = (List<int[]>) paramSession[2];
                                            char[][] chairs = room.getFilmRoomChairs();
                                            for (int[] place : places) {
                                                chairs[place[0] - 1][place[1] - 1] = 'X';
                                            }
                                            for (int j = 0; j < room.getFilmRoomHeight(); j++) {
                                                for (int k = 0; k < room.getFilmRoomWidth(); k++) {
                                                    System.out.print(chairs[k][j] + " ");
                                                }
                                                System.out.print("\n");
                                            }
                                            System.out.println("Кино:\n" + session.getFilm().toString().split("- ")[1]);
                                        } else {
                                            System.out.println("Все места заняты");
                                        }

                                        break;
                                    }
                                }
                            }

                        }
                    }
                    time = time.plusMinutes(1);
                }

            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

}
