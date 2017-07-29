import java.net.Socket;
import java.net.ServerSocket; 
import java.io.*;
import java.util.logging.Logger;

/**
 * server socket view: connects host with client, transfer data 
 *   between two players.
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */


public class ServerSocketView implements AutoCloseable{ 

    private final int DEFAULT_PORT = 9999;
    private final int DEFAULT_POOL = 10; 
    private static final Logger LOG = Logger.getLogger(ServerSocketView.class.getName());

    private ObjectOutputStream output;
    private ObjectInputStream input ;

    private ServerSocket server;
    private Socket connection ;
    private ServerPresenter presenter;
    private DataPackage data;
    private Player player;
    public boolean received = false;

    /**
     * construct a server socket 
     * @params integer port to be used
     * @params integer pool to be used 
     */
    public ServerSocketView(int port ) throws IOException {
        data = null;
        this.player = Player.X;
        LOG.info("starting server socket view");
        server = new ServerSocket(port, DEFAULT_POOL);
        LOG.info("started server socket view");
    }

    /**
     * set presenter for the server socket
     * @params ServerPresenter to be set
     */
    public void setPresenter(ServerPresenter presenter) { 
        this.presenter = presenter;
    }

    /**
     * start running the server socket
     */
    public void startRunning() { 
        try { 
            establishConnection();
            establishStreams();
            while (data == null || data.getCommand() != "END" ){ 
                receive();
            }
        }  catch (Exception e){
            System.out.println("problem starting server " + e.toString());
            close();
        } 
    }          

    /**
     * close the streams and connection 
     */
    public void close(){
        try { 
            output.close();
            input.close();
            connection.close();
        } catch (Exception e){
            System.out.println("exception at 61 - serversocket ");
            e.printStackTrace();
        }
    }

    /**
     * establish connections with client socket 
     */
    private void establishConnection() throws IOException{ 
        try { 
            connection = server.accept();
            LOG.info("received connection from "+connection.getRemoteSocketAddress() + " Handling on "+connection.getLocalSocketAddress());
        
        } catch (Exception e){
            if (e instanceof EOFException) {
                System.out.println("Disconnected from client -server socket 66");
            }
            System.out.println("problem starting connection "+e.toString());
        } 
        
    }

    /**
     * establish streams data to client 
     */
    private void establishStreams() throws IOException{ 
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        LOG.info("Streams to clients : CHECK");
    }

    /**
     * forward a datapackage to client 
     * @params dataPackage to be sent
     */
    public void forward(DataPackage data) throws IOException{ 
	   try { 
           this.data = data;
	       output.writeObject(data);
           output.flush();
           this.data = null;
           LOG.info("sending package - 96 : CHECK");
           System.out.println("****************************");
	    } catch (Exception e ){
           if (e instanceof EOFException) {
               System.out.println("Disconnected");
           }
	       System.out.println("problem forwarding - server socket - 93: "+e.toString());
	   }
        
    }

    /**
     * receive data package from client and process them
     * with server presenter
     * stall server's moves until client makes one
     */
    public void receive() throws IOException { 
          try { 
               do { 
                   while (data == null ) {
                       data = (DataPackage)input.readObject();
                       System.out.println("loop at 117");
                   }
                   LOG.info("reading package from client : CHECK");
                   System.out.println("check 113- receive of server socket: "+presenter.moveFromReceive(data));
                   System.out.println("******************************************************");
                   data = null;
               } while (data == null || !data.getCommand().equals("END") );
           } catch (IOException e) {
               System.out.println(e.toString()+" 123 - serversocket");
               data.setCommand("END");
               close();
           } catch (ClassNotFoundException cnfException) { 
               System.out.println(cnfException.toString()+ " 126 - serversocket");
               close();
           }
       }
}

