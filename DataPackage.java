public class DataPackage {
    private int x ; 
    private int y ; 
    private String command ; 
    private Player playerID ; 

    public DataPackage(int x, int y, String command, Player playerID){
        this.x = x ;
        this.y = y ; 
        this.command = command ; 
        this.playerID = playerID;
    } 

    public int getX(){
        return x ;
    }
    
    public int getY(){ 
        return y ; 
    }
    
    public String getCommand(){
        return command; 
    } 
    
    public Player getPlayer(){
        return playerID; 
    }
}


