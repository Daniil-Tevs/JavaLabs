import java.util.Arrays;
import java.util.Scanner;

public class ex3 {
    static Scanner scanner = new Scanner(System.in);
    private static  int[] scanArray(){
        int len;
        System.out.println("Введите длину первого масссива");
        len = scanner.nextInt();
        System.out.println("Введите данные масссива");
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = scanner.nextInt();
        }
        return array;
    }

    public static void main(String[] args) {

        // Data for input
        int[] array = scanArray();

        int index = 0, maxSum = 0 , sum = 0;

        for (int i = 0; i < array.length; i++) {
            sum = array[i];
            if(i == array.length - 1){
                if(sum > maxSum)
                    maxSum = sum;
                break;
            }
            for (int j = i + 1; j < array.length; j++) {
                sum+=array[j];
                if(sum > maxSum)
                    maxSum = sum;
            }
        }

        System.out.println(maxSum);
    }
    // 10 1 2 3 4 -5 10 2 4 9 10 = 40
    // 10 1 2 3 4 -5 10 2 4 -100 50 = 50
    // 10 1 -100 3 70 -5 10 2 4 -100 50 = 84
}
