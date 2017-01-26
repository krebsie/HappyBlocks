/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import robsgraphics.SbActionArea;
import robsgraphics.ScoreboardAdv;

/**
 * A listener for the scoreboard section of the UI.
 * @author Rob
 */
public class ScoreboardListener implements MouseListener {
    //Data members
    private ScoreboardAdv sb;
    private SbActionArea[] actionAreas;
    
    public ScoreboardListener(ScoreboardAdv sb) {
        this.sb = sb;
        this.actionAreas = sb.getActionAreas();
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
        //Do nothing
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //Do nothing    
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        Point pt = me.getPoint();
        for(int i = 0; i < actionAreas.length; i++) {
            if(actionAreas[i].contains(pt)) actionAreas[i].execute();
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //Do nothing
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //Do nothing
    }
    
}
