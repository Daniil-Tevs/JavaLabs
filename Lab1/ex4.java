import java.util.Scanner;

public class ex4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите количество дорог");
        int countRoads = scanner.nextInt(), index = 1, minHeight = Integer.MAX_VALUE, tunnels, height;

        for (int i = 0; i < countRoads; i++) {
            System.out.println("Введите количество туннелей");
            tunnels = scanner.nextInt();
            for(int j = 0; j < tunnels; j++){
                System.out.println("Введите высоту туннеля");
                height = scanner.nextInt();
                if(height < minHeight){
                    index = i+1;
                    minHeight = height;
                }
            }
        }

        System.out.println("Номер туннеля: " + Integer.toString(index) + "\nМинимальная высота туннеля:" + Integer.toString(minHeight));
    }
}
