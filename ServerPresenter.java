import java.io.IOException; 
import java.util.logging.Logger;
import static javax.swing.SwingUtilities.invokeLater;

public class ServerPresenter implements Presenter { 
    public static final int DEFAULT_PORT = 9999;
    private static final int DEFAULT_POOL = 10; 

    private Player[][] board; 
    private Player whosMove;
    private Player win; 
    private Player winner ; 
    private int[] score;
    private static View guiView ; 
    private static ServerSocketView socketView; 

    public ServerPresenter(View guiView, ServerSocketView socketView) { 
        this.guiView = guiView; 
        this.socketView = socketView; 
        board = new Player[3][3];
        for ( int i = 0 ; i < 3 ; i ++ ) { 
            for ( int j = 0 ; j < 3 ; j ++) { 
                board[i][j] =  Player.NONE; 
            }
        }
        score = new int[2];
    }

   
    // check game results;
    public void checkGameState(int row, int col) { 
        checkWin(row , col);
        if ( checkDraw()) { 
            System.out.println("DRAWN GAME, DRAWN GAME");
        }
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
                if (board[i][j] == Player.NONE){ 
                    return false; 
                } 
            }
        }
        return true;
    }

    public boolean move(int x,int y, Player player){
        if ( board[x][y] == Player.NONE) {

            board[x][y]= player; 
            checkGameState(x,y);
            try { 
                socketView.forward(new DataPackage(x,y,"",player));
            } catch (IOException e){ 
                e.printStackTrace();
            }
            return true ; 

        }
        return false;
    }
    
    public void registerPropertyChangeListener(){
    } 

    public void newGame(){
    }
 
    public static void main(final String[] array) throws IOException { 
        int port = DEFAULT_PORT;
        int pool = DEFAULT_POOL;
//        if (array.length >=1) {
//            port = Integer.parseInt(array[0]);
//        }
//        if (array.length >=2) { 
//            pool = Integer.parseInt(array[1]);
//            //note: implement with pool
//        }

        try { 
            ServerSocketView socketView = new ServerSocketView(port);
        } catch (IOException e) { 
            e.printStackTrace();
        }
            
        GUIView guiView = new GUIView(Player.X);

        ServerPresenter presenter = new ServerPresenter(guiView,socketView);

        guiView.setPresenter(presenter);

        socketView.startRunning();
        guiView.showGame();


    }
}


