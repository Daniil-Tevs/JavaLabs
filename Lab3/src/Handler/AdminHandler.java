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
            Arrays.asList("����������� ������ �����������", "�������� ���������", "������������� ���������", "������� ���������",
                    "����������� ������ �������", "�������� �����", "�������� �����", "������� �����",
                    "����������� ������ �������", "�������� �����", "�������� �����", "������� �����"));

    static private final Scanner scanner = new Scanner(System.in);

    static private int chooseOperate() {
        System.out.println("�������� ��������");
        int index = 0;
        for (String operation : adminOperation) {
            System.out.println(++index + " - " + operation);
        }

        return scanner.nextInt();
    }

    static private Cinema getCinema(String... args) {
        String cinemaName;
        if (args.length == 0) {
            System.out.println("������� �������� ����������");
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
        System.out.println("������� �������� ������");
        String filmTitle = scanner.next();
        System.out.println("������� ������ ������������ ������");
        String filmCountry = scanner.next();
        System.out.println("������� ����������������� ������ � �������");
        int filmDuration = scanner.nextInt();
        System.out.println("������� ��� ������������ ������");
        int filmYear = scanner.nextInt();

        return new Film(filmTitle, filmCountry, filmDuration, filmYear, null);
    }

    static private Film changeFilm(Film film) {
        System.out.println("��������:" + film.getFilmName() + "\n��������: ��/���?");
        if (scanner.nextLine().equalsIgnoreCase("��")) {
            System.out.println("������� �������� ������");
            film.setFilmName(scanner.next());
        }

        System.out.println("C����� ������������:" + film.getFilmCountry() + "\n��������: ��/���?");
        if (scanner.next().equalsIgnoreCase("��")) {
            System.out.println("������� ������ ������������ ������");
            film.setFilmCountry(scanner.next());
        }

        System.out.println("�����������������:" + film.getFilmDuration() + "\n��������: ��/���?");
        if (scanner.next().equalsIgnoreCase("��")) {
            System.out.println("������� ����������������� ������ � �������");
            film.setFilmDuration(scanner.nextInt());
        }

        System.out.println("��� ������������:" + film.getFilmYear() + "\n��������: ��/���?");
        if (scanner.next().equalsIgnoreCase("��")) {
            System.out.println("������� ��� ������������ ������");
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
                System.out.println("����: " + formattedDate);

                LocalTime time = LocalTime.of(0, 0);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                while (!time.equals(LocalTime.of(23, 50))) {
                    String formattedTime = time.format(formatter);
                    if (scheduleMap.get(formattedDate).containsKey(formattedTime)) {
                        System.out.println("�����: " + formattedTime);
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
                                            System.out.println("����:\n" + session.getFilm().toString().split("- ")[1]);
                                        } else {
                                            System.out.println("��� ����� ������");
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
                    System.out.println("������� �������� ����������");
                    String cinemaName = scanner.next();

                    if (getCinema(cinemaName) == null) {
                        Cinema newCinema = new Cinema(cinemaName);
                        if (repo.saveCinema(newCinema))
                            cinemaList.add(newCinema);
                    }
                }

                case 3 -> {
                    System.out.println("������� �������� ����������");
                    Cinema cinema = getCinema(scanner.next());

                    if (cinema != null) {
                        CinemaHandler.startHandle(cinema, repo);
                    }
                }

                case 4 -> {
                    System.out.println("������� �������� ����������");
                    Cinema cinema = getCinema(scanner.next());

                    if (cinema != null) {
                        if (repo.removeCinema(cinema)) {
                            System.out.println("��������� �����");
                            cinemaList.remove(cinema);
                        }
                    }
                }

                case 5 -> {
                    printSchedule();
                }

                case 6 -> {
                    System.out.println("������� ���� � �������� dd.mm.yyyy");
                    String date = scanner.next();
                    System.out.println("������� ����� � �������� hh.mm");
                    String time = scanner.next();

                    System.out.println("�������� id ������");
                    printFilms();
                    String filmId = scanner.next();
                    Film film = getFilmById(filmId);

                    if (film != null) {
                        System.out.println("�������� ���������");
                        for (Cinema cinema : cinemaList) {
                            System.out.println(cinema.getCinemaName());
                        }

                        String cinemaName = scanner.next();
                        Cinema cinema = getCinema(cinemaName);
                        if (cinema != null) {
                            System.out.println("�������� id ����");
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
                                        System.out.println("����� ��� ������� ��������");
                                    } else {
                                        schedule = scheduleTmp;
                                    }
                                }
                            }

                        }
                    }
                }

                case 7 -> {
                    System.out.println("������� ���� � �������� dd.mm.yyyy");
                    String date = scanner.next();
                    System.out.println("������� ����� � �������� hh.mm");
                    String time = scanner.next();

                    List<Object[]> needDateTime = schedule.getSchedule().get(date).get(time);

                    if (needDateTime != null) {
                        System.out.println("������ � ���� ����");
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

                        System.out.println("�������� id ���������� ������");
                        String sessionId = scanner.next();
                        System.out.println("������� ����� id ������ (no - ���� �� ����� ������)");
                        String movieId = scanner.next();
                        System.out.println("������� ����� ��������� (no - ���� �� ����� ������)");
                        String cinemaId = scanner.next();
                        System.out.println("������� ����� ��� (no - ���� �� ����� ������)");
                        String filmRoomId = scanner.next();
                        System.out.println("������� ����� �����, ������� ������. ������ = '1:2,4:3' (no - ���� �� ����� ������)");
                        String usedPlaces = scanner.next();
                        System.out.println("������� �������� �� ���(1, 0)");
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
                            System.out.println("����� ��� ������� ������");
                        } else {
                            schedule = scheduleReserve;
                        }
                    }
                }

                case 8 -> {
                    System.out.println("������� ���� � �������� dd.mm.yyyy");
                    String date = scanner.next();
                    System.out.println("������� ����� � �������� hh.mm");
                    String time = scanner.next();

                    System.out.println("�������� id ���������� ������");
                    for (Session session : sessionList) {
                        System.out.println(session.getSessionId() + ' ' + session.getCinema() + ' ' + session.getFilmRoom() + ' ' + session.getFilm().getFilmName());
                    }

                    Session sessionObject = getSession(scanner.next());
                    if (sessionObject != null) {
                        ScheduleCinema scheduleReserve = schedule;
                        schedule.removeScheduleItem(date, time, sessionObject);
                        if (repo.saveSchedule(schedule)) {
                            System.out.println("�������!");
                        } else {
                            schedule = scheduleReserve;
                            System.out.println("��������� ������, ����� �� �����");
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
                        System.out.println("������ ������ � ����. ����� �� ��� ��������");
                    }
                }

                case 11 -> {
                    printFilms();

                    System.out.println("������� id ������");
                    String idFilm = scanner.next();

                    Film film = getFilmById(idFilm);

                    if (film != null) {
                        List<Film> filmListTmp = filmList;

                        filmList.set(filmList.indexOf(film), changeFilm(film));
                        if (!repo.saveFilm(filmList)) {
                            filmList = filmListTmp;
                            System.out.println("������ ������ � ����. ����� �� ��� ������");
                        }
                    } else {
                        System.out.println("����� �������� id ������");
                    }
                }

                case 12 -> {
                    printFilms();

                    System.out.println("������� id ������ � ������");

                    String idFilm = scanner.next();

                    Film film = getFilmById(idFilm);

                    if (film != null) {
                        List<Film> filmListTmp = filmList;
                        filmList.remove(film);
                        if (!repo.saveFilm(filmList)) {
                            filmList = filmListTmp;
                            System.out.println("������ ������ � ����. ����� �� ��� �����");
                        }
                    } else {
                        System.out.println("����� �������� ����� ������");
                    }
                }
            }
        }
    }
}
