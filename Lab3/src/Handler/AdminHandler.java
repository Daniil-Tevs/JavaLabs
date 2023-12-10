package Handler;

import Model.*;
import Repository.Repository;

import java.util.*;

public class AdminHandler extends Handler {
    static private final Scanner scanner = new Scanner(System.in);
    static private final List<String> adminOperation = new ArrayList<>(
            Arrays.asList("Просмотреть список кинотетаров", "Добавить кинотеатр", "Редактировать кинотетар", "Удалить кинотетар",
                    "Просмотреть список сеансов", "Добавить сеанс", "Изменить сеанс", "Удалить сеанс",
                    "Просмотреть список фильмов", "Добавить фильм", "Изменить фильм", "Удалить фильм"));

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
        System.out.println("***Изменение фильма***\n\n");
        System.out.println("Название: " + film.getFilmName() + "\nИзменить: да/нет?");
        if (scanner.nextLine().equalsIgnoreCase("да")) {
            System.out.println("Введите новое название фильма");
            film.setFilmName(scanner.next());
        }

        System.out.println("Cтрана производства: " + film.getFilmCountry() + "\nИзменить: да/нет?");
        if (scanner.next().equalsIgnoreCase("да")) {
            System.out.println("Введите новую страну производства фильма");
            film.setFilmCountry(scanner.next());
        }

        System.out.println("Продолжительность: " + film.getFilmDuration() + "\nИзменить: да/нет?");
        if (scanner.next().equalsIgnoreCase("да")) {
            System.out.println("Введите новую продолжительность фильма в минутах");
            film.setFilmDuration(scanner.nextInt());
        }

        System.out.println("Год производства: " + film.getFilmYear() + "\nИзменить: да/нет?");
        if (scanner.next().equalsIgnoreCase("да")) {
            System.out.println("Введите новый год производства фильма");
            film.setFilmYear(scanner.nextInt());
        }

        return film;
    }

    static private void addCinema(Repository repo){
        System.out.println("***Добавление кинотеатра***\n\nВведите название кинотеатра");
        String cinemaName = scanner.next();

        if (getCinema(cinemaName) != null) {
            System.out.println("Кинотеатр с таким названием уже существует");
            return;
        }

        Cinema newCinema = new Cinema(cinemaName);
        if (repo.saveCinema(newCinema)) {
            cinemaList.add(newCinema);
            System.out.println("Добавление нового кинотетра прошло успешно");
        }
        else
            System.out.println("Сохранение нового кинотеатра не выполнено из-за системной ошибки, добавление не было выполнено");
    }

    static private void changeCinema(Repository repo){
        System.out.println("***Изменение кинотеатра***\n\nВведите название кинотеатра");
        Cinema cinema = getCinema(scanner.next());

        if (cinema == null) {
            System.out.println("Кинотеатра с таким названием не существует");
            return;
        }

        CinemaHandler.startHandle(cinema, repo);
        System.out.println("Изменение произошло успешно");
    }

    static private void deleteCinema(Repository repo){
        System.out.println("***Удаление кинотеатра***\n\nВведите название кинотеатра");
        Cinema cinema = getCinema(scanner.next());

        if (cinema == null) {
            System.out.println("Кинотеатр с таким названием не существует");
            return;
        }

        if (repo.removeCinema(cinema)) {
            System.out.println("Кинотетар был удалён");
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
            System.out.println("Сохранение нового кинотеатра не выполнено из-за системной ошибки, добавление не было выполнено");
        }
    }

    static private void addSchedule(Repository repo){
        System.out.println("***Добавление сеанса***\n");
        System.out.println("Введите дату в форматте dd.mm.yyyy");
        String date = scanner.next();
        System.out.println("Введите время в форматте hh:mm");
        String time = scanner.next();

        System.out.println("Имеющиеся фильмы:");
        printFilms();

        System.out.println("Выберете id необходимого фильма");
        String filmId = scanner.next();
        Film film = getFilmById(filmId);

        if (film == null) {
            System.out.println("Фильма с таким id не существует");
            return;
        }

        System.out.println("Имеющиеся кинотеатры:");
        for (Cinema cinema : cinemaList) {
            System.out.println(cinema.getCinemaName());
        }

        System.out.println("Введите название кинотеатра");
        String cinemaName = scanner.next();
        Cinema cinema = getCinema(cinemaName);
        if (cinema != null) {
            System.out.println("Имеющиеся в данном кинотеатре залы:");
            for (FilmRoom room : cinema.getFilmRooms()) {
                System.out.println(room.getFilmRoomId());
            }
            System.out.println("Выберите id зала");
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
                        System.out.println("Сеанс не был добавлен из-за системной ошибки - сохранения в файл");
                    }
                }
                else{
                    System.out.println("Зала с таким названием не существует");
                }
            }
            else{
                System.out.println("Сеанса с данными параметрами не существует");
            }
        }
        else {
            System.out.println("Кинотеатра с таким названием не существует");
        }
    }

    static private void changeSchedule(Repository repo){
        System.out.println("***Изменение сеанса***\n");
        System.out.println("Введите дату в формате dd.mm.yyyy");
        String date = scanner.next();
        System.out.println("Введите время в формате hh:mm");
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
                System.out.println("Сеанс был успешно изменён");
            } else {
                schedule = scheduleReserve;
                System.out.println("Системная ошибка - сеанс не был изменён из-за ошибки сохранения в файл");
            }
        }
    }

    static private void deleteSchedule(Repository repo){
        System.out.println("***Удаление сеанса***\n\nВведите дату в форматте dd.mm.yyyy");
        String date = scanner.next();
        System.out.println("Введите время в форматте hh:mm");
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
                System.out.println("Удаление сеанса произошло успешно");

            } else {
                schedule = scheduleReserve;
                System.out.println("Системная ошибка, сеанс не удалён из-за ошибки сохранения в файл");
            }
        }
    }

    static private void addFilm(Repository repo){
        System.out.println("***Добавление фильма***\n");

        Film newFilm = getNewFilm();

        filmList.add(newFilm);
        if (!repo.saveFilm(filmList)) {
            filmList.remove(newFilm);
            System.out.println("Ошибка записи в файл. Фильм не был добавлен");
        }
        else {
            System.out.println("Добавление фильма произошло успешно");
        }
    }

    static private void changeFilm(Repository repo){
        System.out.println("***Изменение фильма***\n");
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
            else {
                System.out.println("Изменение фильма произошло успешно\n");
            }
        } else {
            System.out.println("Введён неверный id фильма");
        }
    }

    static private void deleteFilm(Repository repo){
        System.out.println("***Удаление фильма***\n");
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
            else {
                System.out.println("Удаление фильма произошло успешно\n");
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
            System.out.println("Введён неверный номер фильма");
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
