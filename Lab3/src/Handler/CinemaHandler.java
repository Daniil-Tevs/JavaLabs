package Handler;

import Model.Cinema;
import Model.FilmRoom;
import Repository.Repository;

import java.util.*;

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

    static private FilmRoom addFilmRoom(List<FilmRoom> filmRooms, String id) {
        if(id == null){
            System.out.println("Введите индентификатор зала");
            id = scanner.next();
            for (FilmRoom filmRoomExist:filmRooms) {
                if(Objects.equals(id, filmRoomExist.getFilmRoomId()))
                {
                    System.out.println("Такой зал уже существует");
                    return null;
                }
            }
        }

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

        return new FilmRoom(id,width,height,vipPlaces);
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
                        System.out.println(cinema);
                    }
                    else {
                        System.out.println("В этом кинотеатре нет доступных залов");
                    }
                }
                case 2 -> {
                    FilmRoom filmRoom = addFilmRoom(cinema.getFilmRooms(), null);
                    if(filmRoom != null)
                    {
                        if(repo.saveFilmRoom(cinema.getCinemaName(), filmRoom)){
                            cinema.addFilmRoom(filmRoom);
                        }
                    }
                }

                case 3 -> {
                    System.out.println("Введите идентификатор зала для изменения:");
                    String id = scanner.next();
                    List<FilmRoom> filmRoomList = cinema.getFilmRooms();
                    boolean isChange = false;
                    for (int i = 0; i < filmRoomList.size(); i++) {
                        if(Objects.equals(filmRoomList.get(i).getFilmRoomId(), id))
                        {
                            FilmRoom newFimRoom = addFilmRoom(cinema.getFilmRooms(),id);
                            if(newFimRoom != null){
                                filmRoomList.set(i,newFimRoom);
                                cinema.setFilmRooms(filmRoomList);
                                isChange = true;
                                break;
                            }
                        }
                    }

                    if(isChange)
                        System.out.println("Зал " + id + " изменён");
                    else
                        System.out.println("Не существующий игдетификатор класса");
                }
                case 4 -> {
                    System.out.println("Введите идентификатор зала для изменения:");
                    String id = scanner.next();
                    List<FilmRoom> filmRoomList = cinema.getFilmRooms();
                    boolean isRemove = false;
                    for (int i = 0; i < filmRoomList.size(); i++) {
                        if(Objects.equals(filmRoomList.get(i).getFilmRoomId(), id))
                        {
                            if(repo.removeFilmRoom(cinema.getCinemaName(),filmRoomList.get(i))){
                                filmRoomList.remove(i);
                                isRemove = true;
                            }
                            else
                                System.out.println("Системная ошибка");
                            break;
                        }
                    }
                    if(isRemove)
                        System.out.println("Зал " + id + " удалён");
                    else
                        System.out.println("Не существующий игдетификатор класса");
                }
            }
        }
    }
}
