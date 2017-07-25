public enum Player {
    NONE("N"), X("X"),O("O"); 
    private String id ; 

    Player(String id) { 
        this.id = id;
    }

    public Player toggle(){
        if (this.equals(X)) {
            return O; 
        } else {
            return X;
        }
    }

    public String showID(){
        return id;
    }
}
