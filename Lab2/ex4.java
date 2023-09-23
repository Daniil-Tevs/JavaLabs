import java.util.Arrays;
import java.util.Scanner;

public class ex4 {
    static Scanner scanner = new Scanner(System.in);

    private static int[][] rotateArray2DLeft(int x, int y,int[][] array){
        int[][] arrayRotated = new int[y][x];

        for (int i = 0, i2 = 0; i < y ; i++, i2++) {
            for (int j = x - 1, j2 = 0; j >= 0; j--, j2++) {
                arrayRotated[i2][j2] = array[j][i];
            }
        }
        return arrayRotated;
    }

    public static void main(String[] args) {

        int x = scanner.nextInt(),y= scanner.nextInt();
        int[][] array = new int[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                array[i][j] = scanner.nextInt();
            }
        }

        int[][] arrayRotated = rotateArray2DLeft(x,y,array);

        String result = "";
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                result += arrayRotated[i][j] + " ";
            }
            result += "\n";
        }
        System.out.println(result);
    }
}
