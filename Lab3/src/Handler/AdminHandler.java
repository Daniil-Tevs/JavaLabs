package Handler;

import Model.*;
import Repository.Repository;

import java.util.*;

public class AdminHandler extends Handler {
    static private final Scanner scanner = new Scanner(System.in);
    static private final List<String> adminOperation = new ArrayList<>(
            Arrays.asList("����������� ������ �����������", "�������� ���������", "������������� ���������", "������� ���������",
                    "����������� ������ �������", "�������� �����", "�������� �����", "������� �����",
                    "����������� ������ �������", "�������� �����", "�������� �����", "������� �����"));

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
        System.out.println("***��������� ������***\n\n");
        System.out.println("��������: " + film.getFilmName() + "\n��������: ��/���?");
        if (scanner.nextLine().equalsIgnoreCase("��")) {
            System.out.println("������� ����� �������� ������");
            film.setFilmName(scanner.next());
        }

        System.out.println("C����� ������������: " + film.getFilmCountry() + "\n��������: ��/���?");
        if (scanner.next().equalsIgnoreCase("��")) {
            System.out.println("������� ����� ������ ������������ ������");
            film.setFilmCountry(scanner.next());
        }

        System.out.println("�����������������: " + film.getFilmDuration() + "\n��������: ��/���?");
        if (scanner.next().equalsIgnoreCase("��")) {
            System.out.println("������� ����� ����������������� ������ � �������");
            film.setFilmDuration(scanner.nextInt());
        }

        System.out.println("��� ������������: " + film.getFilmYear() + "\n��������: ��/���?");
        if (scanner.next().equalsIgnoreCase("��")) {
            System.out.println("������� ����� ��� ������������ ������");
            film.setFilmYear(scanner.nextInt());
        }

        return film;
    }

    static private void addCinema(Repository repo){
        System.out.println("***���������� ����������***\n\n������� �������� ����������");
        String cinemaName = scanner.next();

        if (getCinema(cinemaName) != null) {
            System.out.println("��������� � ����� ��������� ��� ����������");
            return;
        }

        Cinema newCinema = new Cinema(cinemaName);
        if (repo.saveCinema(newCinema)) {
            cinemaList.add(newCinema);
            System.out.println("���������� ������ ��������� ������ �������");
        }
        else
            System.out.println("���������� ������ ���������� �� ��������� ��-�� ��������� ������, ���������� �� ���� ���������");
    }

    static private void changeCinema(Repository repo){
        System.out.println("***��������� ����������***\n\n������� �������� ����������");
        Cinema cinema = getCinema(scanner.next());

        if (cinema == null) {
            System.out.println("���������� � ����� ��������� �� ����������");
            return;
        }

        CinemaHandler.startHandle(cinema, repo);
        System.out.println("��������� ��������� �������");
    }

    static private void deleteCinema(Repository repo){
        System.out.println("***�������� ����������***\n\n������� �������� ����������");
        Cinema cinema = getCinema(scanner.next());

        if (cinema == null) {
            System.out.println("��������� � ����� ��������� �� ����������");
            return;
        }

        if (repo.removeCinema(cinema)) {
            System.out.println("��������� ��� �����");
            cinemaList.remove(cinema);
            for (String date:schedule.getSchedule().keySet()) {
                for (String time: schedule.getSchedule().get(date).keySet()) {
                    List<Object[]> needDelete = new ArrayList<>();
                    for (Object[] param:schedule.getSchedule().get(date).get(time)) {
                        Session session = (Session) param[0];
                        if(Objects.equals(session.getCinema(), cinema.getCinemaName())){
                            needDelete.add(param);
                        }
                    }
                    for (Object[] paramDelete:needDelete) {
                        schedule.getSchedule().get(date).get(time).remove(paramDelete);
                    }
                }
            }
            repo.saveSchedule(schedule);
            for (FilmRoom filmRoom:cinema.getFilmRooms()) {
                repo.removeFilmRoom(cinema.getCinemaName(),filmRoom);
            }
        }
        else {
            System.out.println("���������� ������ ���������� �� ��������� ��-�� ��������� ������, ���������� �� ���� ���������");
        }
    }

    static private void addSchedule(Repository repo){
        System.out.println("***���������� ������***\n");
        System.out.println("������� ���� � �������� dd.mm.yyyy");
        String date = scanner.next();
        System.out.println("������� ����� � �������� hh:mm");
        String time = scanner.next();

        System.out.println("��������� ������:");
        printFilms();

        System.out.println("�������� id ������������ ������");
        String filmId = scanner.next();
        Film film = getFilmById(filmId);

        if (film == null) {
            System.out.println("������ � ����� id �� ����������");
            return;
        }

        System.out.println("��������� ����������:");
        for (Cinema cinema : cinemaList) {
            System.out.println(cinema.getCinemaName());
        }

        System.out.println("������� �������� ����������");
        String cinemaName = scanner.next();
        Cinema cinema = getCinema(cinemaName);
        if (cinema != null) {
            System.out.println("��������� � ������ ���������� ����:");
            for (FilmRoom room : cinema.getFilmRooms()) {
                System.out.println(room.getFilmRoomId());
            }
            System.out.println("�������� id ����");
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
                        System.out.println("����� �� ��� �������� ��-�� ��������� ������ - ���������� � ����");
                    }
                }
                else{
                    System.out.println("���� � ����� ��������� �� ����������");
                }
            }
            else{
                System.out.println("������ � ������� ����������� �� ����������");
            }
        }
        else {
            System.out.println("���������� � ����� ��������� �� ����������");
        }
    }

    static private void changeSchedule(Repository repo){
        System.out.println("***��������� ������***\n");
        System.out.println("������� ���� � ������� dd.mm.yyyy");
        String date = scanner.next();
        System.out.println("������� ����� � ������� hh:mm");
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
                    String changeFilmRoom = session.getFilmRoom();

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
                System.out.println("��������� ������ - ����� �� ��� ������ ��-�� ������ ���������� � ����");
            }
        }
    }

    static private void deleteSchedule(Repository repo){
        System.out.println("***�������� ������***\n\n������� ���� � �������� dd.mm.yyyy");
        String date = scanner.next();
        System.out.println("������� ����� � �������� hh:mm");
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
                System.out.println("�������� ������ ��������� �������");

            } else {
                schedule = scheduleReserve;
                System.out.println("��������� ������, ����� �� ����� ��-�� ������ ���������� � ����");
            }
        }
    }

    static private void addFilm(Repository repo){
        System.out.println("***���������� ������***\n");

        Film newFilm = getNewFilm();

        filmList.add(newFilm);
        if (!repo.saveFilm(filmList)) {
            filmList.remove(newFilm);
            System.out.println("������ ������ � ����. ����� �� ��� ��������");
        }
        else {
            System.out.println("���������� ������ ��������� �������");
        }
    }

    static private void changeFilm(Repository repo){
        System.out.println("***��������� ������***\n");
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
            else {
                System.out.println("��������� ������ ��������� �������\n");
            }
        } else {
            System.out.println("����� �������� id ������");
        }
    }

    static private void deleteFilm(Repository repo){
        System.out.println("***�������� ������***\n");
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
            else {
                System.out.println("�������� ������ ��������� �������\n");
                for (String date:schedule.getSchedule().keySet()) {
                    for (String time: schedule.getSchedule().get(date).keySet()) {
                        List<Object[]> needDelete = new ArrayList<>();
                        for (Object[] param:schedule.getSchedule().get(date).get(time)) {
                            Session session = (Session) param[0];
                            if(Objects.equals(session.getFilm().getFilmId(), film.getFilmId())){
                                needDelete.add(param);
                            }
                        }
                        for (Object[] paramDelete:needDelete) {
                            schedule.getSchedule().get(date).get(time).remove(paramDelete);
                        }
                    }
                }
            }
        } else {
            System.out.println("����� �������� ����� ������");
        }
    }

    static public void startHandle(Admin admin, Repository repo) {
        getData(repo);

        while (true) {
            switch (chooseOperate(adminOperation)) {
                case 0 -> {
                    return;
                }

                case 1 -> {
                    printCinemaList();
                }

                case 2 -> {
                    addCinema(repo);
                }

                case 3 -> {
                    changeCinema(repo);
                }

                case 4 -> {
                    deleteCinema(repo);
                }

                case 5 -> {
                    printSchedule();
                }

                case 6 -> {
                    addSchedule(repo);
                }

                case 7 -> {
                    changeSchedule(repo);
                }

                case 8 -> {
                    deleteSchedule(repo);
                }

                case 9 -> {
                    printFilms();
                }

                case 10 -> {
                    addFilm(repo);
                }

                case 11 -> {
                    printFilms();
                    changeFilm(repo);
                }

                case 12 -> {
                    printFilms();
                    deleteFilm(repo);
                }
            }
        }
    }
}
