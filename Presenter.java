import java.io.IOException;
 
/**
 * interface presenter: Presenter concept.
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */


public interface Presenter { 
    public boolean move(DataPackage data) throws IOException; 
    //public void registerPropertyChangeListener();
    public void newGame() throws Exception ;
    public Player getPlayer();
}
