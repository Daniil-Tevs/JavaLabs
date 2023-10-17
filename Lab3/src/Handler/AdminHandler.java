package Handler;

import Model.Admin;
import Model.Cinema;
import Model.FilmRoom;
import Model.Movie;
import Repository.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class AdminHandler {
    static List<Cinema> cinemaList = new ArrayList<>();
    static List<Movie> movieList = new ArrayList<>();

    static private final List<String> adminOperation = new ArrayList<>(
            Arrays.asList("Просмотреть список кинотетаров", "Добавить кинотеатр", "Редактировать кинотетар","Удалить кинотетар",
                    "Просмотреть список сеансов","Добавить сеанс","Изменить сеанс", "Удалить сеанс"));

    static private final List<String> cinemaOperation = new ArrayList<>(
            Arrays.asList("Просмотреть список залов", "Добавить зал", "Редактировать зал","Удалить зал"));
    static private final Scanner scanner = new Scanner(System.in);

    static private int chooseOperate(){
        System.out.println("Выберете операцию");
        int index = 0;
        for (String operation : adminOperation) {
            System.out.println(++index + " - " + operation);
        }

        return scanner.nextInt();
    }

    static private Cinema getCinema(String... args){
        String cinemaName;
        if(args.length == 0){
            System.out.println("Введите название кинотеатра");
            cinemaName = scanner.next();
        }
        else{
            cinemaName = args[0];
        }

        for (Cinema cinema:cinemaList) {
            if(cinemaName.equals(cinema.getCinemaName()))
                return cinema;
        }
        return null;
    }

    static private void getData(Repository repo){
        cinemaList = repo.getCinemaList();
    }
    static public void startHandle(Admin admin, Repository repo){

        getData(repo);

        while(true)
        {
            switch (chooseOperate()){
                case 1 -> {

                }
                case 2 -> {
                    System.out.println("Введите название кинотеатра");
                    String cinemaName = scanner.next();

                    if (getCinema(cinemaName) == null)
                    {
                        Cinema newCinema = new Cinema(cinemaName);
                        if(repo.saveCinema(newCinema))
                            cinemaList.add(newCinema);
                    }
                }

                case 3 -> {
                    System.out.println("Введите название кинотеатра");
                    Cinema cinema = getCinema(scanner.next());

                    if (cinema != null)
                    {
                        CinemaHandler.startHandle(cinema,repo);
                    }
                }
            }
        }
    }
}
