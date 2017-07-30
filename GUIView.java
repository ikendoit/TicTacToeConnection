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
    private int score[];
    public boolean changed = false;
    public DataPackage data;
    public static ServerPresenter serverPresenter = null; 
    public static ClientPresenter clientPresenter = null;
    public final int BUFFER_ROW = 4;
    public final int BUFFER_COL = 4;
    

    private JButton[][] buttons;
    private JButton reset ;
    private JOptionPane turn ; 
    private int moveCount = 9 ; 
    private boolean gameWon = false;
    private boolean gameDraw = false;
    private JTextArea scoreBoard ; 
    private JButton resetScore;
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
        this.score = new int[2];
        score[0] = 0 ;
        score[1] = 0 ;
        buttons = new JButton[3][3];
        initGUI();
        gameGuide.setText("Host goes first");
        scoreBoard.setText("HOST "+score[0]+" : "+score[1]+" CLIENT");
    } 

    /**
     * init GUI components 
     */
    public void initGUI(){
        //initialize variables and components
        reset = new JButton("Reset Game"); 
        turn = new JOptionPane(); 
        scoreBoard = new JTextArea(); 
        resetScore = new JButton("reset scores "); 
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
        //init reset  button
        resetButton();
        resetScoresButton();
        //init buttons for X O locations
        initButton();
            
        //south east : chat + game guide
        scoreBoard.setBorder(new LineBorder(Color.GRAY));
        gameGuide.setBorder(new LineBorder(Color.GRAY));

        northEast.add(gameGuide);
        northEast.add(scoreBoard);
 
        //North panel: scoreboard + gameGuide
        north.add(northEast);

        //South panel: Start game + reset scores
        south.add(reset); 
        south.add(resetScore);

        //add components to Container
        add(north);
        add(center);
        add(south);
    }

    /**
     * parse the dataPackage passed in GUI
     * parse commands: reset, winner, draw, move
     * @params DataPackage to be parsed
     */
    public void parseData(DataPackage data) throws IOException { 
        if (data.getCommand().equals("RESET")){
            resetGame();
            return ;
        } else if (data.getCommand().equals("RESETSCORE")){
            resetScores();
            return ;
        }
        if (!isChosen(data.getX(),data.getY())){
            setButton(data);
         
            System.out.println(data.getCommand().equals("X"));
            if (data.getCommand().equals("X") || data.getCommand().equals("O")) {
                System.out.println("139 - GUIView");
                setWin();
                System.out.println("ENDING GAME ");
                endGame(data.getPlayer());
                addScore(data.getPlayer());
            }

            if (data.getCommand().equals("DRAW")){
                setDraw();
                endGame(data.getPlayer());
            }
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

                        if ("text" == property && buttons[a][b].getText() != " " ) {
                            if (serverPresenter != null && getCurrentPlayer() == serverPresenter.getPlayer()) { 
                                try { 
                                    serverPresenter.move(dataButton);
                                } catch (IOException e) { 
                                    e.printStackTrace();
                                    System.out.println("exception at 113 - GUI");
                                }
                                
                            } else if (clientPresenter != null && getCurrentPlayer() == clientPresenter.getPlayer()){ 
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

    /**
     * init Reset Button
     */
    public void resetButton(){
        reset.addActionListener( (e) -> { 
            reset.setText("RESETING.....");
            reset.setText("Reset Game");
            
        });
        reset.addPropertyChangeListener(new PropertyChangeListener() { 
           @Override
           public void propertyChange(PropertyChangeEvent event ) { 
               //set data package to send reset request
               DataPackage dataButton = new DataPackage(4,4,"RESET",player);
               gameGuide.setText("Host goes first");
               String property = event.getPropertyName(); 

               if ("text" == property ){
                   if (serverPresenter != null ) { 
                       try { 
                           serverPresenter.move(dataButton);
                           serverPresenter.resetGame();
                           resetGame();
                       } catch (IOException e) { 
                           e.printStackTrace();
                           System.out.println("exception at 113 - GUI");
                       }
                       
                   } else if (clientPresenter != null ){ 
                       try  {
                           clientPresenter.move(dataButton);
                           clientPresenter.resetGame();
                           resetGame();
                       } catch (IOException e){ 
                           e.printStackTrace();
                       }
                   
                   }
               }
           }
       });
    }

    /**
     * init Reset Score Button
     */
    public void resetScoresButton(){
        resetScore.addActionListener( (e) -> { 
            resetScore.setText("RESETING.....");
            resetScore.setText("Reset Score");
            
        });
        resetScore.addPropertyChangeListener(new PropertyChangeListener() { 
           @Override
           public void propertyChange(PropertyChangeEvent event ) { 
               //set data package to send reset request
               DataPackage dataButton = new DataPackage(4,4,"RESETSCORE",player);
               gameGuide.setText("Score Reset");
               String property = event.getPropertyName(); 

               if ("text" == property ){
                   if (serverPresenter != null ) { 
                       try { 
                           System.out.println("reseting in server");
                           serverPresenter.move(dataButton);
                           resetScores();
                       } catch (IOException e) { 
                           e.printStackTrace();
                           System.out.println("exception at 113 - GUI");
                       }
                       
                   } else if (clientPresenter != null ){ 
                       try  {
                           System.out.println("reseting in client");
                           clientPresenter.move(dataButton);
                           resetScores();
                       } catch (IOException e){ 
                           e.printStackTrace();
                       }
                   
                   }
               }
           }
       });
    }

  //********************SET METHODS******************
   
    /**
     * set a button on GUI board using information
     * from a dataPackage
     * @params DataPackage to be used
     */
    public void setButton(DataPackage data) { 
        if (data.getPlayer() == getCurrentPlayer()){
            if (!data.getCommand().equals("") ){
                this.gameGuide.setText(data.getCommand());
            }
            this.buttons[data.getX()][data.getY()].setText(data.getPlayer().getID());

            this.currentPlayer = this.currentPlayer.toggle();
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

    /**
     * set current player
     * @params player : set currentPlayer
     */
    public void setCurrentPlayer(Player player){
        this.currentPlayer = player;
    }

  //********************get + reset + addScore method*****************
    /**
     * return current player
     * @return currentPlayer
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * reset the game board
     */
    public void resetGame(){
        for (int i = 0 ; i < 3 ; i ++) {
            for (int j = 0 ; j < 3 ; j ++) { 
                buttons[i][j].setText(" ");
            }
        }
        this.gameWon = false;
        this.gameDraw = false;
        setCurrentPlayer(Player.X);
    }

    /**
     * reset scores 
     */
    public void resetScores(){
        score[0] = 0 ;
        score[1] = 0 ;
        scoreBoard.setText("HOST "+score[0]+" : "+score[1]+" CLIENT");
    }

    /**
     * add score to a player
     */
    public void addScore(Player player){
        if (player == Player.X){
            score[0]++;
            scoreBoard.setText("HOST "+score[0]+" : "+score[1]+" CLIENT");
        } else { 
            score[1]++;
            scoreBoard.setText("HOST "+score[0]+" : "+score[1]+" CLIENT");
        }
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
            this.gameGuide.setText("Game Over \n Player "+(winner == Player.X ? "Host" : "Client")+" won");
        } else if (gameDraw){ 
            this.gameGuide.setText("Game Draw, No one wins ");
        }
        gameWon = false;
        gameDraw = false;
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
