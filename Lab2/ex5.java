import java.util.Scanner;

public class ex5 {
    static Scanner scanner = new Scanner(System.in);
    private static  int[] scanArray(){
        int len;
        System.out.println("Введите длину масссива");
        len = scanner.nextInt();
        System.out.println("Введите данные масссива");
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = scanner.nextInt();
        }
        return array;
    }

    private static int[] getCouple(int[] array, int target){
        int[] result = new int[2];
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i]+array[j] == target)
                {
                    result[0] = array[i];
                    result[1] = array[j];
                    return result;
                }

            }
        }
        return null;
    }

    public static void main(String[] args) {

        // Data for input
        int[] array = scanArray();
        System.out.println("Введите заданное число");
        int target = scanner.nextInt();
        int[] result = getCouple(array,target);
        if(result != null)
        {
            System.out.println(result[0] + " " + result[1]);
        }
        else {
            System.out.println("don't find");
        }
    }

}
