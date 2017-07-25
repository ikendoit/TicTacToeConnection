import java.net.Socket;
import java.net.ServerSocket; 
import java.io.*;
import java.util.logging.Logger;

public class ServerSocketView{ 

    private final int DEFAULT_PORT = 9999;
    private final int DEFAULT_POOL = 10; 
    private static final Logger LOG = Logger.getLogger(ServerSocketView.class.getName());
    private ObjectOutputStream output;
    private ObjectInputStream input ;
    private ServerSocket server;
    private Socket connection ;
    private DataPackage data;
    private Presenter presenter;

    public ServerSocketView(int port , Presenter presenter) throws IOException {
        data = new DataPackage(-1,-1,"",Player.X);
        this.presenter = presenter;
        LOG.info("starting server socket view");
        server = new ServerSocket(DEFAULT_PORT, DEFAULT_POOL);
        LOG.info("started server socket view");
        }

    public void startRunning() { 
        try { 
            while (true){
                establishConnection();
                establishStreams();
                forward(data);  //add data
                receive();
            }
        }  catch (IOException e){
            e.printStackTrace();
        }
    }          


    public void establishConnection() throws IOException{ 
        try { 
                connection = server.accept();
                LOG.info("received connection from "+connection.getRemoteSocketAddress() + " Handling on "+connection.getLocalSocketAddress());
            
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            LOG.info("shutting down");
        }
    }

    public void establishStreams() throws IOException{ 
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
    }

    public void forward(DataPackage data) throws IOException{ 
	   try { 
	       output.writeObject(data);
	       output.flush();
	       // process data
	       LOG.info("sending package");
	   } catch (Exception e ){
	       e.printStackTrace();
	   }
        
    }

    public DataPackage receive() throws IOException { 
        do { 
            try { 
                data = (DataPackage)input.readObject();
                presenter.move(data);
                LOG.info("reading package");
                //process data
                return data;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!data.getCommand().equals("END"));
        return null;
    }
}

