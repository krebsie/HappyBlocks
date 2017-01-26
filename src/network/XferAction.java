/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import backend.Game;
import backend.Player;
import java.io.Serializable;
import ui.NewGameDialog;

/**
 * Provides a superclass for the other Xfer classes.
 * @author Rob
 */
public class XferAction implements Serializable {
    //Data members.
    protected transient NetworkInterface ni;
    
    /**
     * Method should be overridden.
     */
    public void execute() {
        //Override in the submethod
        
    }
    
    /**
     * Sets the object for the action.
     * @param setme 
     */
    public void setTransientVars(NetworkInterface ni) {
        this.ni = ni;
    }
    
}
