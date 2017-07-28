import java.io.Serializable;

/**
 * DataPackage: format data to be sent 
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */


public class DataPackage  implements Serializable{
    private int x ; 
    private int y ; 
    private String command ; 
    private Player player ; 

    /** 
     * construct a data package from 
     * position : 2 integers
     * commmand : a string 
     * player : a player
     * @params x : row position 
     * @params y : col position 
     * @params command: string command 
     * @params player : PLayer 
     */
    public DataPackage(int x, int y, String command, Player player){
        this.x = x ;
        this.y = y ; 
        this.command = command ; 
        this.player = player;
    } 

    /**
     * return row position
     */
    public int getX(){
        return x ;
    }
    
    /**
     * return col position
     */
    public int getY(){ 
        return y ; 
    }
    /**
     * return command 
     */
    public String getCommand(){
        return command; 
    } 
    /**
     * return player 
     */
    public Player getPlayer(){
        return player; 
    }

    /** set command for datapackage
     */
    public void setCommand(String s) {
        this.command = s ;
    }
    /**
     * set player for data package
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}


