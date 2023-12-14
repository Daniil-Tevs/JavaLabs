package models;

public class Movie {
    private final int id;
    private final String title;
    private final String description;
    private final int duration;
    private final int year;
    private final String country;


    public Movie(int id, String title, String description, int duration, int year, String country) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.year = year;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public int getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }
}
