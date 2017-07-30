import java.io.IOException; 
import java.util.logging.Logger;
import java.io.EOFException;

/**
 * server presenter: connecting GUI, socket and model  
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */


public class ServerPresenter implements Presenter { 

    private static final int DEFAULT_PORT = 9999;
    private static final int DEFAULT_POOL = 10; 
    public GUIView guiView ;
    private ServerSocketView socketView ;
    private Model model ; 

    /**
     * constructor : register guiView and server socket 
     * @params guiView to be passed
     * @params ServerSocketView to be passed
     */
    public ServerPresenter(GUIView guiView , ServerSocketView socketView) { 
	    this.model = new Model() ;
        this.socketView = socketView;
        this.guiView = guiView; 
    }

    /**
     * reset the game
     */
    public void resetGame(){
        model.resetGame();
    }
    
    /**
     * return Player ID of the server
     */
    public Player getPlayer(){
        return Player.X;
    }

    /**
     * make a move depends on data package, then send 
     * that data to client socket
     * @params dataPackage to be used
     * @return boolean: operation successful
     */
    public boolean move(DataPackage data) throws IOException{
        int x = data.getX();
        int y = data.getY();
        if (data.getCommand().equals("RESET") || data.getCommand().equals("RESETSCORE")){
            socketView.forward(data);
            return true;
        }
        if (model.isAvailable(data)  ){ 
            model.set(data);

            if (model.checkWin(x,y)) {
                System.out.println("server presenter-84 current prob");
                data.setCommand(data.getPlayer().getID());
                guiView.addScore(data.getPlayer());
                guiView.setWin();
                guiView.endGame(data.getPlayer());
            } else if (model.checkDraw()) {
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
     * update gui and cache memory with data sent by 
     * client
     * @params DataPackge sent by client
     * @return boolean : operation successful
     */
    public boolean moveFromReceive(DataPackage data) throws IOException { 
        System.out.println("moving from receive - server");
        int x = data.getX();
        int y = data.getY() ;
        if (data.getCommand().equals("RESET")){
            guiView.parseData(data);
            resetGame();
            return true;
        } else if (data.getCommand().equals("RESETSCORE")){
            guiView.parseData(data);
            return true;
        } else if ( model.isAvailable(data) ){ 
            model.set(data);
            guiView.parseData(data);
            return true;
        }
        return false;
    }
    /**
     * start a new game on the server side
     */
    public void newGame() throws Exception{
        guiView.showGame();
        socketView.startRunning();
    }
    
    /**
     * start Server Presenter
     * @params integer port of the server
     */
    public static void presentGame(int port) throws IOException { 
        
        try (ServerSocketView socketView = new ServerSocketView(port);  
             GUIView guiView = new GUIView(Player.X) ){

            ServerPresenter presenter = new ServerPresenter(guiView, socketView);

            guiView.setPresenter(presenter);

            socketView.setPresenter(presenter);
            presenter.newGame();
        } catch (Exception e) { 
            if (e instanceof EOFException){ 
               System.out.println("Client has shutdown");
            }
            e.printStackTrace();
            System.out.println("exception: server presenter - 83");
        }
         
    }
}


