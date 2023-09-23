import java.util.Scanner;

public class ex1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();

        char[] symbolArray = new char[text.length()];
        char[] symbolIndex = new char[text.length()];
        char[] tmp = new char[text.length()];

        text.getChars(0, text.length(), symbolArray, 0);
        int indexBegin = 0, maxLength = 0, length = 0;
        Boolean isChange;
        for (int i = 1; i < symbolArray.length; i++) {
            isChange = false;

            for (int j = 0; j < symbolIndex.length; j++) {
                if (symbolIndex[j] == symbolArray[i])
                {
                    isChange = true;
                    maxLength = Math.max(maxLength, length);
                    symbolIndex = tmp.clone();
                    length = 0;
                    indexBegin += 1;
                    i = indexBegin;
                    break;
                }
            }

            if(!isChange)
            {
                length++;
                symbolIndex[length] = symbolArray[i];
            }
        }
        System.out.println(maxLength);
    }
}
