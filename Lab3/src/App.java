import Handler.AdminHandler;
import Handler.UserHandler;
import Model.*;
import Repository.FileRepository;

import java.util.*;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    private static final FileRepository repo = new FileRepository("data/user.txt","data/admin.txt",
            "data/cinema/","data/room/","data/film.txt",
            "data/sessions.txt","data/schedule.txt");
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
        System.out.println("������� �����");
        String login = scanner.next();
        System.out.println("������� ������");
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
        System.out.println("\n**************************\n�����������\n");
        System.out.println("������� �����");
        String login = scanner.next();

        for (int i = 0; i < userList.size(); i++) {
            if(userList.get(i).getLogin().equals(login)) {
                System.out.println("������ ����� ��� �����");
                return null;
            }
        }

        System.out.println("������� ������");
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

        System.out.println("��� ������������ ���� ����������� 'DTP'\n������ ��������:\n1) ����\n2) �����������");

        switch (scanner.nextInt()) {
            case 1 -> {
                User user = login();
                if (user != null)
                    UserHandler.startHandle(repo);
            }
            case 2 -> {
                User user = registration();
                if (user != null)
                    UserHandler.startHandle(repo);
            }

            case 999 -> {
                Admin admin = loginAdmin();
                if (admin != null)
                    AdminHandler.startHandle(admin, repo);
            }
        }
    }
}


