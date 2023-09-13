import java.util.Objects;
import java.util.Scanner;

public class ex3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int c_x, c_y, b_x = 0, b_y = 0, commandStep = 0, counterInstruction = 0;
        String command;
        System.out.println("Введите координаты клада");
        System.out.println("По x:");
        c_x = scanner.nextInt();
        System.out.println("По y:");
        c_y = scanner.nextInt();

        while(true) {
            System.out.println("Введите направление/команда");
            command = scanner.next();
            if (!Objects.equals(command, "стоп")){
                System.out.println("Введите количество шагов");
                commandStep = scanner.nextInt();
            }

            switch (command) {
                case "север" -> b_y += commandStep;
                case "юг" -> b_y -= commandStep;
                case "восток" -> b_x += commandStep;
                case "запад" -> b_x -= commandStep;
                default -> {
                    System.out.println("Нельзя дойти до клада");
                    return;
                }
            }
            counterInstruction++;
            if(c_x == b_x & c_y == b_y){
                System.out.println("Количество инструкций: " + Integer.toString(counterInstruction));
                return;
            }
        }

    }
}
