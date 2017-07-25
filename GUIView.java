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
    public GUIView(Player player) { 

        this.player= player;
        setContentPane(new ViewGUI(player, presenter) ) ; 

    } 

    public void setPresenter(Presenter presenter) { 
        this.presenter = presenter; 
    }

    public Player who() {
        return this.player;
    }

    public void showGame(){ 
        JFrame f = new GUIView(player) ; 
        f.setSize(450,600); 
        f.setTitle("TIC TAC TOE ONLINE");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setVisible(true); 
    } 

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
        private Presenter presenter;

        public ViewGUI(Player player, Presenter presenter) { 
            //instances
            this.presenter = presenter;
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

                        if (presenter.move(a,b,playerThis)) { 
                            System.out.println("****************************"+playerThis.showID());
                            buttons[a][b].setText(playerThis.showID());
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

        public void updateGame(int row, int col, Player player) { 
            this.gameGuide.setText("Turn of "+player.toggle());
            this.buttons[row][col].setText(player.showID());
        }

        public void endGame(Player winner){ 
            if (gameWon) { 
                this.gameGuide.setText("Game Over \n Player "+winner+" won");
            } else if (gameDraw){ 
                this.gameGuide.setText("Game Draw, No one wins ");
            }
        }

    }

}
