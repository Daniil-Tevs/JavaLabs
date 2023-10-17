package Handler;

import Model.Cinema;
import Model.FilmRoom;
import Repository.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CinemaHandler {
    static private final List<String> cinemaOperation = new ArrayList<>(
            Arrays.asList("Просмотреть список залов", "Добавить зал", "Редактировать зал","Удалить зал"));
    static private final Scanner scanner = new Scanner(System.in);

    static private int chooseOperate(){
        System.out.println("Выберете операцию");
        int index = 0;
        for (String operation : cinemaOperation) {
            System.out.println(++index + " - " + operation);
        }
        System.out.println("0 - Выход");

        return scanner.nextInt();
    }

    static private FilmRoom getFilmRoom(FilmRoom[] filmRoomList, String... args){
        String filmRoomName;
        if(args.length == 0){
            System.out.println("Введите название кинотеатра");
            filmRoomName = scanner.next();
        }
        else{
            filmRoomName = args[0];
        }

        for (FilmRoom filmRoom:filmRoomList) {
            if(filmRoomName.equals(filmRoom.getFilmRoomId()))
                return filmRoom;
        }
        return null;
    }

    public static Cinema startHandle(Cinema cinema, Repository repo){
        while(true)
        {
            switch (chooseOperate()){
                case 0 -> {
                    return cinema;
                }
                case 1 -> {
                    if (!cinema.getFilmRooms().isEmpty()){
                        for (FilmRoom filmRoom:cinema.getFilmRooms()) {
                            System.out.println(filmRoom.getFilmRoomId() + ' ' + filmRoom.getSize());
                        }
                    }
                    else {
                        System.out.println("В этом кинотеатре нет доступных залов");
                    }
                }
                case 2 -> {
                    System.out.println("Введите индентификатор зала");
                    String id = scanner.next();
                    System.out.println("Введите количество мест в ширину зала");
                    int width = scanner.nextInt();
                    System.out.println("Введите количество мест в длину зала");
                    int height = scanner.nextInt();
                    System.out.println("Введите количество вип мест");
                    int vipCount = scanner.nextInt();

                    List<int[]> vipPlaces = new ArrayList<>();

                    while(vipCount > 0)
                    {
                        System.out.println("Введите ряд для вип места");
                        int row = scanner.nextInt();
                        System.out.println("Введите номер в ряде для вип места");
                        int grid = scanner.nextInt();
                        vipPlaces.add(new int[]{row, grid});
                        vipCount--;
                    }

                    FilmRoom filmRoom = new FilmRoom(id,width,height,vipPlaces);

                    if(repo.saveFilmRoom(cinema.getCinemaName(), filmRoom)){
                        cinema.addFilmRoom(filmRoom);
                    }
                }

                case 3 -> {

                }
            }
        }
    }
}
