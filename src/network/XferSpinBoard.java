/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

/**
 * This class is used for networking a game spin board move.
 * @author Rob
 */
public class XferSpinBoard extends XferAction {
    //Data Members
    private boolean clockwise;
    
    /**
     * Default constructor
     * @param player The player to pass over.
     */
    public XferSpinBoard(boolean clockwise) {
        this.clockwise = clockwise;
    }

    /**
     * Executes the player transfer.
     */
    @Override
    public void execute() {
        super.ni.executeSpinBoard(clockwise);
    }
    
}
