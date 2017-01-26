/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uifinal;

import java.awt.Dimension;
import javax.swing.JFrame;
import ui.UserInterface;

/**
 * Test Class. This class will not be used in the final project but instead be a
 * test class for launching the game.
 *
 * @author Rob
 */
public class UIFinal {

   //TEST
   // LAURA TEST CHANGE
   //ROB TEST CHANGE
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
       } catch(InterruptedException ex) {
           
       }

   }

}
