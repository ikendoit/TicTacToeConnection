import java.io.IOException;
public class TicTacToe { 
    public static void main(String[] args) {
        try { 
            if (args.length >= 2) {
                String host = args[0];
                int port = Integer.parseInt(args[1]);
                ClientPresenter.presentGame(host,port);
            } else if (args.length ==1) {
                int port = Integer.parseInt(args[0]);
                ServerPresenter.presentGame(port);
            } else {
                System.out.println("Invalid input ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
            
    }
}
