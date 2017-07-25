import java.io.*;
import java.net.Socket;
public class ClientSocketAdapter { 

    private ObjectOutputStream output;
    private ObjectInputStream input; 
    private DataPackage data; 
    private String host ;
    private int port ; 
    private Socket connection ; 

    public ClientSocketAdapter(String host , int port) throws IOException{
       data = new DataPackage(-1,-1,"",Player.O);
       this.host = host ; 
       this.port = port ;
    }

    public void startRunning() { 
        try { 
            establishConnection();
            establishStreams();
//            while (!data.getCommand().equals("END")){
//                forward(data); // add data
//                receive();
//            }
        } catch (IOException e) { 
            e.printStackTrace();
        } finally {
        }
    }

    private void establishConnection() throws IOException{ 
        try { 
            connection = new Socket(host,port);
        } catch (IOException e){ 
            e.printStackTrace();
        }
    }

    private void establishStreams() throws IOException{ 
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
    }

    public void forward(DataPackage data) throws IOException { 
        do { 
            try { 
               //process data for client
               output.writeObject(data);
               output.flush();
            } catch (Exception e ){ 
                e.printStackTrace();
            }
        } while (!data.getCommand().equals("END"));
    }

    public DataPackage receive() throws IOException { 
        do { 
            try {
                data = (DataPackage)input.readObject();
                //process data for client
                return data;
            } catch (IOException e) { 
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        } while (!data.getCommand().equals("END"));
        return null;
    }
}
