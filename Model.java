import java.io.*;
public abstract class Model implements Serializable{ 
    //Player[] board ; 
    int[] score ; 
    Player win ; 
    Player winner; 
    Player whosMove; 
    Player[][] board ; 

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

} 

