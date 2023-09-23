import java.util.Scanner;

public class ex6 {
    static Scanner scanner = new Scanner(System.in);

    private static int getSumArray2d(int x, int y,int[][] array){
       int sum = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                sum+= array[i][j];
            }
        }
        return sum;
    }

    public static void main(String[] args) {

        int x = scanner.nextInt(),y= scanner.nextInt();
        int[][] array = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                array[i][j] = scanner.nextInt();
            }
        }

        System.out.println(getSumArray2d(x,y,array));
    }
    // 10 1 2 3 4 -5 10 2 4 9 10 = 40
    // 10 1 2 3 4 -5 10 2 4 -100 50 = 50
    // 10 1 -100 3 70 -5 10 2 4 -100 50 = 84
}
