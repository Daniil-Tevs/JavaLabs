package models.db;

import models.Movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static String databaseName = "movies";
    private static String url = "jdbc:mysql://localhost:3306/" + databaseName;
    private static String username = "java";
    private static String password = "java1234";
    private static Connection connection = null;

    private static void initConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Movie> getMovieList() {
        initConnection();

        List<Movie> movieList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT id, title, description, duration FROM " + databaseName;
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
//                    System.out.println(resultSet.get());
                    movieList.add(new Movie(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("duration")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movieList;
    }

    public static void main(String[] args) {


        for (Movie movie:getMovieList()) {
            System.out.println(movie.getTitle());
        }
    }
}
