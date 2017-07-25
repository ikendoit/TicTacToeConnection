import javax.swing.JFrame ; 
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Show GUIView of game, depending on Player role - Host or client
 */
public class GUIView extends JFrame implements View{ 

    private static Player player;
    private Presenter presenter;
    public ViewGUI view ; 

    //constructor
    public GUIView(Player player) { 
        this.player= player;
	this.view = new ViewGUI(player) ;

        setContentPane(view) ;  
    } 

    public void setButton(DataPackage data) { 
	this.view.setButton(data); 
    }

    public void setWin() { 
	this.view.setWin(); 
    }
    
    public void setDraw() {
	this.view.setDraw();
    }

    //set presenter
//    public void setPresenter(Presenter presenter) { 
//        this.presenter = presenter; 
//    }

    //implements interface View
    public Player who() {
        return this.player;
    }

    //show game GUI
    public void showGame(){ 
        JFrame f = new GUIView(player) ; 
        f.setSize(450,600); 
        f.setTitle("TIC TAC TOE ONLINE");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setVisible(true); 
    } 

    /**
     * game Component, create and manage GUI board
     */
    class ViewGUI extends JComponent { 
        private final JButton[][] buttons;
        private JButton reset ;
        private JOptionPane turn ; 
        private int moveCount = 9 ; 
        private boolean gameWon = false;
        private boolean gameDraw = false;
        private JTextArea scoreBoard ; 
        private JButton startGame; 
        private JButton resetScores;
        private JTextArea gameGuide; 
        private Player player;

        public ViewGUI(Player player) { 
            //instances
            this.player = player;
            buttons = new JButton[3][3];
            reset = new JButton("Reset"); 
            turn = new JOptionPane(); 
            player = Player.X;
            scoreBoard = new JTextArea(); 
            startGame = new JButton(); 
            resetScores = new JButton(); 
            gameGuide = new JTextArea(); 

            //set layout JComponent
            setLayout(new GridLayout(3,1));

            //set JComponents
            JPanel north = new JPanel();
            JPanel center = new JPanel(); 
            JPanel south = new JPanel();
            JPanel northEast = new JPanel();
            
            center.setLayout(new GridLayout(3,3)); 
            center.setBorder(new LineBorder(Color.LIGHT_GRAY));

            north.setLayout(new GridLayout(1,1)); 
            north.setBorder(new LineBorder(Color.LIGHT_GRAY));

            south.setLayout(new GridLayout(1,2));

            northEast.setLayout(new GridLayout(2,1));

            //Buttons constructor - CENTER
            for (int i = 0 ; i < 3 ; i ++) {
                for (int j = 0 ; j < 3 ; j ++) { 
                    buttons[i][j] = new JButton();
                    center.add(buttons[i][j]);
                }
            }

            //add X O symmbol in buttons
            for (int i = 0 ; i < 3 ; i ++) {
                for (int j = 0 ; j < 3 ; j ++) { 
                    int a = i ; 
                    int b = j ; 
                    Player playerThis = player; 
                    buttons[a][b].addActionListener(e -> {

                         if ( isChosen(a,b) ) { 
                            setButton(new DataPackage(a,b,"",playerThis);
                            gameGuide.setText("the move is good");
                        } else { 
                            gameGuide.setText("invalid move, try again, please");
                        }
                        this.moveCount--;
                    });
                }
            }

            //South east : chat + game guide
            scoreBoard.setBorder(new LineBorder(Color.GRAY));
            gameGuide.setBorder(new LineBorder(Color.GRAY));

            northEast.add(gameGuide);
            northEast.add(scoreBoard);
     
            //North panel: scoreboard + gameGuide

            north.add(northEast);

           //South panel: Start game + reset scores
            south.add(startGame); 
            south.add(resetScores);

            //add components to Container
            add(north);
            add(center);
            add(south);

        } 

	//check board if filled ? 
	public boolean isChosen(int x, int y) { 
	    if ( board[x][y] != null) { 
	        return true ; 
	    } 
	    return false; 
	}

        //set the button from DataPackage
        public void setButton(DataPackage data) { 
            this.gameGuide.setText("Turn of "+player.toggle());
            this.buttons[data.getX()][data.getY()].setText(data.getPlayer().showID());
        }

        //set game Won
        public void setWin(){
            this.gameWon = true ; 
        } 

        //set game Drawn
        public void setDraw() {
            this.gameDraw = true ;
        }

        //end the game
        public void endGame(Player winner){ 
            if (gameWon) { 
                this.gameGuide.setText("Game Over \n Player "+winner+" won");
            } else if (gameDraw){ 
                this.gameGuide.setText("Game Draw, No one wins ");
            }
        }

    }

}
