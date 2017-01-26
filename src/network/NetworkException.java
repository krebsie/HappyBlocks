/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This exception is thrown when an error occurs during the networking.  A popup
 * dialog is used to describe the situation.
 * @author Rob
 */
public class NetworkException extends Exception{
    String reason;
    
    /**
     * Constructor for the NetworkException which displays the reason in a 
     * popup dialog.
     * @param reason 
     */
    public NetworkException(String reason) {
        this.reason = reason;
        System.out.println("NETWORK EXCEPTION: " + reason);
        JOptionPane.showMessageDialog(new JFrame(), "Network Error: " + reason,
                "Network Error", JOptionPane.ERROR_MESSAGE);
    }
}