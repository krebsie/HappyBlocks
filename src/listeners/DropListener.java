/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import backend.Game;
import graphics.Board;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Listener for user interactions for dropping new blocks. This listener allows
 * for interactions between the Board and the user. This class is used by the
 * Board class.
 *
 * @author Laura
 */
public class DropListener implements MouseListener {

   private Game game;
   private Board board;
   private int currentDropColumn;

   public DropListener(Game game, Board board) {
      this.game = game;
      this.board = board;
   }

   @Override
   public void mouseClicked(MouseEvent me) {
      //Maybe do stuff
   }

   @Override
   public void mousePressed(MouseEvent me) {
   }

   /**
    * Handles the event of a user clicking on a column to make a game move by
    * dropping a piece in that column.
    */
   @Override
   public void mouseReleased(MouseEvent me) {

      if (game.getGameStatus()) {
         if (true) {
            int colSize = board.getWidth() / board.getGridSize();

            int x = me.getX();
            int y = me.getY();
            int j = 0;
            if (y < (board.getHeight() - 50)) {
               for (int i = 0; i < board.getGridSize(); i++) {
                  if ((j < x) && (x < (j + colSize))) {
                     game.gmvDropPiece(i);
                     board.readypaint();
                  }
                  j += colSize;
               }
            } else {
               if (x < (board.getWidth() / 2)) {
                  game.gmvSpinBoard(true);
                  for (int i = 0; i < board.getBlockList().size(); ++i) {
                     board.getBlockList().get(i).setSpun(true);
                  }
                  board.readypaint();
                  for (int i = 0; i < board.getBlockList().size(); ++i) {
                     board.getBlockList().get(i).setSpun(false);
                  }
               } else {
                  game.gmvSpinBoard(false);
                  for (int i = 0; i < board.getBlockList().size(); ++i) {
                     board.getBlockList().get(i).setSpun(true);
                  }
                  board.readypaint();
                  for (int i = 0; i < board.getBlockList().size(); ++i) {
                     board.getBlockList().get(i).setSpun(false);
                  }
               }
            }
         }
      }
   }

   @Override
   public void mouseEntered(MouseEvent me) {
      //Maybe do stuff
   }

   @Override
   public void mouseExited(MouseEvent me) {
      //Maybe do stuff
   }

   public int getCurrentDropColumn() {
      return this.currentDropColumn;
   }

}
