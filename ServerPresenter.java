import java.io.IOException; 
import java.util.logging.Logger;
import static javax.swing.SwingUtilities.invokeLater;
import java.beans.PropertyChangeListener ;
import java.beans.PropertyChangeEvent ;

public class ServerPresenter implements Presenter { 
    public static final int DEFAULT_PORT = 9999;
    private static final int DEFAULT_POOL = 10; 
    private static View guiView ; 
    private static Model model ; 

    //constructor
    public ServerPresenter(View guiView ) { 
	this.model = new Model() ;
        this.guiView = guiView; 
    }

    public void propertyChange(PropertyChangeEvent event) { 
	DataPackage data = (DataPackage) event.getNewValue() ; 
	guiView.
	socket.setData(data) ; 
    }
       //make a move, check legitimacy, update GUI
    public boolean move(int x,int y, Player player){
        if ( model.is(x,y) )  {

            model.board[x][y]= player; 
            model.checkGameState(x,y);

            try { 
               DataPackage data = new DataPackage(x,y,"",player);
	       guiView.setButton(x,y,player);
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
    // add new game statements
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
            GUIView guiView = new GUIView(Player.X);

            ServerPresenter presenter = new ServerPresenter(guiView,socketView);

            guiView.setPresenter(presenter);

            guiView.showGame();

            socketView.startRunning();
        } catch (IOException e) { 
            e.printStackTrace();
        }
            
        
    }
}


