package Handler;

import Model.Cinema;
import Model.FilmRoom;
import Repository.Repository;

import java.util.*;

public class CinemaHandler {
    static private final List<String> cinemaOperation = new ArrayList<>(
            Arrays.asList("����������� ������ �����", "�������� ���", "������������� ���","������� ���"));
    static private final Scanner scanner = new Scanner(System.in);

    static private int chooseOperate(){
        System.out.println("�������� ��������");
        int index = 0;
        for (String operation : cinemaOperation) {
            System.out.println(++index + " - " + operation);
        }
        System.out.println("0 - �����");

        return scanner.nextInt();
    }

    static private FilmRoom getFilmRoom(FilmRoom[] filmRoomList, String... args){
        String filmRoomName;
        if(args.length == 0){
            System.out.println("������� �������� ����������");
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
            System.out.println("������� �������������� ����");
            id = scanner.next();
            for (FilmRoom filmRoomExist:filmRooms) {
                if(Objects.equals(id, filmRoomExist.getFilmRoomId()))
                {
                    System.out.println("����� ��� ��� ����������");
                    return null;
                }
            }
        }

        System.out.println("������� ���������� ���� � ������ ����");
        int width = scanner.nextInt();
        System.out.println("������� ���������� ���� � ����� ����");
        int height = scanner.nextInt();
        System.out.println("������� ���������� ��� ����");
        int vipCount = scanner.nextInt();

        List<int[]> vipPlaces = new ArrayList<>();

        while(vipCount > 0)
        {
            System.out.println("������� ��� ��� ��� �����");
            int row = scanner.nextInt();
            System.out.println("������� ����� � ���� ��� ��� �����");
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
                        System.out.println("� ���� ���������� ��� ��������� �����");
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
                    System.out.println("������� ������������� ���� ��� ���������:");
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
                        System.out.println("��� " + id + " ������");
                    else
                        System.out.println("�� ������������ ������������� ������");
                }
                case 4 -> {
                    System.out.println("������� ������������� ���� ��� ���������:");
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
                                System.out.println("��������� ������");
                            break;
                        }
                    }
                    if(isRemove)
                        System.out.println("��� " + id + " �����");
                    else
                        System.out.println("�� ������������ ������������� ������");
                }
            }
        }
    }
}
