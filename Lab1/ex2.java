import java.util.Scanner;

public class ex2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите количество чисел:");
        int n = scanner.nextInt(), result = 0;

        for (int i = 0; i < n; i++) {
            System.out.println("Введите число:");
            if (i % 2 == 0)
                result += scanner.nextInt();
            else
                result -= scanner.nextInt();
        }
        System.out.println("Сумма ряда равна: " + Integer.toString(result));
    }
}
