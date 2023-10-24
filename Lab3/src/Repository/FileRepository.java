package Repository;

import Model.*;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileRepository extends Repository{
    public FileRepository(String newUserFilePath, String newAdminFilePath, String newCinemaFilePath, String newFilmRoomFilePath, String newMovieFilePath, String newSessionFilePath, String newScheduleFilePath) {
        super(newUserFilePath, newAdminFilePath, newCinemaFilePath, newFilmRoomFilePath, newMovieFilePath, newSessionFilePath, newScheduleFilePath);
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
    public List<Film> getFilmList() {
        List<Film> filmList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filmFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] exploded = line.split("~");
                filmList.add(new Film(exploded[1],exploded[2], Integer.parseInt(exploded[3]), Integer.parseInt(exploded[4]),exploded[0]));
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filmList;
    }

    @Override
    public List<Session> getSessionList(List<Film> filmList) {
        List<Session> sessionList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(sessionFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            Map<String,Film> filmMap = new HashMap<>();
            for (Film film:filmList) {
                filmMap.put(film.getFilmId(),film);
            }

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] exploded = line.split("~");
                sessionList.add(new Session(filmMap.get(exploded[1]),exploded[2], exploded[3], exploded[0]));
            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionList;
    }

    @Override
        public ScheduleCinema getScheduleList(List<Session> sessionList) {
        ScheduleCinema schedule = new ScheduleCinema();
        try {
            FileReader fileReader = new FileReader(scheduleFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            Map<String,Session> sessionMap = new HashMap<>();

            for (Session session:sessionList) {
                sessionMap.put(session.getSessionId(),session);
            }

            String date, time, session;
            while ((date = bufferedReader.readLine()) != null) {
                String[] exploded = date.split("~");
                date = exploded[1];

                while(!Objects.equals(time = bufferedReader.readLine(), "endTime"))
                {
                    while(!Objects.equals(session = bufferedReader.readLine(), "end")){
                        Session sessionObject = sessionMap.get(session);
                        boolean isFull = Objects.equals(bufferedReader.readLine(), "true");
                        List<int[]> usedPlaces = new ArrayList<>();
                        String places = bufferedReader.readLine();
                        if(!Objects.equals(places, "empty"))
                        {
                            for (String place:places.split(" ")) {
                                usedPlaces.add(new int[]{Integer.parseInt(place.split(":")[0]), Integer.parseInt(place.split(":")[1])});
                            }
                        }

                        schedule.addScheduleItem(date,time,sessionObject,isFull,usedPlaces);
                    }
                }

            }
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return schedule;
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
            for (int i = 0; i < filmRoom.getFilmRoomHeight(); i++) {
                String chairLine = "";
                for (int j = 0; j < filmRoom.getFilmRoomWidth(); j++) {
                    chairLine +=  Character.toString(chairs[j][i]) + ' ';
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

    @Override
    public boolean saveFilm(List<Film> filmList) {
        try {
            FileWriter fileWriter = new FileWriter(filmFilePath);

            for (Film film : filmList)
            {
                fileWriter.write(film.getFilmToFile() + '\n');
            }

            fileWriter.close();

            return true;
        }
        catch (IOException e) {
            System.err.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean saveSession(List<Session> sessionList) {
        try {
            FileWriter fileWriter = new FileWriter(sessionFilePath);

            for (Session session : sessionList)
            {
                fileWriter.write(session.getSessionId() + "~" + session.getFilm().getFilmId() + "~" +
                        session.getCinema() + "~" + session.getFilmRoom() + '\n');
            }

            fileWriter.close();

            return true;
        }
        catch (IOException e) {
            System.err.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean saveSchedule(ScheduleCinema schedule) {
        try {
            FileWriter fileWriter = new FileWriter(scheduleFilePath);

            for (String date:schedule.getSchedule().keySet()) {
                fileWriter.write("date~" + date + '\n');
                for (String time:schedule.getSchedule().get(date).keySet()) {
                    fileWriter.write(time + '\n');
                    for (Object[] paramSchedule:schedule.getSchedule().get(date).get(time)) {
                        Session session = (Session) paramSchedule[0];
                        boolean isFull = (boolean) paramSchedule[1];
                        fileWriter.write(session.getSessionId() + "\n");
                        fileWriter.write( (isFull)? "true\n": "false\n");
                        List<int[]> places = (List<int[]>)paramSchedule[2];
                        if(!places.isEmpty()){
                            for (int[] place: (List<int[]>)paramSchedule[2]) {
                                fileWriter.write(Integer.toString(place[0]) + ":" + Integer.toString(place[1]) + " ");
                            }
                        }
                        else{
                            fileWriter.write("empty");
                        }

                        fileWriter.write("\n");
                    }
                    fileWriter.write("end\n");
                }
                fileWriter.write("endTime\n");
            }

            fileWriter.close();

            return true;
        }
        catch (IOException e) {
            System.err.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeCinema(Cinema cinema) {
        File file = new File(cinemaFilePath + cinema.getCinemaName() + ".txt");
        return file.delete();
    }

    @Override
    public boolean removeFilmRoom(String cinemaName, FilmRoom filmRoom) {
        File file = new File(filmRoomFilePath + cinemaName + "-"+ filmRoom.getFilmRoomId() +".txt");
        return file.delete();
    }
}
