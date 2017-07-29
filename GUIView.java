import javax.swing.JFrame ; 
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

/**
 * Show GUIView of game, depending on Player role - Host or client
 * @author Trung Kien Nguyen
 * @id 100284963
 * @professor Jeremy Hilliker 
 * @course CPSC 1181 - 002
 * @school Langara Colllege
 * @date 27th July, 2017 
 * @version 1.0
 */

public class GUIView extends JFrame implements View, AutoCloseable{ 

    private Player currentPlayer;
    private Player player;
    public boolean changed = false;
    public DataPackage data;
    public static ServerPresenter serverPresenter = null; 
    public static ClientPresenter clientPresenter = null;
    

    private JButton[][] buttons;
    private JButton reset ;
    private JOptionPane turn ; 
    private int moveCount = 9 ; 
    private boolean gameWon = false;
    private boolean gameDraw = false;
    private JTextArea scoreBoard ; 
    private JButton resetScores;
    public JTextArea gameGuide; 
    private JPanel north ;
    private JPanel center ;
    private JPanel south ;    
    private JPanel northEast ;
    /**
     * construct the GUIView depending on player
     * initiate GUI
     */
    public GUIView(Player player){ 
        this.player= player;
        this.currentPlayer = Player.X;
        initGUI();
        gameGuide.setText("Host goes first");
    } 

    /**
     * init GUI components 
     */
    public void initGUI(){
        //initialize variables and components
        reset = new JButton("Reset Game"); 
        buttons = new JButton[3][3];
        turn = new JOptionPane(); 
        scoreBoard = new JTextArea(); 
        resetScores = new JButton("reset scores (under construction)"); 
        gameGuide = new JTextArea("WELCOME TO ONLINE TICTACTOE"); 

        //set layout JComponent
        setLayout(new GridLayout(3,1));

        //set JComponents
        north = new JPanel();
        center = new JPanel(); 
        south = new JPanel();
        northEast = new JPanel();
        
        center.setLayout(new GridLayout(3,3)); 

        center.setBorder(new LineBorder(Color.LIGHT_GRAY));

        north.setLayout(new GridLayout(1,1)); 

        north.setBorder(new LineBorder(Color.LIGHT_GRAY));

        south.setLayout(new GridLayout(1,2));

        northEast.setLayout(new GridLayout(2,1));

        //Buttons constructor - CENTER
        //init buttons for X O locations
        initButton();
            
        //init reset  button
//        resetBoard();

        //South east : chat + game guide
        scoreBoard.setBorder(new LineBorder(Color.GRAY));
        gameGuide.setBorder(new LineBorder(Color.GRAY));

        northEast.add(gameGuide);
        northEast.add(scoreBoard);
 
        //North panel: scoreboard + gameGuide

        north.add(northEast);

        //South panel: Start game + reset scores
        south.add(reset); 
        south.add(resetScores);

        //add components to Container
        add(north);
        add(center);
        add(south);
    }

    /**
     * parse the dataPackage passed in GUI
     * @params DataPackage to be parsed
     */
    public void parseData(DataPackage data) throws IOException { 
        if (data.getCommand() == "RESET"){
//            resetBoard();
        }
        if (!isChosen(data.getX(),data.getY())){
            setButton(data);
            this.currentPlayer = this.currentPlayer.toggle();
        } 
        if (data.getCommand() == "X" || data.getCommand() =="O") {
            setWin();
            endGame(data.getPlayer());
            this.currentPlayer = this.currentPlayer.toggle();
        }
        if (data.getCommand() == "DRAW"){
            setDraw();
            endGame(data.getPlayer());
            this.currentPlayer = this.currentPlayer.toggle();
        }
    } 

    
   /**
     * check if a specific position is taken in GUI board
     * @params x : row position
     * @params y : col position 
     * @return boolean : the position is chosen
     */
	public boolean isChosen(int x, int y) { 
		if ( buttons[x][y].getText() == " ") { 
			return false ; 
		} 
		return true; 
    }

  //********************INIT BUTTONS *************************

    /**
     * initButton for X O locations
     */
    public void initButton(){
        for (int i = 0 ; i < 3 ; i ++) {
            for (int j = 0 ; j < 3 ; j ++) { 
                buttons[i][j] = new JButton(" ");
                center.add(buttons[i][j]);
            }
        }

        //add X O symmbol in buttons
        for (int i = 0 ; i < 3 ; i ++) {
            for (int j = 0 ; j < 3 ; j ++) { 
                int a = i ; 
                int b = j ; 
                String command = player+" made a move, it is " + (player != Player.X ? "server's" : "client's") + " turn ";
                DataPackage dataButton = new DataPackage(i,j,command,player);

                buttons[a][b].addActionListener( (e) -> { 
                    if (buttons[a][b].getText() ==" "){
                        
                        setButton(dataButton);
                    }
                });
                buttons[a][b].addPropertyChangeListener(new PropertyChangeListener() { 
                    @Override
                    public void propertyChange(PropertyChangeEvent event ) { 
                        String property = event.getPropertyName(); 
                        if (serverPresenter != null ) { 
                            if ("text" == property ) {
                                try { 
                                    serverPresenter.move(dataButton);
                                } catch (IOException e) { 
                                    e.printStackTrace();
                                    System.out.println("exception at 113 - GUI");
                                }
                            }
                        } else if (clientPresenter != null ){ 
                            if ("text" == property ){
                                try  {
                                    clientPresenter.move(dataButton);
                                } catch (IOException e){ 
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }
    } 

//    /**
//     * reset the game board
//     */
//    public void resetBoard(){
//        
//        String command = "RESET";
//        DataPackage dataButton = new DataPackage(0,0,command,player);
//
//        reset.addActionListener( (e) -> { 
//            reset.setText("RESETING.....");
//            System.out.println("reseting the game");
//            reset.setText("Reset Game");
//            
//        });
//
//       reset.addPropertyChangeListener(new PropertyChangeListener() { 
//           @Override
//           public void propertyChange(PropertyChangeEvent event ) { 
//               for (int i = 0 ; i < 3 ; i ++) {
//                   for (int j = 0 ; j < 3 ; j ++) { 
//                       buttons[i][j].setText(" ");
//                   }
//               }
//               gameGuide.setText("game has been reset");
//
//               String property = event.getPropertyName(); 
//               if (serverPresenter != null ) { 
//                   if ("text" == property ) {
//                       try { 
//                           System.out.println("reseting in server");
//                           serverPresenter.resetGame();
//                           serverPresenter.move(dataButton);
//                       } catch (IOException e) { 
//                           e.printStackTrace();
//                           System.out.println("exception at 113 - GUI");
//                       }
//                   }
//               } else if (clientPresenter != null ){ 
//                   if ("text" == property ){
//                       try  {
//                           System.out.println("reseting in client");
//                           clientPresenter.resetGame();
//                           clientPresenter.move(dataButton);
//                       } catch (IOException e){ 
//                           e.printStackTrace();
//                       }
//                   }
//               }
//           }
//       });
//
//    }
  //********************SET METHODS******************
   
    /**
     * set a button on GUI board using information
     * from a dataPackage
     * @params DataPackage to be used
     */
    public void setButton(DataPackage data) { 
        if (data.getPlayer() == getCurrentPlayer()){
            this.gameGuide.setText("Turn of "+player.toggle());
            if (data.getCommand() != ""){
                this.gameGuide.setText(data.getCommand());
            }
            this.buttons[data.getX()][data.getY()].setText(data.getPlayer().getID());
        }
    }
 
    /**
     * set player for GUI
     */
    public void who(Player player) {
       this.player = player;
    }

    /**
     * set server presenter for GUI
     * @params ServerPresenter to be set
     */
    public void setPresenter(ServerPresenter presenter){ 
        this.serverPresenter = presenter; 
    }

    /**
     * set clientPresenter for GUI
     * @params clientPresenter to be set
     */
    public void setPresenter(ClientPresenter presenter){ 
        this.clientPresenter = presenter;
    }

    /**
     * return current player
     * @return currentPlayer
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }


    /**
     * set game to state: Win
     */
    public void setWin(){
        this.gameWon = true ; 
    } 

    /**
     * set game to state : DRAW
     */
    public void setDraw() {
        this.gameDraw = true ;
    }

  //******************CLOSE-ENDGAME-SHOWGAME*****************
  
    /** 
     * close when connection is disconnected
     */
    public void close() throws Exception { 
        this.gameGuide.setText("connection discontinued");
    }
 
    /**
     * end the game, showing the winner or draw
     * @params Player winner 
     */
    public void endGame(Player winner){ 
        if (gameWon) { 
            gameGuide.setText("Game Over \n Player "+(winner == Player.X ? "Host" : "Client")+" won");
        } else if (gameDraw){ 
            gameGuide.setText("Game Draw, No one wins ");
        }
    }

    /**
     * show game GUI in a jframe
     */
    public void showGame(){
        setSize(450,600);
        setTitle("TIC TAC TOE ONLINE"); 
        setResizable(false); 
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setVisible(true); 
    } 

}
