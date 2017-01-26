/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import graphics.WelcomeBoard.Block;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * /**
 * A timer for animating the dropping blocks on the WelcomeBoard. Every "delay"
 * milliseconds, the fire() method executes which decrements a counter from the
 * initial value and calls repaint(). Timer automatically stops when the new
 * game is chosen.
 *
 * @author LauraKrebs
 */
public class WelcomeTimer {

   private Timer timer;
   private Board board;

   public WelcomeTimer(Board board, int delay) {
      this.board = board;
      timer = new Timer(delay, createActionListener());

   }

   private ActionListener createActionListener() {
      return new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //Meat of the timer.  What happens each time it fires.
            //Fires repaint to force the images.
            fire();
         }
      };
   }

   /**
    * Starts the timer.
    */
   void start() {
      timer.start();
   }

   /**
    * Stops the timer and resets the flag.
    */
   void stop() {
      timer.stop();
   }

   /**
    * Method executes every "delay" value.
    */
   private void fire() {
      for (Block aBlock : board.getWelcomeBoard().getBlockArray()) {
         aBlock.update();
      }

      board.repaint();

      if (board.getGame().getGameStatus() == true) {
         stop();
      }
   }

}
