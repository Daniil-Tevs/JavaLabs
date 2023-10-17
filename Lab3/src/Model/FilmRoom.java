package Model;

import java.util.List;

public class FilmRoom {
    private final String filmRoomId;
    private char[][] filmRoomChairs;
    private int filmRoomWidth = 1;
    private int filmRoomHeight = 1;
    public FilmRoom(String id, int width, int height, List<int[]> vipPlaces){
        filmRoomId = id;
        if (width > 0)
            filmRoomWidth = width;

        if(height > 0)
            filmRoomHeight = height;

        filmRoomChairs = new char[filmRoomWidth][filmRoomHeight];

        for (int i = 0; i < filmRoomWidth; i++) {
            for (int j = 0; j < filmRoomHeight; j++) {
                filmRoomChairs[i][j] = 'S';
            }
        }

        for (int[] vipPlace:vipPlaces) {
            if(vipPlace[0] < filmRoomWidth && vipPlace[1] < filmRoomHeight)
                filmRoomChairs[vipPlace[0]][vipPlace[1]] = 'V';
        }
    }
    public String getSize() {
        return  Integer.toString(filmRoomWidth) + 'x' + Integer.toString(filmRoomHeight);
    }

    public void setFilmRoomChairs(char[][] chairs){
        filmRoomChairs = chairs;
    }

    public String getFilmRoomId() {
        return filmRoomId;
    }

    public int getFilmRoomWidth() {
        return filmRoomWidth;
    }

    public int getFilmRoomHeight() {
        return filmRoomHeight;
    }

    public char[][] getFilmRoomChairs() {
        return filmRoomChairs;
    }
}
