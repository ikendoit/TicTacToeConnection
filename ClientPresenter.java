import java.io.IOException;
import java.io.EOFException;

/**
 * client presenter: present game for client, check validity of moves
 *   use socket to receive and forward data, check game states, hold a
 *   copy of the game.
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */


//Client presenter - connect Socketclient with GUI
public class ClientPresenter implements Presenter { 
    private static final int DEFAULT_PORT = 9999; 
    private int port;

    private Player[][] board; 
    private Player whosMove;
    private GUIView guiView ; 
    private ClientSocketAdapter socketView; 

    /**
     * constructor: register GUIView and client socket 
     * @params guiView to be passed in as GUIView
     * @params socketView to be passed in as clientSocketAdapter
     */
    public ClientPresenter(GUIView guiView, ClientSocketAdapter socketView) { 
        this.guiView = guiView; 
        this.socketView = socketView; 
        board = new Player[3][3];
    }
  //******************CHECK GAME STATE*****************

     /**
     * checkWin at position of play
     * @params row : row position
     * @params col : col position
     * @return boolean : there is a winner
     */
    private boolean checkWin(int row, int col) { 
        if (checkWinStraight(row,col) || checkWinDiagonal()) { 
            System.out.println("we have a WINNER"); 
            return true;
        } else { 
            return false;
        } 
    }

    /**
     * checkWin in straightlines 
     * @params row position
     * @params col position
     * @return boolean: there is a winner
     */
    private boolean checkWinStraight(int row, int col) { 
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
     * check win in diagonal 
     * @return boolean: there is a winner
     */
    private boolean checkWinDiagonal(){ 
        if (board[1][1] != null            &&
            ((board[0][0]  == board[1][1]   && 
            board[1][1]  == board[2][2]  ) ||
            (board[1][1]  == board[0][2]   &&
            board[1][1]   == board[2][0]  ))) {

            return true ;
        }
        return false;      
    }
 
    /**
     * check if the game is drawn
     * @return boolean: drawn game
     */
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
 
  //******************Client Presenter functions***************
 
    /**
     * return PLayer ID of the client
     * @return player : client
     */
    public Player getPlayer(){
        return Player.O;
    }

    /**
     * reset the game
     */
    public void resetGame(){
        board = new Player[3][3];
    }

    /**
     * make a move from a datapackage when client makes a move
     * then forward data to host
     * @params Datapackage to move and forward
     * @return boolean: operation is successful
     */
    @Override 
    public boolean move(DataPackage data) throws IOException{
        int x = data.getX();
        int y = data.getY();
        if (data.getCommand().equals("RESET") || data.getCommand().equals("RESETSCORE")){
            socketView.forward(data);
            return true;
        }
        if ( board[x][y] == null ) {
            board[x][y]= data.getPlayer(); 

            if (checkWin(x,y)){ 
                System.out.println("173 prob client presenter");
                data.setCommand(data.getPlayer().getID());
                guiView.addScore(data.getPlayer());
                guiView.setWin();
                guiView.endGame(data.getPlayer());
            }else if (checkDraw()){
                data.setCommand("DRAW");
                guiView.setDraw();
                guiView.endGame(data.getPlayer());
            } 
            socketView.forward(data);
            
            return true ; 
        }
        return false;
    }

    /**
     * update the gui when host makes a move 
     * @params DataPackge : sent bu host when they make a move
     * @return boolean operation successful
     */
    public boolean moveFromReceive(DataPackage data) throws IOException { 
        System.out.println("moving from receive - client");
        int x = data.getX() ;
        int y = data.getY() ; 
        if (data.getCommand().equals("RESET") || data.getCommand().equals("RESETSCORE")) { 
            guiView.parseData(data);
            resetGame();
            return true;
        } else if (board[x][y] == null ) { 
            board[x][y] = data.getPlayer();
            guiView.parseData(data); 
            return true;
        }
        return false;
    }

    /**
     * start a new game on the client side
     */
    public void newGame() throws Exception{
        guiView.showGame();
        socketView.startRunning();
    }
    
    /**
     * start Client Presenter
     * @params String host name of server
     * @params integer port of server
     */
    public static void presentGame(String host,int port) throws IOException{

        try ( ClientSocketAdapter socketView = new ClientSocketAdapter(host,port); 
                  GUIView guiView = new GUIView(Player.O)) {

            ClientPresenter presenter = new ClientPresenter(guiView,socketView);
            
            guiView.setPresenter(presenter);

            socketView.setPresenter(presenter);

            presenter.newGame();

        } catch (Exception e) { 
            if (e instanceof EOFException){
                System.out.println("Disconnected");
            }
            e.printStackTrace();
                
        }
		    
    }
}
