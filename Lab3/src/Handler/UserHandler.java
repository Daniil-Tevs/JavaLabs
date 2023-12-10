package Handler;

import Model.*;
import Repository.Repository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserHandler extends Handler {
    static private final List<String> userOperation = new ArrayList<>(Arrays.asList("������ ����� �� ����", "����������� ������ �������"));
    static private final Scanner scanner = new Scanner(System.in);

    static private void buyTickets(Repository repo){
        System.out.println("������� ���� � ������� dd.mm.yyyy");
        String date = scanner.next();
        if(printScheduleDate(date)){
            System.out.println("������� ����� � ������� HH:MM");
            String time = scanner.next();
            if(printScheduleDateTime(date,time)){
                System.out.println("������� �������� ������");
                String movie = scanner.next() + scanner.nextLine();
                System.out.println("�������� ��������� �����");
                if(printScheduleDateTimeMovie(date,time,movie)){
                    System.out.println("������� ���:");
                    int row = scanner.nextInt();
                    System.out.println("������� ����� � ����:");
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
                                            if(((List<int[]>) paramDateTime[2]).size() == countPlaces)
                                                paramDateTime[1] = true;
                                            repo.saveSchedule(schedule);
                                            System.out.println("������\n...\n...\n...\n...\n...\n\n");
                                            System.out.println("�����\t\t\t�: " + UUID.randomUUID().toString() + "\n\n");
                                            System.out.println("����:\t" + date);
                                            System.out.println("�����:\t" + time);
                                            System.out.println("���������:\t" + cinema.getCinemaName() + "\t���:\t" + filmRoom.getFilmRoomId());
                                            System.out.println("�����:\t" + movie);
                                            System.out.println("���:\t" + row + "\t�����:\t" + placeInRow);
                                        }
                                        else {
                                            System.out.println("������ ����� ������");
                                        }
                                }
                            }

                        }

                    }
                }
            }
        }
    }
    static private boolean printScheduleDate(String date) {
        if(!schedule.getSchedule().containsKey(date))
        {
            System.out.println("� ������ ���� ��� �������");
            return false;
        }
        System.out.println("������ � ������ ����");

        Map<String, List<Object[]>> dateMap = schedule.getSchedule().get(date);

        LocalTime time = LocalTime.of(0, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        while (!time.equals(LocalTime.of(23, 59))) {
            String formattedTime = time.format(formatter);
            if (dateMap.containsKey(formattedTime)) {
                System.out.println("�����: " + formattedTime);
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
        return true;
    }

    static private boolean printScheduleDateTime(String date, String time){
        if(!schedule.getSchedule().containsKey(date))
        {
            System.out.println("� ������ ���� ��� �������");
            return false;
        }
        else if(!schedule.getSchedule().get(date).containsKey(time))
        {
            System.out.println("�� ������ ����� ��� �������");
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
                            System.out.println("����:\n" + session.getFilm().toString().split("- ")[1]);
                        } else {
                            System.out.println("��� ����� ������");
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
            System.out.println("� ������ ���� ��� �������");
            return false;
        }
        else if(!schedule.getSchedule().get(date).containsKey(time))
        {
            System.out.println("�� ������ ����� ��� �������");
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
                                System.out.println("��� ����� ������");
                                return false;
                            }

                            break;
                        }
                    }
                }

                return true;
            }
        }
        System.out.println("�� ������� ������� �� ������ �����");
        return false;
    }

    static public void startHandle(Repository repo) {
        getData(repo);

        while (true) {
            switch (chooseOperate(userOperation)) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    buyTickets(repo);
                }
                case 2 -> {
                    printSchedule();
                }
            }
        }
    }
}
