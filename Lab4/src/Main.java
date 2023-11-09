import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static void printInstructions(){
        System.out.println("Команды: ");
        System.out.println("----- start: Начать новую игру");
        System.out.println("----- help:  Показать команды");
        System.out.println("----- exit:  Выход из игры");
        System.out.println("----- y1 x1 y2 x2: Ход фигуры из клетки x1, y1 в клекту x2, y2\n");
    }

    private static Board startNewGame(){
        Board board = new Board();
        board.setColorGaming('w');
        board.init();
        return board;
    }

    public static void main(String[] args) {
        System.out.println("**** Chess v0.1 ****\n");
        printInstructions();
        Board board = startNewGame();
        Scanner in = new Scanner(System.in);

        while (true) {
            board.print_board();
            System.out.println("\n");
            if(board.isCheckmate()){
                System.out.println("Мат! " + ((board.getColorGaming() == 'w')? "Чёрные " : "Белые ") + "выиграли\n");
                return;
            }
            else if(board.isCheck()){
                System.out.println("Шах " + ((board.getColorGaming() == 'w')? "белым!" : "чёрным!") + "\n");
            }

            System.out.println("Взятые Белые:"+board.getTakeWhite());
            System.out.println("Взятые Черные:"+board.getTakeBlack());

            switch (board.getColorGaming()){
                case 'w': System.out.println("Ход Белых:");break;
                case 'b': System.out.println("Ход Черных:");break;
            }

            String inputLine = in.nextLine();

            if(Objects.equals(inputLine, "start")){
                board = startNewGame();
                continue;
            }
            else if (inputLine.equals("exit")){
                System.out.println("Игра завершена.");
                in.close();
                return;
            }
            else if (inputLine.equals("help")){
                printInstructions();
                continue;
            }

            String[] coords;
            int x1, y1, x2, y2;
            while (true) {
                try{
                    coords = inputLine.split(" ");
                    y1 = Integer.parseInt(coords[0]);
                    x1 = Integer.parseInt(coords[1]);
                    y2 = Integer.parseInt(coords[2]);
                    x2 = Integer.parseInt(coords[3]);
                    if(!board.move_figure(y1,x1, y2,x2)){
                        System.out.println("Ошибка хода, повторите ввод хода!");
                        inputLine = in.nextLine();
                    }
                    else
                    {
                        break;
                    }
                }
                catch (Exception e){
                    System.out.println("Ошибка хода, повторите ввод хода!");
                    inputLine = in.nextLine();
                }
            }

            switch (board.getColorGaming()){
                case 'w': board.setColorGaming('b'); break;
                case 'b': board.setColorGaming('w'); break;
            }
        }
    }
}