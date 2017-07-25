public class DataPackage {
    private int x ; 
    private int y ; 
    private String command ; 
    private Player player ; 

    public DataPackage(int x, int y, String command, Player player){
        this.x = x ;
        this.y = y ; 
        this.command = command ; 
        this.player = player;
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
        return player; 
    }
}


