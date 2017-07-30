import java.io.*;

/**
 * Model: holds a version of the game, check validity 
 *   of move 
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */


public class Model {
    //Player[] board ; 
    public Player win ; 
    public Player winner; 
    public Player whosMove; 
    public Player[][] board ; 

    /**
     * construct the model
     */
    public Model() { 
        board = new Player[3][3]; 
    }

    /**
     * get id of player at a position in the cached memory 
     * @params integer: row position
     * @params integer: col position
     * @return player: player at the specified position
     */
    public Player get(DataPackage data){
        return board[data.getX()][data.getY()]; 
    }

    /**
     * set a position with data from a dataPackage
     * @params DataPackge to be used
     */
    public void set(DataPackage data){ 
        board[data.getX()][data.getY()] =  data.getPlayer(); 
    } 

    /**
     * check if a position specified in a dataPackage is taken 
     * @params DataPackge to be used
     * @return boolean : position is available
     */
    public boolean isAvailable(DataPackage data){
        if (board[data.getX()][data.getY()] == null){
            return true;
        } 
        return false;
    }

    /**
     * reset the game
     */
    public void resetGame(){
        board = new Player[3][3];
    }

  //*************** Check Game State*******************
    /**
     * check if a game has a winner 
     * @params integer row position
     * @params integer col position 
     * @return boolean : there is a winner
     */
    public boolean checkWin(int row, int col) { 
        if (checkWinStraight(row,col) || checkWinDiagonal()) { 
            return true;
        } else { 
            System.out.println("no winner yet ") ;
            return false;
        } 
    }

    /**
     * check if a position has won in straight lines
     * @params integer row position
     * @params integer col position 
     * @return boolean: a player won straight
     */
    public boolean checkWinStraight(int row, int col) { 
        boolean gameDoneVert = false;
        boolean gameDoneHor = false;

        Player player = board[row][col]; 
        for (int i = 0 ; i < 3 ; i ++ ) { 
            if (player == board[row][i]) {
               gameDoneVert = true ; 
            } else {
                gameDoneVert = false;
                break;
            } 
        }

        for (int i = 0 ; i < 3 ; i ++) { 
            if (player == board[i][col]) {
                gameDoneHor = true ; 
            } else { 
                gameDoneHor = false ; 
                break ; 
            } 
        }
        if (gameDoneVert ) {
            return gameDoneVert;
        }
        if (gameDoneHor ) { 
            return gameDoneHor;
        }
        return false;
    } 

    /**
     * check if the game has diagonal win
     * return boolean: a player won diagonal
     */
    private boolean checkWinDiagonal(){ 
        if ((board[0][0]  == board[1][1]   && 
            board[1][1]  == board[2][2]    &&
            board[0][0] !=  null         ) ||
            (board[1][1]  == board[0][2]   &&
            board[1][1]   == board[2][0]   &&
            board[1][1] != null            )) {

            System.out.println("won in diagonal");
            return true ;
        }
        return false;
    }

    /**
     * check if the game is drawn 
     * @return boolean: game drawn
     */
    public boolean checkDraw(){
        for ( int i = 0 ; i < 3 ; i ++) { 
            for ( int j = 0 ; j < 3 ; j++ ) { 
                if (board[i][j] == null){ 
                    return false; 
                } 
            }
        }
        return true;
    }


}


