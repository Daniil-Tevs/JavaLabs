package models.db;

import models.Genre;
import models.Movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

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
            String query = "SELECT movies.id as id, movies.title as title, description, duration, year, c.title as country FROM movies Left Join movies.country c on c.id = movies.country_id";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
//                    System.out.println(resultSet.get());
                    movieList.add(new Movie(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("duration"), resultSet.getInt("year"), resultSet.getString("country")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movieList;
    }

    public static List<Genre> getGenreList() {
        initConnection();

        List<Genre> genreList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT id, title FROM genre";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
//                    System.out.println(resultSet.get());
                    genreList.add(new Genre(resultSet.getInt("id"), resultSet.getString("title")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return genreList;
    }

    public static Map<String, List<Movie>> getMovieByGenre() {
        initConnection();

        Map<String, List<Movie>> mapMovieByGenre = new HashMap<>();

        try (Statement statement = connection.createStatement()) {
            String query = "SELECT m.id as id, m.title as title, m.description as description , m.duration as duration, m.year as year, c.title as country, g.title as genre FROM movie_genre b LEFT JOIN genre g on g.id = b.genre_id LEFT JOIN movies m on m.id = b.movie_id LEFT JOIN movies.country c on c.id = m.country_id;";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    if (!mapMovieByGenre.containsKey(resultSet.getString("genre")))
                        mapMovieByGenre.put(resultSet.getString("genre"), new ArrayList<>());
                    mapMovieByGenre.get(resultSet.getString("genre")).add(new Movie(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("duration"), resultSet.getInt("year"), resultSet.getString("country")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mapMovieByGenre;
    }

    public static List<Movie> getMovieListByGenre(String genre) {
        initConnection();

        List<Movie> movieList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT DISTINCT m.id, m.title, m.description, m.duration, m.year as year, c.title as country FROM movies m LEFT JOIN movies.country c on c.id = m.country_id LEFT JOIN movie_genre mg on m.id = mg.movie_id LEFT JOIN genre g on g.id = mg.genre_id where LOWER(g.title) like '%" + genre.toLowerCase() + "%'";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
//                    System.out.println(resultSet.get());
                    movieList.add(new Movie(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("duration"), resultSet.getInt("year"), resultSet.getString("country")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movieList;
    }

    public static List<Movie> getMovieListByGenres(String genreFirst, String genreSecond) {
        initConnection();

        List<Movie> moviesByFirstGenre = getMovieListByGenre(genreFirst);
        List<Movie> moviesBySecondGenre = getMovieListByGenre(genreSecond);

        Map<Integer,Movie> map = new HashMap<>();

        for (Movie movie2:moviesBySecondGenre) {
            for (Movie movie1:moviesByFirstGenre) {
                if(movie1.getId() == movie2.getId() && !map.containsKey(movie1.getId()))
                    map.put(movie1.getId(), movie1);
            }
        }

        return new ArrayList<>(map.values());
    }

    public static List<Movie> getMovieListByTitle(String title) {
        initConnection();

        List<Movie> movieList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT movies.id as id, movies.title as title, description, duration, year , c.title as country FROM movies LEFT JOIN movies.country c on c.id = movies.country_id WHERE Lower(movies.title) like '%Один%'" + title.toLowerCase() + "%'";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    movieList.add(new Movie(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("duration"), resultSet.getInt("year"), resultSet.getString("country")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movieList;
    }

    public static List<String> getCountryList() {
        initConnection();

        List<String> countryList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "select title from country";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    countryList.add(resultSet.getString("title"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return countryList;
    }

    public static List<Movie> search(String title, String year, String genre, String country) {
        initConnection();

        List<Movie> movieList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT DISTINCT m.id as id, m.title as title, m.description as description , m.duration as duration, m.year as year, c.title as country FROM movies m LEFT JOIN movies.movie_genre mg on m.id = mg.movie_id LEFT JOIN genre g on g.id = mg.genre_id LEFT JOIN movies.country c on c.id = m.country_id WHERE m.id > 0 ";
            if (!Objects.equals(title, ""))
                query += " AND LOWER(m.title) like '%" + title.toLowerCase() + "%' ";

            if (year != null && !year.equals("Все"))
                query += " AND m.year = " + Integer.parseInt(year);

            if (genre != null && !genre.equals("Все"))
                query += " AND LOWER(g.title) like '%" + genre.toLowerCase() + "%' ";

            if (country != null && !country.equals("Все"))
                query += " AND LOWER(c.title) like '%" + country.toLowerCase() + "%' ";

            System.out.println(query);

            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    movieList.add(new Movie(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("description"), resultSet.getInt("duration"), resultSet.getInt("year"), resultSet.getString("country")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movieList;
    }


    public static List<String> getYearList() {
        initConnection();

        List<String> yearList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            String query = "select distinct year from movies order by year";
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    yearList.add(resultSet.getString("year"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return yearList;
    }


//    public static void main(String[] args) {

//        Map<String, List<Movie>> map = getMovieByGenre();
//        for (String key: map.keySet()) {
//            System.out.println(key);
//            for (Movie movie:map.get(key)) {
//                System.out.println(movie.getTitle());
//            }
//
//        }

//        for (Movie movie : getMovieListByGenre("drama")) {
//            System.out.println(movie.getTitle());
//        }
//
//        for (Movie movie : getMovieListByTitle("ie3")) {
//            System.out.println(movie.getTitle());
//        }


//    }
}
