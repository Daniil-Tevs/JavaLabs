package Handler;

import Model.*;
import Repository.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserHandler {
    static List<Cinema> cinemaList = new ArrayList<>();
    static List<Film> filmList = new ArrayList<>();
    static List<Session> sessionList = new ArrayList<>();
    static ScheduleCinema schedule;

    static private final List<String> adminOperation = new ArrayList<>(Arrays.asList("Купить билет на кино", "Просмотреть список сеансов"));

    static private final Scanner scanner = new Scanner(System.in);

    static private int chooseOperate() {
        System.out.println("Выберете операцию");
        int index = 0;
        for (String operation : adminOperation) {
            System.out.println(++index + " - " + operation);
        }

        return scanner.nextInt();
    }

    static private Cinema getCinema(String... args) {
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

    static private Film getNewFilm() {
        System.out.println("Введите название фильма");
        String filmTitle = scanner.next();
        System.out.println("Введите страну производства фильма");
        String filmCountry = scanner.next();
        System.out.println("Введите продолжительность фильма в минутах");
        int filmDuration = scanner.nextInt();
        System.out.println("Введите год производства фильма");
        int filmYear = scanner.nextInt();

        return new Film(filmTitle, filmCountry, filmDuration, filmYear, null);
    }

    static private Film changeFilm(Film film) {
        System.out.println("Название:" + film.getFilmName() + "\nИзменить: да/нет?");
        if (scanner.nextLine().equalsIgnoreCase("да")) {
            System.out.println("Введите название фильма");
            film.setFilmName(scanner.next());
        }

        System.out.println("Cтрана производства:" + film.getFilmCountry() + "\nИзменить: да/нет?");
        if (scanner.next().equalsIgnoreCase("да")) {
            System.out.println("Введите страну производства фильма");
            film.setFilmCountry(scanner.next());
        }

        System.out.println("Продолжительность:" + film.getFilmDuration() + "\nИзменить: да/нет?");
        if (scanner.next().equalsIgnoreCase("да")) {
            System.out.println("Введите продолжительность фильма в минутах");
            film.setFilmDuration(scanner.nextInt());
        }

        System.out.println("Год производства:" + film.getFilmYear() + "\nИзменить: да/нет?");
        if (scanner.next().equalsIgnoreCase("да")) {
            System.out.println("Введите год производства фильма");
            film.setFilmYear(scanner.nextInt());
        }

        return film;
    }

    static private void printFilms() {
        for (Film film : filmList) {
            System.out.println(film);
        }
    }

    static private Film getFilmById(String idFilm) {
        for (Film film : filmList) {
            if (film.getFilmId().equals(idFilm)) {
                return film;
            }
        }
        return null;
    }

    static private FilmRoom getFilmRoom(String filmRoomId, Cinema cinema) {
        for (FilmRoom room : cinema.getFilmRooms()) {
            if (Objects.equals(room.getFilmRoomId(), filmRoomId))
                return room;
        }
        return null;
    }

    static private Session getSession(String sessionId) {
        for (Session session : sessionList) {
            if (Objects.equals(session.getSessionId(), sessionId))
                return session;
        }
        return null;
    }

    static private Session getSession(Film film, String cinemaName, String filmRoomId, Repository repo) {
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

    static private void printSchedule() {
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

    static private boolean printScheduleDate(String date) {
        if(!schedule.getSchedule().containsKey(date))
        {
            System.out.println("В данный день нет сеансов");
            return false;
        }
        System.out.println("Сеансы в данный день");

        Map<String, List<Object[]>> dateMap = schedule.getSchedule().get(date);

        LocalTime time = LocalTime.of(0, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!time.equals(LocalTime.of(23, 59))) {
            String formattedTime = time.format(formatter);
            if (dateMap.containsKey(formattedTime)) {
                System.out.println("ВРЕМЯ: " + formattedTime);
                for (Object[] paramSession : dateMap.get(formattedTime)) {
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
        return true;
    }

    static private boolean printScheduleDateTime(String date, String time){
        if(!schedule.getSchedule().containsKey(date))
        {
            System.out.println("В данный день нет сеансов");
            return false;
        }
        else if(!schedule.getSchedule().get(date).containsKey(time))
        {
            System.out.println("На данное время нет сеансов");
            return false;
        }
        for (Object[] paramSession : schedule.getSchedule().get(date).get(time)) {
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
        return true;
    }

    static private boolean printScheduleDateTimeMovie(String date, String time, String movie){
        if(!schedule.getSchedule().containsKey(date))
        {
            System.out.println("В данный день нет сеансов");
            return false;
        }
        else if(!schedule.getSchedule().get(date).containsKey(time))
        {
            System.out.println("На данное время нет сеансов");
            return false;
        }
        for (Object[] paramSession : schedule.getSchedule().get(date).get(time)) {
            Session session = (Session) paramSession[0];

            if (Objects.equals(session.getFilm().getFilmName(), movie))
            {
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
                            } else {
                                System.out.println("Все места заняты");
                                return false;
                            }

                            break;
                        }
                    }
                }

                return true;
            }
        }
        System.out.println("Не найдено сеансов на данный фильм");
        return false;
    }

    static private void getData(Repository repo) {
        cinemaList = repo.getCinemaList();
        filmList = repo.getFilmList();
        sessionList = repo.getSessionList(filmList);
        schedule = repo.getScheduleList(sessionList);
    }


    static public void startHandle(Repository repo) {
        getData(repo);

        while (true) {
            switch (chooseOperate()) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    System.out.println("Введите дату в формате dd.mm.yyyy");
                    String date = scanner.next();
                    if(printScheduleDate(date)){
                        System.out.println("Введите время в формате HH:MM");
                        String time = scanner.next();
                        if(printScheduleDateTime(date,time)){
                            System.out.println("Введите название фильма");
                            String movie = scanner.next() + scanner.nextLine();
                            System.out.println("Выберете свободное место");
                            if(printScheduleDateTimeMovie(date,time,movie)){
                                System.out.println("Введите ряд:");
                                int row = scanner.nextInt();
                                System.out.println("Введите место в ряду:");
                                int placeInRow = scanner.nextInt();


                                for (Object[] paramDateTime:schedule.getSchedule().get(date).get(time)) {
                                    Session session = (Session) paramDateTime[0];
                                    Cinema cinema = getCinema(session.getCinema());
                                    if (cinema!=null){
                                        FilmRoom filmRoom = getFilmRoom(session.getFilmRoom(),cinema);
                                        if(filmRoom != null){
                                            int countPlaces = filmRoom.getFilmRoomHeight() * filmRoom.getFilmRoomWidth();
                                            if(Objects.equals(session.getFilm().getFilmName(), movie)){
                                                boolean isUsed = false;
                                                for (int[] place:(List<int[]>) paramDateTime[2]) {
                                                    if(place[0] - 1 == row - 1 && place[1] - 1 == placeInRow - 1){
                                                        isUsed = true;
                                                        break;
                                                    }
                                                }
                                                if(row <= filmRoom.getFilmRoomHeight() && placeInRow <= filmRoom.getFilmRoomHeight())
                                                if (!isUsed){
                                                    ((List<int[]>) paramDateTime[2]).add(new int[]{row, placeInRow});
                                                    repo.saveSchedule(schedule);
                                                    System.out.println("Успешно");
                                                }
                                                else {

                                                }
                                            }
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
                case 2 -> {
                    printSchedule();
                }
            }
        }
    }
}
