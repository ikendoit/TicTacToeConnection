import java.io.*;
public class Model implements Serializable{ 
    //Player[] board ; 
    public int[] score ; 
    public Player win ; 
    public Player winner; 
    public Player whosMove; 
    public Player[][] board ; 

    public Model() { 
	score = new int[2] ; 
	board = new Player[3][3]; 
    }

    public Player get(int row, int col){
        return board[row][col]; 
    }

    public void set(int row, int col){ 
        board[row][col] =  whosMove; 
    } 

    public boolean is(int row, int col){
        if (board[row][col] != null){
            return true;
        } 
        return false;
    }

    // check game result
    public void checkGameState(int row, int col) { 
	    checkWin(row,col) ;
	    if (checkDraw()) {
		System.out.println("DRAWN GAME,DRAWN GAME");		
	    }
    }

    //check win 
    private void checkWin(int row, int col) { 
        if (checkWinStraight(row,col) || checkWinDiagonal()) { 
            System.out.println("we have a WINNER"); 
        } else { 
            System.out.println("no winner yet ") ;
        } 
    }

    private boolean checkWinStraight(int row, int col) { 
        boolean gameDone = false;

        Player player = board[row][col]; 
        for (int i = 0 ; i < 3 ; i ++ ) { 
            if (player == board[row][i]) {
               gameDone = true ; 
            } else {
                gameDone = false;
                break;
            } 
        }

        for (int i = 0 ; i < 3 ; i ++) { 
            if (player == board[i][col]) {
                gameDone = true ; 
            } else { 
                gameDone = false ; 
                break ; 
            } 
        }
        return gameDone;
    } 

    private boolean checkWinDiagonal(){ 
        if ((board[0][0]  == board[1][1]   && 
            board[1][1]  == board[2][2]  ) ||
            (board[1][1]  == board[0][2]   &&
            board[1][1]   == board[2][0]  )) {

            System.out.println("won in diagonal");
            return true ;
        }
        return false;
    }

    private boolean checkDraw(){
        for ( int i = 0 ; i < 3 ; i ++) { 
            for ( int j = 0 ; j < 3 ; j++ ) { 
                if (board[i][j] == Player.NONE){ 
                    return false; 
                } 
            }
        }
        return true;
    }


}


