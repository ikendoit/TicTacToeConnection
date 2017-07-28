import java.io.*;
import java.net.Socket;

/**
 * client socket adapter: connects to host, transfer data
 *   between two players.
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */

public class ClientSocketAdapter implements AutoCloseable{ 

    private ObjectOutputStream output;
    private ObjectInputStream input; 

    private DataPackage data; 
    private String host ;
    private int port ; 
    private Socket connection ; 
    private ClientPresenter presenter ; 
    

    /**
     * construct client socket from host and port 
     * @params string host to be used
     * @params int port to be used
     */
    public ClientSocketAdapter(String host , int port) throws IOException{
       data = null;
       this.host = host ; 
       this.port = port ;
    }

    /**
     * set presenter for client socket
     * @params clientpresenter to be set
     */
    public void setPresenter(ClientPresenter presenter){ 
        this.presenter = presenter;
    }

    /**
     * start running client socket 
     */
    public void startRunning() { 
        try { 
            establishConnection();
            establishStreams();
            while (data == null || data.getCommand() != "END"){
                receive();
            }
            
        } catch (IOException e) { 
            System.out.println("problem starting connection "+e.toString());
            close();
        } 
    }

    /**
     * close connection to host 
     * close all streams with host
     */
    public void close(){
        try { 
            output.close();
            input.close();
            connection.close();
        } catch (Exception e){
            System.out.println("problem closing");
        }
    }

    /**
     * establish connection with host socket
     */
    private void establishConnection() throws IOException{ 
        try { 
            connection = new Socket(host,port);
            System.out.println("connected to host : CHECK");
        } catch (IOException e){ 
            System.out.println("No connection");
        }
    }

    /**
     * establish streams to host 
     */
    private void establishStreams() throws IOException{ 
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Stream established : CHECK");
    }

    /**
     * forward data to host 
     * @params dataPackage to be forwarded
     */
    public void forward(DataPackage data) throws IOException { 
        try { 
           //process data for client
           output.writeObject(data);
           output.flush();
           System.out.println("data sent to host : CHECK");
           System.out.println("********************************");
           data = null;
        } catch (Exception e ){ 
            System.out.println("problem forwarding - 65- client socket "+e.toString());
            close();
        }
    }

    /**
     * receive dataPackages from host and process them
     */
    public void receive() throws IOException { 
        try {
            do { 
                while (data == null ) { 
                    data = (DataPackage)input.readObject();
                } 
                //process data for client
                System.out.println("received from host : CHECK");
                System.out.println("make a move from data of server - 81: "+ presenter.moveFromReceive(data));
                System.out.println("****************************************");
                data = null;
            } while (data == null || !data.getCommand().equals("END"));
        } catch (IOException e) { 
            System.out.println(e.toString());
            data.setCommand("END");
            close();
        } catch (ClassNotFoundException cnfException) { 
            System.out.println(cnfException.toString());
            close();
        }
    }
}
