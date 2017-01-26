/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author LauraKrebs
 */
public class CloudTimer {

   private Timer timer;
   private Board board;

   public CloudTimer(Board board, int delay) {
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
      board.getCloud().update();

      board.repaint();

   }

   private boolean isRunning() {
      return timer.isRunning();
   }
}
