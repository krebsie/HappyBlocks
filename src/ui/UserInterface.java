/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import backend.CriticalException;
import backend.Game;
import graphics.Board;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import robsgraphics.ScoreboardAdv;

/**
 * Represents the game area visible to the user. This class extends JFrame, and
 * will be shown as the main area the user interacts with. It will be dominated
 * by the canvas, but will contain the options to launch other UI areas.
 *
 * @author Rob
 */
public class UserInterface extends JFrame {

   //Basic constants for size of frame.
   private int WIDTH = 1100;
   private int HEIGHT = 700;
   private final int RIGHTPANELWIDTH = 200;

   private Game game;
   private Board board;

   private final int MAXNUMBEROFPLAYERS = 4;
   private LinkedList<JPanel> playerPanels = new LinkedList<JPanel>();
   private ScoreboardAdv dScoreboard;

   /**
    * Default constructor for the UserInterface.
    */
   public UserInterface() throws InterruptedException {
      game = new Game(this);
      instantiateUI();
   }

   /**
    * A constructor for a game in progress.
    *
    * @param game The game in progress.
    */
   public UserInterface(Game game) throws InterruptedException {
      this.game = game;
      instantiateUI();
   }

   //===========UI CREATION HELPERS==============
   //Instantiate User Interface
   private void instantiateUI() throws InterruptedException {
      this.setPreferredSize(new Dimension(WIDTH, HEIGHT + 60));
      this.setTitle("Happy Blocks");
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);

      JPanel buttonPanel = createButtonPanel();
      buttonPanel.setPreferredSize(new Dimension(RIGHTPANELWIDTH,
          (int) (HEIGHT * 0.2)));

      board = new Board(game, 700, 700);
      
      dScoreboard = new ScoreboardAdv(300, 700, game);
      
      this.setLayout(new FlowLayout());
      board.setPreferredSize(new Dimension(700,700));
      this.add(board);

      JPanel rightPanel = new JPanel(new FlowLayout());
      dScoreboard.readypaint();
      rightPanel.add(dScoreboard);
      this.add(rightPanel);
   }

   //Create the button panels.
   private JPanel createButtonPanel() {
      JButton btnNewGame = new JButton();
      btnNewGame.setAction(new NewGameAction("New Game", null, "Play a new game.",
          this, game));
      JButton btnAbout = new JButton("About");
      JButton btnSpinClockwise = new JButton("Clockwise");
      JButton btnSpinCC = new JButton("Counterclockwise");

      btnAbout.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent ae) {
            AboutDialog dialog = new AboutDialog();
         }

      });

      btnSpinClockwise.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            game.gmvSpinBoard(true);
            board.readypaint();
         }
      });

      btnSpinCC.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            game.gmvSpinBoard(false);
            board.readypaint();
         }
      });

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridBagLayout());
      GridBagConstraints cButtonPanel = new GridBagConstraints();
      cButtonPanel.ipady = 0;
      cButtonPanel.gridwidth = 2;
      cButtonPanel.ipadx = 10;
      cButtonPanel.fill = GridBagConstraints.HORIZONTAL;
      cButtonPanel.weightx = 1;
      cButtonPanel.insets = new Insets(0, 10, 0, 00);

      //Add buttons
      buttonPanel.add(btnNewGame, cButtonPanel);
      cButtonPanel.gridy = 1;

      buttonPanel.add(btnAbout, cButtonPanel);     
      
      return buttonPanel;
   }

   //===========GETTERS AND SETTERS==============
   /**
    * Updates the scores for the players on the GUI.
    */
   public void updateGUIForNewGame() {
       if(game.getGameStatus()) {
            int gridsize = game.getBoardSize();
            int i = 700;
            //get the proper board size
            while(true) {
                if(i % gridsize == 1) break;
                i--;
            }
            board.setMySize(i);
       }
      board.readypaint();
      dScoreboard.readypaint();
   }

   /**
    * Updates the gui after a move.
    */
   public void updateGUIAfterMove() {
       board.readypaint();
       dScoreboard.readypaint();
       if(game.getGameOver()) {
           LinkedList<Integer> winners = new LinkedList<>();
           winners.add(0);
           Integer winningscore = game.getPlayerScore(0);
           
           for(int i = 1; i < game.getNumberOfPlayers(); i++) {
               if(game.getPlayerScore(i) > winningscore) {
                   winners.clear();
                   winningscore = game.getPlayerScore(i);
                   winners.add(i);
               }
               else if(game.getPlayerScore(i) == winningscore) {
                   winners.add(i);
               }
           }
           
           StringBuilder stbuild = new StringBuilder();
           stbuild.append("Game Over!");
           stbuild.append("\n\n");
           
           if(winners.size() == 1) {
               String name = game.getPlayerName(winners.get(0));
               stbuild.append(name + " wins!");
           } else {
               stbuild.append("It's a Draw");
           }
           
           stbuild.append("\nScore: " + winningscore.toString());
           
           
           JOptionPane.showMessageDialog(new JFrame(), stbuild.toString(),
                "Game Over", JOptionPane.PLAIN_MESSAGE);
       }
   }

   /**
    * This method generates an icon for use with buttons or labels. Note the
    * object passed in has the getClass() method called on it to find the
    * correct folder. Images should be placed in [PackageName].resources folder
    * and the object passed in (second parameter) should be a class in that
    * package.
    *
    * @param imageName The image filename + extension.
    * @param o An object, usually 'this' should be passed in.
    * @param width The width of the resulting icon.
    * @param height The height of the resulting icon.
    * @return An ImageIcon representation of the
    */
   public static ImageIcon makeResourceIcon(String imageName, Object o,
       int width, int height) {

      ImageIcon icon = null;
      try {
         icon = new ImageIcon(o.getClass().getResource("resources/" + imageName));

         Image img = icon.getImage();
         Image newimg = img.getScaledInstance(30, 25, java.awt.Image.SCALE_SMOOTH);
         icon = new ImageIcon(newimg);
      } catch (NullPointerException ex) {
         return null;
      }

      return icon;
   }
   
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
       UserInterface ui;
       try {
           ui = new UserInterface();
      
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.pack();      
        ui.setVisible(true);           
       } catch(Exception ex) {
           new CriticalException("Unhandled Exception thrown!\n" + ex.getStackTrace().toString());
       }

   }   

}
