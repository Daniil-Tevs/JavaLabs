package Figures;

public class Queen extends Figure{
    public Queen(String name, char color) {
        super(name, color);
    }

    @Override
    public boolean canMove(int row, int col, int row1, int col1, Figure[][] fields) {
        if(col == col1){
            for (int i = row + 1; i < row1; i++) {
                if(fields[i][col] != null)
                    return false;
            }
            for (int i = row - 1; i > row1; i--) {
                if(fields[i][col] != null)
                    return false;
            }
            return true;
        }
        if(row == row1) {
            for (int i = col + 1; i < col1; i++) {
                if(fields[row][i] != null)
                    return false;
            }
            for (int i = col - 1; i > col1; i--) {
                if(fields[row][i] != null)
                    return false;
            }
            return true;
        }

        if((row >=0 && row < 8 ) && (col >=0 && col < 8) && (row1 >=0 && row1 < 8 ) && (col1 >=0 && col1 < 8) && Math.abs(row-row1) == Math.abs(col-col1)){

            if( !(row == 7 && row1 == 0) && !(row == 0 && row1 == 7)){
                if( row < row1 ){
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

        }
        return false;
    }

    @Override
    public boolean canAttack(int row, int col, int row1, int col1, Figure[][] fields) {
        return this.canMove(row, col, row1, col1, fields);
    }
}
