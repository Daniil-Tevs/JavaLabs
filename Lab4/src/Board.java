import Figures.Bishop;
import Figures.Figure;
import Figures.King;
import Figures.Knight;
import Figures.Pawn;
import Figures.Queen;
import Figures.Rook;

import java.util.ArrayList;
import java.util.Objects;

public class Board {
    private Figure fields[][] = new Figure[8][8];
    private ArrayList<String> takeWhite = new ArrayList(16);
    private ArrayList<String> takeBlack = new ArrayList(16);

    public char getColorGaming() {
        return colorGaming;
    }

    public void setColorGaming(char colorGaming) {
        this.colorGaming = colorGaming;
    }

    private char colorGaming;

    private boolean isCheck = false;

    public void init() {
        this.fields[0] = new Figure[]{
                new Rook("R", 'w'), new Knight("N", 'w'),
                new Bishop("B", 'w'), new Queen("Q", 'w'),
                new King("K", 'w'), new Bishop("B", 'w'),
                new Knight("N", 'w'), new Rook("R", 'w')
        };
        this.fields[1] = new Figure[]{
                new Pawn("P", 'w'), new Pawn("P", 'w'),
                new Pawn("P", 'w'), new Pawn("P", 'w'),
                new Pawn("P", 'w'), new Pawn("P", 'w'),
                new Pawn("P", 'w'), new Pawn("P", 'w'),
        };

        this.fields[7] = new Figure[]{
                new Rook("R", 'b'), new Knight("N", 'b'),
                new Bishop("B", 'b'), new Queen("Q", 'b'),
                new King("K", 'b'), new Bishop("B", 'b'),
                new Knight("N", 'b'), new Rook("R", 'b')
        };
        this.fields[6] = new Figure[]{
                new Pawn("P", 'b'), new Pawn("P", 'b'),
                new Pawn("P", 'b'), new Pawn("P", 'b'),
                new Pawn("P", 'b'), new Pawn("P", 'b'),
                new Pawn("P", 'b'), new Pawn("P", 'b'),
        };
    }

    public void initTest() {
        this.fields[3][2] = new King("K", 'w');
        this.fields[5][4] = new Bishop("B", 'w');

        this.fields[4][3] = new Queen("Q", 'w');
        this.fields[2][2] = new Pawn("P", 'w');
        this.fields[3][4] = new King("K", 'b');
    }

    public String getCell(int row, int col) {
        Figure figure = this.fields[row][col];
        if (figure == null) {
            return "    ";
        }
        return " " + figure.getColor() + figure.getName() + " ";
    }

    public ArrayList<String> getTakeWhite() {
        return takeWhite;
    }

    public String getTakeWhiteText() {
        StringBuilder result = new StringBuilder();
        for (String figure:takeWhite) {
            result.append(figure).append(", ");
        }
        return result.toString();
    }

    public ArrayList<String> getTakeBlack() {
        return takeBlack;
    }
    public String getTakeBlackText() {
        StringBuilder result = new StringBuilder();
        for (String figure:takeBlack) {
            result.append(figure).append(", ");
        }
        return result.toString();
    }

    public boolean move_figure(int row1, int col1, int row2, int col2) {

        Figure figure = this.fields[row1][col1];

        if (figure.canMove(row1, col1, row2, col2, fields) && this.fields[row2][col2] == null) {
            this.fields[row2][col2] = figure;
            this.fields[row1][col1] = null;
            if(!isCheck()) {
                return true;
            }
            else {
                this.fields[row1][col1] = figure;
                this.fields[row2][col2] = null;
            }
        } else if (figure.canAttack(row1, col1, row2, col2, fields) && this.fields[row2][col2] != null
                && this.fields[row2][col2].getColor() != this.fields[row1][col1].getColor() && !(this.fields[row2][col2] instanceof King)) {
            Figure oldFigure = this.fields[row2][col2];
            this.fields[row2][col2] = figure;
            this.fields[row1][col1] = null;
            if(!isCheck()) {
                switch (oldFigure.getColor()) {
                    case 'w':
                        this.takeWhite.add(oldFigure.getColor() + oldFigure.getName());
                        break;
                    case 'b':
                        this.takeBlack.add(oldFigure.getColor() + oldFigure.getName());
                        break;
                }
                return true;
            }
            else {
                this.fields[row1][col1] = figure;
                this.fields[row2][col2] = oldFigure;
            }
        }
        return false;
    }

    public void print_board() {
        System.out.println(" +----+----+----+----+----+----+----+----+");
        for (int row = 7; row > -1; row--) {
            System.out.print(row);
            for (int col = 0; col < 8; col++) {
                System.out.print("|" + getCell(row, col));
            }
            System.out.println("|");
            System.out.println(" +----+----+----+----+----+----+----+----+");
        }

        for (int col = 0; col < 8; col++) {
            System.out.print("    " + col);
        }
    }

    private int[] getKing() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (fields[i][j] != null && fields[i][j].getColor() == colorGaming && fields[i][j] instanceof King) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public boolean isCheck() {
        int[] placeKing = this.getKing();
        if (placeKing == null) return false;

        int row = placeKing[0], col = placeKing[1];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (fields[i][j] != null && fields[i][j].getColor() != colorGaming && fields[i][j].canAttack(i, j, row, col, fields)) {
                    isCheck = true;
                    return true;
                }
            }
        }
        isCheck = false;
        return false;
    }

    private boolean isCanAvoidCheck(int row, int col) {
        Figure figureStart = fields[row][col];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Figure figure = fields[i][j];

                if (move_figure(row, col, i, j)) {
                    if (!isCheck()) {
                        fields[i][j] = figure;
                        fields[row][col] = figureStart;
                        return true;
                    }
                    fields[i][j] = figure;
                    fields[row][col] = figureStart;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate() {
        if (!isCheck()) return false;

        Figure[][] fieldsTmp = fields.clone();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (fields[i][j] != null && fields[i][j].getColor() == colorGaming && isCanAvoidCheck(i, j)) {
                    fields = fieldsTmp;
                    return false;
                }
            }
        }
        fields = fieldsTmp;

        return true;
    }

}
