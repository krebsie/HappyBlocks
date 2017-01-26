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
 * A timer for animating the dropping blocks. Every "delay" milleseconds, the
 * fire() method executes which decrements a counter from the initial value and
 * calls repaint(). Timer automatically stops when the currentValue reaches
 * zero.
 *
 * @author Rob
 */
public class DropTimer {

   private Timer t;
   private Board board;
   private int endingValue;
   private int currentValue;
   private int decrement;
   private int doneBlocks = 0;
   private int blocksCount = 0;

   /**
    * Creates the timer with a delay value and a starting value.
    *
    * @param delay The delay (in ms) for each timer event to fire.
    * @param startingValue The value for the timer to start with
    */
   public DropTimer(int delay, int endingValue, int decrement, Board board) {
      t = new Timer(delay, createActionListener());
      this.endingValue = endingValue;
      this.board = board;
      this.decrement = decrement;
      currentValue = 0;
   }

   //Method to create the action listener.
   private ActionListener createActionListener() {
      return new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent ae) {
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
      t.start();
      board.removeMouse();
      board.setMouseActive(false);
   }

   /**
    * Stops the timer and resets the flag.
    */
   void stop() {
      t.stop();
      currentValue = 0;
      doneBlocks = 0;
      if (!board.getMouseActive()) {
         board.addMouse();
         board.setMouseActive(true);
      }
   }

   /**
    * Method executes every "delay" value.
    */
   private void fire() {
      currentValue += decrement;
      board.repaint();

      if (currentValue >= endingValue) {
         stop();
      }
   }

   /**
    * Returns the currentValue of the decrementing timer.
    */
   int getValue() {
      return currentValue;
   }

   /**
    * A block reports done which increments the count. When all blocks report
    * done, the timer stops.
    */
   void reportDone() {
      doneBlocks++;

      if (doneBlocks >= blocksCount) {
         stop();
      }
   }

   /**
    * Sets the block count for checking the number of blocks reported done.
    *
    * @param a Number of total blocks to draw.
    */
   void setBlockCount(int a) {
      blocksCount = a;
   }

   /**
    * Returns the running status of the timer.
    *
    * @return
    */
   boolean isRunning() {
      return t.isRunning();
   }
}
