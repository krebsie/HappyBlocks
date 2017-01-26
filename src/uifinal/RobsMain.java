/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uifinal;

import backend.Game;
import backend.Player;
import java.awt.Color;
import javax.swing.JFrame;
import robsgraphics.ScoreboardAdv;
import ui.UserInterface;

/**
 *
 * @author Rob
 */
public class RobsMain {

   //Test Game
   Game game;

   public RobsMain() {
      Player[] players = new Player[3];
      players[0] = new Player("Rob", Color.GREEN);
      players[1] = new Player("Laura", Color.RED);
      players[2] = new Player("Jesus", Color.CYAN);
      players[1].setPlayerType(Player.NETWORK);
      players[2].setPlayerType(Player.AIENABLED);
      game = new Game(2, players);
      try {
          game.setUI(new UserInterface());
      } catch(InterruptedException ex) {
          
      }
      
      game.gmvDropPiece(1);
      
   }

   public Game getGame() {
      return game;
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
       JFrame testFrame = new JFrame();
       RobsMain rb = new RobsMain();
       testFrame.add(new ScoreboardAdv(300,800, rb.game));
      testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      testFrame.pack();
      testFrame.setVisible(true);       
   }
}
