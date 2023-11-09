package Figures;

public class Bishop extends Figure{
    public Bishop(String name, char color) {
        super(name, color);
    }

    @Override
    public boolean canMove(int row, int col, int row1, int col1, Figure[][] fields) {
        if(super.canMove(row, col, row1, col1, fields) && Math.abs(row - row1) == Math.abs(col - col1)){
            if( row < row1){
                if(col < col1){
                    for(int i = row + 1, j = col + 1 ; i < row1 && j < col1 ; i++, j++ ) {
                        if(fields[i][j] != null)
                            return false;
                    }
                }
                else {
                    for(int i = row + 1, j = col - 1 ; i < row1 && j > col1 ; i++, j-- ) {
                        if(fields[i][j] != null)
                            return false;
                    }
                }
            }
            else {
                if(col < col1){
                    for(int i = row - 1, j = col + 1 ; i > row1 && j < col1 ; i--, j++ ) {
                        if(fields[i][j] != null)
                            return false;
                    }
                }
                else {
                    for(int i = row - 1, j = col - 1 ; i > row1 && j > col1 ; i--, j-- ) {
                        if(fields[i][j] != null)
                            return false;
                    }
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean canAttack(int row, int col, int row1, int col1, Figure[][] fields) {
        return this.canMove(row, col, row1, col1, fields);
    }
}
