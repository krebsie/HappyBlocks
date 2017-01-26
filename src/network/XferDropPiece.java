/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import backend.Player;

/**
 * This class is used for networking a game piece drop move.
 * @author Rob
 */
public class XferDropPiece extends XferAction {
    //Data Members
    private Integer column;
    
    /**
     * Default constructor
     * @param player The player to pass over.
     */
    public XferDropPiece(Integer column) {
        this.column = column;
    }

    /**
     * Executes the player transfer.
     */
    @Override
    public void execute() {
        super.ni.executeDropPiece(column);
    }
    
}
