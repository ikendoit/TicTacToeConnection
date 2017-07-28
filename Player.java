
/**
 * Player enum: define two types of player.
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */


public enum Player {
    NONE("N"), X("X"),O("O"); 
    private String id ; 

    /**
     * init enum
     */
    Player(String id) { 
        this.id = id;
    }

    /**
     * get next player with respect to current player
     */
    public Player toggle(){
        if (this.equals(X)) {
            return O; 
        } else {
            return X;
        }
    }

    /**
     * show ID of player
     */
    public String getID(){
        return id;
    }
}
