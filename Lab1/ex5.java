import java.util.Scanner;

public class ex5 {
    public static boolean checkNumber(int n) {
        int sumDigit = 0, multDigit = 1, digit;
        while(n > 0){
            digit = n % 10;
            sumDigit += digit;
            multDigit *= digit;
            n /= 10;
        }
        if(sumDigit % 2 == 0 & multDigit % 2 == 0){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите число");
        if (checkNumber(scanner.nextInt()))
            System.out.println("Дважды чётное");
        else
            System.out.println("Не дважды чётное");
    }
}
