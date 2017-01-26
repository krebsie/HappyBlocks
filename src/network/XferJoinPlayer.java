/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import backend.Game;
import backend.Player;
import ui.NewGameDialog;

/**
 * The XferJoinPlayer is used to send a single joining player into the game.
 * @author Rob
 */
public class XferJoinPlayer extends XferAction {
    //Data Members
    private Player player;
    
    /**
     * Default constructor
     * @param player The player to pass over.
     */
    public XferJoinPlayer(Player player) {
        this.player = player;
    }

    /**
     * Executes the player transfer.
     */
    @Override
    public void execute() {
        super.ni.executeJoiningPlayer(player);
    }
    
}
