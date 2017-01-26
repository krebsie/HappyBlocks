/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robsgraphics;

import backend.Game;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.Rectangle;
import ui.AboutDialog;
import ui.NewGameDialog;

/**
 * This class contains a rectangular point area that corresponds to a drawn
 * rectangle (a button).  It's used by the ScoreboardListener.
 */
public class SbActionArea {
    //Static vars
    static final int NEWGAMEAREA = 1;
    static final int ABOUTAREA = 2;

    //Data Members
    private int x1;
    private int y1;
    private int w;
    private int h;
    private int type;
    private Game game;

    /**
     * Constructor for action area
     * @param tx1 x1
     * @param ty1 y1
     * @param tw width
     * @param th height
     * @param type static type integer.
     */
    public SbActionArea(int tx1, int ty1, int tw, int th, int type, Game game) {
        x1 = tx1;
        w = tw;
        y1 = ty1;
        h = th;
        this.type = type;
        this.game = game;
    }

    /**
     * Checks to see if a given point is in the area.  Called by the Scoreboard
     * listener.
     * @param pt point to check
     * @return whether the point is in the area.
     */
    public boolean contains(Point pt) {
        Rectangle rect = new Rectangle(x1,y1,w,h);
        return rect.contains(pt);
    }

    /**
     * Executes the commands depending on the type of area.
     */
    public void execute() {
        switch (type) {
            case NEWGAMEAREA:
                NewGameDialog nd = new NewGameDialog(game);
                nd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                nd.setVisible(true);
                break;
            case ABOUTAREA:
                AboutDialog dialog = new AboutDialog();
                break;
        }
    }
}