import java.util.Arrays;
import java.util.Scanner;

public class ex2 {
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
        int[] array1 = scanArray();
        int[] array2 = scanArray();

        int[] array = new int[array1.length + array2.length];

        int index1 = 0, index2 = 0, length = 0;
        for (int i = 0; i < array.length; i++) {
            if (index1 == array1.length){
                array[i] = array2[index2];
                index2++;
            }
            else if (index2 == array2.length){
                array[i] = array1[index1];
                index1++;
            }
            else if(array1[index1] >= array2[index2])
            {
                array[i] = array2[index2];
                index2++;
            }
            else {
                array[i] = array1[index1];
                index1++;
            }
            length++;
        }
        String result = "";
        for (int i = 0; i < length; i++)
            result += Integer.toString(array[i]) + " ";
        System.out.println("Результат");
        System.out.println(result);
    }

}
