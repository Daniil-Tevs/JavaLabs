import java.util.Scanner;

public class ex7 {
    static Scanner scanner = new Scanner(System.in);

    private static int[] getMaxElementLine(int x, int y,int[][] array){
        int[] result = new int[x];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                result[i] = Math.max(result[i], array[i][j]);
            }
        }
        return result;
    }

    public static void main(String[] args) {

        int x = scanner.nextInt(),y= scanner.nextInt();
        int[][] array = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                array[i][j] = scanner.nextInt();
            }
        }

        int[] result = getMaxElementLine(x,y,array);
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
        }
    }
}
