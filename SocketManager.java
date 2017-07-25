import java.io.IOException;
public interface SocketManager{ 
    public void startRunning();
    public void establishConnection() throws IOException; 
    public void establishStreams() throws IOException;
    public void forward(DataPackage data) throws IOException;
    public DataPackage receive() throws IOException;
}
