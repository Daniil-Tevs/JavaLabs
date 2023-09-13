import java.util.Scanner;

public class ex1 {
    private static int getStepPosled(int n) {
        int steps = 0;
        while (n > 1) {
            if (n % 2 == 0) {
                n /= 2;
            } else {
                n = 3 * n + 1;
            }

            steps++;
        }
        return steps;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число для вычисления шагов сиракузской последовательность:");
        int n = scanner.nextInt();
        System.out.println("Потребуется " + Integer.toString(getStepPosled(n)) + " шагов");
    }
}
