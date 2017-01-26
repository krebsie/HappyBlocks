/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This exception is thrown when something really bad happens.  The end user
 * should never see these messages.
 * @author Rob
 */
public class CriticalException extends Exception{
    String reason;
    
    public CriticalException(String reason) {
        this.reason = reason;
        //this.printStackTrace
        System.out.println("CRITICAL EXCEPTION: " + reason);
        JOptionPane.showMessageDialog(new JFrame(), "CRITICAL EXCEPTION: " + reason
                + "\nThis should never be seen by the user.", 
                "Critical Exception", JOptionPane.ERROR_MESSAGE);
    }
}
