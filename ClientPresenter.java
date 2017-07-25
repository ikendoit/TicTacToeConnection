import java.io.IOException;
import static javax.swing.SwingUtilities.invokeLater;

public class ClientPresenter implements Presenter { 
	private static final int DEFAULT_PORT = 9999; 
	private int port;

    private Player[][] board; 
    private Player whosMove;
    private Player win; 
    private Player winner ; 
    private int[] score;
    private static View guiView ; 
    private static ClientSocketAdapter socketView; 

    public ClientPresenter(View guiView, ClientSocketAdapter socketView) { 
        this.guiView = guiView; 
        this.socketView = socketView; 
        board = new Player[3][3];
        score = new int[2];

    }

    @Override
    public boolean move(int x,int y, Player player){
        if ( board[x][y] == null) {

            board[x][y]= player; 
            checkGameState(x,y);
            try { 
                socketView.forward(new DataPackage(x,y,"",player));
            } catch ( IOException e) { 
                e.printStackTrace();
            }
            return true ; 

        }
        return false;
    }
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
                if (board[i][j] == null){ 
                    return false; 
                } 
            }
        }
        return true;
    }
 
    public void registerPropertyChangeListener(){
    } 
    
    public void newGame(){
       
     }
    
    // check game results;
    public void checkGameState(int row, int col) { 
        checkWin(row , col);
        if ( checkDraw()) { 
            System.out.println("DRAWN GAME, DRAWN GAME");
        }
    }


 	public static void main(String[] array) throws IOException{
//		if (array.length >= 1){
//			new ClientPresenter(Integer.parseInt(array[0])).newGame();
//		} //note: implement constructor later  

        int port = DEFAULT_PORT; 

        try { 
            ClientSocketAdapter socketView = new ClientSocketAdapter("localhost",port); 
            socketView.startRunning();
        } catch (IOException e) { 
            e.printStackTrace();
        }
            
        GUIView guiView = new GUIView(Player.O);

        ClientPresenter presenter = new ClientPresenter(guiView,socketView);

        guiView.setPresenter(presenter);

        
        guiView.showGame();
	}
}
