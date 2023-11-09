package Figures;

public class King extends Figure{
    public King(String name, char color) {
        super(name, color);
    }

    @Override
    public boolean canMove(int row, int col, int row1, int col1, Figure[][] fields) {
        if(super.canMove(row, col, row1, col1, fields)){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (fields[i][j] != null && (fields[i][j] instanceof King) && !(i == row && j == col) && (Math.abs(row - i) <= 1 && Math.abs(col - j) <= 1)) {
                        return false;
                    }
                }
            }

            return (Math.abs(row - row1) == 1 && col == col1) || (row == row1 && Math.abs(col - col1) == 1) || ((Math.abs(row - row1) == 1 && Math.abs(col - col1) == 1));
        }
        return false;
    }
    @Override
    public boolean canAttack(int row, int col, int row1, int col1, Figure[][] fields) {
        return this.canMove(row, col, row1, col1, fields);
    }
}
