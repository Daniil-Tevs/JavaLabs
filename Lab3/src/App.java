import Handler.AdminHandler;
import Handler.UserHandler;
import Model.*;
import Repository.FileRepository;

import java.util.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    private static final FileRepository repo = new FileRepository("data/user.txt","data/admin.txt",
            "data/cinema/","data/room/","/data/movie.txt");

    static List<User> userList;
    static List<Admin> adminList;

    static boolean isLogin = false;
    static boolean isAdmin = false;

    private static void getData(){
        userList = repo.getUserList();
        adminList = repo.getAdminList();
    }

    static private String[] getLoginData(){
        String[] loginData = new String[2];
        System.out.println("Введите логин");
        String login = scanner.next();
        System.out.println("Введите пароль");
        String password = scanner.next();

        loginData[0] = login;
        loginData[1] = password;

        return loginData;
    }

    static private User login() {
        String[] loginData = getLoginData();

        for (User user : userList) {
            if (user.login(loginData[0], loginData[1]))
                return user;
        }

        return null;
    }

    static private Admin loginAdmin() {
        String[] loginData = getLoginData();

        for (Admin admin : adminList) {
            if (admin.login(loginData[0], loginData[1]))
                return admin;
        }

        return null;
    }

    static private User registration() {
        System.out.println("\n**************************\nРегистрация\n");
        System.out.println("Введите логин");
        String login = scanner.next();

        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getLogin().equals(login)) {
                System.out.println("Логин уже занят");
                return null;
            }
        }

        System.out.println("Введите пароль");
        String password = scanner.next();

        User user = new User(login,password);
        userList.add(user);

        if(repo.saveUser(user))
            return user;
        else{
            return null;
        }
    }

    public static void start(){
        getData();

        System.out.println("Вас приветствует сеть кинотеатров\nВыберете операцию:\n1) Вход\n2) Регистрация");

        switch (scanner.nextInt()) {
            case 1 -> {
                User user = login();
                if (user != null)
                    System.out.println("user");
            }
            case 2 -> {
                User user = registration();
                if (user != null)
                    System.out.println("newUser");
            }

            case 999 -> {
                Admin admin = loginAdmin();
                if (admin != null)
                    AdminHandler.startHandle(admin, repo);
            }
        }
    }
}


