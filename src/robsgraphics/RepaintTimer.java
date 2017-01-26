/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robsgraphics;


import graphics.Board;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * This class is a more general form of the CloudTimer.
 * @author LauraKrebs
 */
public class RepaintTimer {

   private Timer timer;
   private JComponent component;

   public RepaintTimer(JComponent component, int delay) {
      this.component = component;
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
      component.repaint();
   }

   private boolean isRunning() {
      return timer.isRunning();
   }
}
