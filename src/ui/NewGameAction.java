/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import backend.Game;
import java.awt.Dialog;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

/**
 * The NewGameAction is performed when the user clicks the new game button.
 * @author Rob
 */
public class NewGameAction extends AbstractAction {

    private NewGameDialog nui;
    private Game game = null;
    private UserInterface ui;
    
    /**
     * Constructor for a NewGameAction
     * @param text Text to set the button or item to.
     * @param icon Icon to use to represent the action.
     * @param desc Tooltip text.
     * @param ui The parent UserInterface object.
     * @param game Game to work with.
     */
    public NewGameAction(String text, ImageIcon icon, String desc, 
            UserInterface ui, Game game) {
          super(text, icon);
          putValue(SHORT_DESCRIPTION, desc);
          this.ui = ui;
          this.game = game;
    }
    
    /**
     * Creates a NewGameUI and sets the game up accordingly.
     * @param ae ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        nui = new NewGameDialog(game);
        nui.setModalityType(ModalityType.APPLICATION_MODAL);
        nui.setVisible(true);
        ui.updateGUIForNewGame();
    }
    
}
