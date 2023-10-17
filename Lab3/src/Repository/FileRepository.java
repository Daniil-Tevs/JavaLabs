package Repository;

import Model.Admin;
import Model.Cinema;
import Model.FilmRoom;
import Model.User;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileRepository extends Repository{
    public FileRepository(String newUserFilePath, String newAdminFilePath, String newCinemaFilePath, String newFilmRoomFilePath, String newMovieFilePath) {
        super(newUserFilePath, newAdminFilePath, newCinemaFilePath, newFilmRoomFilePath, newMovieFilePath);
    }

    @Override
    public List<User> getUserList(){
        List<User> users = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(userFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] exploded = line.split(" ");
                users.add(new User(exploded[0],exploded[1]));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<Admin> getAdminList() {
        List<Admin> admins = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(adminFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] exploded = line.split(" ");
                admins.add(new Admin(exploded[0],exploded[1]));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return admins;
    }

    @Override
    public List<Cinema> getCinemaList() {
        List<Cinema> cinameList = new ArrayList<>();
        try {

            Path folder = Paths.get(cinemaFilePath);

            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folder);
            for (Path file : directoryStream) {
                if (Files.isRegularFile(file)) {
                    FileReader fileReader = new FileReader(cinemaFilePath + file.getFileName());
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String cinemaName = bufferedReader.readLine();
                    bufferedReader.close();
                    if(cinemaName == null)
                        break;
                    Path folderFilmRooms = Paths.get(filmRoomFilePath);

                    DirectoryStream<Path> directoryStreamFilmRooms = Files.newDirectoryStream(folderFilmRooms);
                    List<FilmRoom> filmRoomsList = new ArrayList<>();
                    for (Path fileFilmRoom : directoryStreamFilmRooms) {
                        String fileName = String.valueOf(fileFilmRoom.getFileName());
                        if (Files.isRegularFile(fileFilmRoom)) {
                            if(fileName.startsWith(cinemaName))
                            {
                                FileReader fileReaderFilmRoom = new FileReader(filmRoomFilePath + fileName);
                                BufferedReader bufferedReaderFilmRoom = new BufferedReader(fileReaderFilmRoom);
                                String id = bufferedReaderFilmRoom.readLine();
                                String[] size = bufferedReaderFilmRoom.readLine().split("x");
                                int width = Integer.parseInt(size[0]);
                                int height = Integer.parseInt(size[1]);
                                char[][] chairs = new char[width][height];

                                String line;
                                int i = 0;
                                while((line = bufferedReaderFilmRoom.readLine()) != null)
                                {
                                    String[] chairsLine = line.split(" ");
                                    for (int j = 0; j < chairsLine.length; j++) {
                                        chairs[j][i] = chairsLine[j].charAt(0);
                                    }
                                    i++;
                                }
                                FilmRoom newFilmRoom = new FilmRoom(id,width,height, new ArrayList<>());
                                newFilmRoom.setFilmRoomChairs(chairs);
                                filmRoomsList.add(newFilmRoom);

                                bufferedReaderFilmRoom.close();
                            }
                        }
                    }

                    Cinema newCinema = new Cinema(cinemaName);
                    newCinema.setFilmRooms(filmRoomsList);
                    cinameList.add(newCinema);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cinameList;
    }

    @Override
    public boolean saveUser(User user) {
        try {
            FileWriter fileWriter = new FileWriter(userFilePath, true);

            fileWriter.write("\n" + user.getLogin() + ' ' + user.getPassword());

            fileWriter.close();

            return true;
        }
        catch (IOException e) {
            System.err.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean saveCinema(Cinema cinema) {
        try {
            FileWriter fileWriter = new FileWriter(cinemaFilePath + '/' + cinema.getCinemaName() + ".txt", true);

            fileWriter.write(cinema.getCinemaName());

            fileWriter.close();

            return true;
        }
        catch (IOException e) {
            System.err.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean saveFilmRoom(String cinemaName, FilmRoom filmRoom) {
        try {
            FileWriter fileWriter = new FileWriter(filmRoomFilePath + '/' + cinemaName + '-' + filmRoom.getFilmRoomId() + ".txt", true);

            fileWriter.write(filmRoom.getFilmRoomId() + '\n');
            fileWriter.write(filmRoom.getSize() + '\n');

            char[][] chairs = filmRoom.getFilmRoomChairs();
            for (int i = 0; i < filmRoom.getFilmRoomWidth(); i++) {
                String chairLine = "";
                for (int j = 0; j < filmRoom.getFilmRoomHeight(); j++) {
                    chairLine += chairs[i][j] + ' ';
                }
                fileWriter.write(chairLine + '\n');
            }

            fileWriter.close();

            return true;
        }
        catch (IOException e) {
            System.err.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }

        return false;
    }
}
