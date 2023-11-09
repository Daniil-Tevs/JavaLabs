import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.setColorGaming('w');
        board.init();
//        board.initTest();

        boolean game = true;

        Scanner in = new Scanner(System.in);

        while (game) {
            board.print_board();
            System.out.println();
            if(board.isCheckmate()){
                System.out.println("\nМат! " + ((board.getColorGaming() == 'w')? "Чёрные " : "Белые ") + "выиграли\n");
                return;
            }
            else if(board.isCheck()){
                System.out.println("\nШах " + ((board.getColorGaming() == 'w')? "белым!" : "чёрным!") + "\n");
            }
            System.out.println("Команды: ");
            System.out.println("----- exit: Выход из игры");
            System.out.println("------y1 x1 y2 x2: Ход фигуры из клетки x1, y1 в клекту x2, y2");


            System.out.println("Взятые Белые:"+board.getTakeWhite());
            System.out.println("Взятые Черные:"+board.getTakeBlack());

            switch (board.getColorGaming()){
                case 'w': System.out.println("Ход Белых:");break;
                case 'b': System.out.println("Ход Черных:");break;
            }

            String[] coords;
            String inputLine;
            int x1, y1, x2, y2;
            while (true) {
                inputLine = in.nextLine();

                if (inputLine.equals("exit")){
                    System.out.println("Игра завершена.");
                    in.close();
                    return;
                }

                coords = inputLine.split(" ");
                y1 = Integer.parseInt(coords[0]);
                x1 = Integer.parseInt(coords[1]);
                y2 = Integer.parseInt(coords[2]);
                x2 = Integer.parseInt(coords[3]);
                if(!board.move_figure(y1,x1, y2,x2))
                    System.out.println("Ошибка хода, повторите ввод хода!");
                else
                {
                    break;
                }
            }

            switch (board.getColorGaming()){
                case 'w': board.setColorGaming('b'); break;
                case 'b': board.setColorGaming('w'); break;
            }
        }

    }
}