package models;

public class Genre {
    private final int id;
    private final String title;

    public Genre(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
