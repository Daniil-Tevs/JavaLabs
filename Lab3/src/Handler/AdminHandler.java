package Handler;

import Model.*;
import Repository.Repository;

import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdminHandler {
    static List<Cinema> cinemaList = new ArrayList<>();
    static List<Film> filmList = new ArrayList<>();
    static List<Session> sessionList = new ArrayList<>();
    static ScheduleCinema schedule;


    static private final List<String> adminOperation = new ArrayList<>(
            Arrays.asList("Просмотреть список кинотетаров", "Добавить кинотеатр", "Редактировать кинотетар", "Удалить кинотетар",
                    "Просмотреть список сеансов", "Добавить сеанс", "Изменить сеанс", "Удалить сеанс",
                    "Просмотреть список фильмов", "Добавить фильм", "Изменить фильм", "Удалить фильм"));

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

    static private void getData(Repository repo) {
        cinemaList = repo.getCinemaList();
        filmList = repo.getFilmList();
        sessionList = repo.getSessionList(filmList);
        schedule = repo.getScheduleList(sessionList);
    }


    static public void startHandle(Admin admin, Repository repo) {
        getData(repo);

        while (true) {
            switch (chooseOperate()) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    for (Cinema cinema : cinemaList) {
                        System.out.println(cinema);

                    }
                }
                case 2 -> {
                    System.out.println("Введите название кинотеатра");
                    String cinemaName = scanner.next();

                    if (getCinema(cinemaName) == null) {
                        Cinema newCinema = new Cinema(cinemaName);
                        if (repo.saveCinema(newCinema))
                            cinemaList.add(newCinema);
                    }
                }

                case 3 -> {
                    System.out.println("Введите название кинотеатра");
                    Cinema cinema = getCinema(scanner.next());

                    if (cinema != null) {
                        CinemaHandler.startHandle(cinema, repo);
                    }
                }

                case 4 -> {
                    System.out.println("Введите название кинотеатра");
                    Cinema cinema = getCinema(scanner.next());

                    if (cinema != null) {
                        if (repo.removeCinema(cinema)) {
                            System.out.println("Кинотетар удалён");
                            cinemaList.remove(cinema);
                        }
                    }
                }

                case 5 -> {
                    printSchedule();
                }

                case 6 -> {
                    System.out.println("Введите дату в форматте dd.mm.yyyy");
                    String date = scanner.next();
                    System.out.println("Введите время в форматте hh.mm");
                    String time = scanner.next();

                    System.out.println("Выберите id фильма");
                    printFilms();
                    String filmId = scanner.next();
                    Film film = getFilmById(filmId);

                    if (film != null) {
                        System.out.println("Выберите кинотеатр");
                        for (Cinema cinema : cinemaList) {
                            System.out.println(cinema.getCinemaName());
                        }

                        String cinemaName = scanner.next();
                        Cinema cinema = getCinema(cinemaName);
                        if (cinema != null) {
                            System.out.println("Выберите id зала");
                            for (FilmRoom room : cinema.getFilmRooms()) {
                                System.out.println(room.getFilmRoomId());
                            }
                            String filmRoomId = scanner.next();
                            FilmRoom filmRoom = getFilmRoom(filmRoomId, cinema);
                            Session session = getSession(film, cinemaName, filmRoomId, repo);
                            if (session != null) {
                                if (filmRoom != null) {
                                    ScheduleCinema scheduleTmp = schedule;
                                    schedule.addScheduleItem(date, time, session, false, new ArrayList<>());
                                    if (repo.saveSchedule(schedule)) {
                                        System.out.println("Сеанс был успешно добавлен");
                                    } else {
                                        schedule = scheduleTmp;
                                    }
                                }
                            }

                        }
                    }
                }

                case 7 -> {
                    System.out.println("Введите дату в форматте dd.mm.yyyy");
                    String date = scanner.next();
                    System.out.println("Введите время в форматте hh.mm");
                    String time = scanner.next();

                    List<Object[]> needDateTime = schedule.getSchedule().get(date).get(time);

                    if (needDateTime != null) {
                        System.out.println("Сеансы в этот день");
                        for (Object[] paramDateTime : needDateTime) {
                            Session session = (Session) paramDateTime[0];
                            boolean isFull = (boolean) paramDateTime[1];
                            List<int[]> usedPlaces = (List<int[]>) paramDateTime[2];
                            System.out.println(session.getSessionId() + "\n" + session.getFilm().getFilmName() + "\n" + session.getCinema());
                            System.out.println(session.getFilmRoom());
                            for (int[] place : usedPlaces) {
                                System.out.print("(" + place[0] + " : " + place[1] + ") ");
                            }
                            System.out.println("\n" + isFull);
                        }

                        System.out.println("Выберите id имеющегося сеанса");
                        String sessionId = scanner.next();
                        System.out.println("Введите новый id фильма (no - если не нужно менять)");
                        String movieId = scanner.next();
                        System.out.println("Введите новый кинотеатр (no - если не нужно менять)");
                        String cinemaId = scanner.next();
                        System.out.println("Введите новый зал (no - если не нужно менять)");
                        String filmRoomId = scanner.next();
                        System.out.println("Введите новые места, которые заняты. Формат = '1:2,4:3' (no - если не нужно менять)");
                        String usedPlaces = scanner.next();
                        System.out.println("Введите заполнен ли зал(1, 0)");
                        int isFull = scanner.nextInt();

                        ScheduleCinema scheduleReserve = schedule;

                        for (Object[] objects : needDateTime) {
                            Session session = (Session) objects[0];
                            if (Objects.equals(session.getSessionId(), sessionId)) {
                                Film changeFilm = session.getFilm();
                                if (!Objects.equals(movieId, "no")) {
                                    for (Film film : filmList) {
                                        if (Objects.equals(film.getFilmId(), movieId)) {
                                            changeFilm = film;
                                            break;
                                        }
                                    }
                                }

                                String changeCinema = session.getCinema();
                                if (!Objects.equals(cinemaId, "no")) {
                                    if (getCinema(cinemaId) != null) {
                                        changeCinema = cinemaId;
                                    }
                                }

                                String changeFilmRoom = session.getFilmRoom();
                                if (!Objects.equals(filmRoomId, "no")) {
                                    changeFilmRoom = filmRoomId;
                                }

                                objects[0] = getSession(changeFilm, changeCinema, changeFilmRoom, repo);
                                objects[1] = isFull > 0;

                                if (!Objects.equals(usedPlaces, "no")) {
                                    List<int[]> formattedUsedPlace = new ArrayList<>();

                                    for (String place : usedPlaces.split(",")) {
                                        formattedUsedPlace.add(new int[]{Integer.parseInt(place.split(":")[0]), Integer.parseInt(place.split(":")[1])});
                                    }
                                    objects[2] = formattedUsedPlace;
                                }
                                break;
                            }
                        }

                        if (repo.saveSchedule(schedule)) {
                            System.out.println("Сеанс был успешно изменён");
                        } else {
                            schedule = scheduleReserve;
                        }
                    }
                }

                case 8 -> {
                    System.out.println("Введите дату в форматте dd.mm.yyyy");
                    String date = scanner.next();
                    System.out.println("Введите время в форматте hh.mm");
                    String time = scanner.next();

                    System.out.println("Выберите id имеющегося сеанса");
                    for (Session session : sessionList) {
                        System.out.println(session.getSessionId() + ' ' + session.getCinema() + ' ' + session.getFilmRoom() + ' ' + session.getFilm().getFilmName());
                    }

                    Session sessionObject = getSession(scanner.next());
                    if (sessionObject != null) {
                        ScheduleCinema scheduleReserve = schedule;
                        schedule.removeScheduleItem(date, time, sessionObject);
                        if (repo.saveSchedule(schedule)) {
                            System.out.println("Успешно!");
                        } else {
                            schedule = scheduleReserve;
                            System.out.println("Системная ошибка, сеанс не удалён");
                        }
                    }
                }

                case 9 -> {
                    printFilms();
                }

                case 10 -> {
                    Film newFilm = getNewFilm();

                    filmList.add(newFilm);
                    if (!repo.saveFilm(filmList)) {
                        filmList.remove(newFilm);
                        System.out.println("Ошибка записи в файл. Фильм не был добавлен");
                    }
                }

                case 11 -> {
                    printFilms();

                    System.out.println("Введите id фильма");
                    String idFilm = scanner.next();

                    Film film = getFilmById(idFilm);

                    if (film != null) {
                        List<Film> filmListTmp = filmList;

                        filmList.set(filmList.indexOf(film), changeFilm(film));
                        if (!repo.saveFilm(filmList)) {
                            filmList = filmListTmp;
                            System.out.println("Ошибка записи в файл. Фильм не был изменён");
                        }
                    } else {
                        System.out.println("Введён неверный id фильма");
                    }
                }

                case 12 -> {
                    printFilms();

                    System.out.println("Введите id фильма в списке");

                    String idFilm = scanner.next();

                    Film film = getFilmById(idFilm);

                    if (film != null) {
                        List<Film> filmListTmp = filmList;
                        filmList.remove(film);
                        if (!repo.saveFilm(filmList)) {
                            filmList = filmListTmp;
                            System.out.println("Ошибка записи в файл. Фильм не был удалён");
                        }
                    } else {
                        System.out.println("Введён неверный номер фильма");
                    }
                }
            }
        }
    }
}
