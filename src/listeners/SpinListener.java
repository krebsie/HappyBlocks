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
import java.awt.event.MouseMotionListener;

/**
 * Listener for user interactions for spinning the board. This listener allows
 * for interactions between the Board and the user.
 *
 * @author Laura
 */
public class SpinListener implements MouseMotionListener, MouseListener {

   private Game game;
   private Board board;
   private int startX;
   private boolean wasDragged;

   public SpinListener(Game game, Board board) {
      this.game = game;
      this.board = board;
   }

   @Override
   public void mouseDragged(MouseEvent e) {
      wasDragged = true;
   }

   @Override
   public void mouseMoved(MouseEvent e) {

   }

   @Override
   public void mouseClicked(MouseEvent e) {

   }

   @Override
   public void mousePressed(MouseEvent e) {
      if ((e.getY() < board.getHeight()) && (e.getY() > (board.getHeight() - 50))) {
         startX = e.getX();
      }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      if ((e.getY() < board.getHeight()) && (e.getY() > (board.getHeight() - 50))) {
         if (wasDragged) {
            if (e.getX() < board.getWidth() / 2) {
               game.gmvSpinBoard(true);
               System.out.println("SPIN PICKED UP");
               for (int i = 0; i < board.getBlockList().size(); ++i) {
                  board.getBlockList().get(i).setSpun(true);
               }
               board.readypaint();
               for (int i = 0; i < board.getBlockList().size(); ++i) {
                  board.getBlockList().get(i).setSpun(false);
               }
            } else {
               game.gmvSpinBoard(false);
               System.out.println("SPIN");
               for (int i = 0; i < board.getBlockList().size(); ++i) {
                  board.getBlockList().get(i).setSpun(true);
               }
               board.readypaint();
               for (int i = 0; i < board.getBlockList().size(); ++i) {
                  board.getBlockList().get(i).setSpun(false);
               }
            }
         }
         wasDragged = false;
      }
   }

   @Override
   public void mouseEntered(MouseEvent e) {

   }

   @Override
   public void mouseExited(MouseEvent e) {

   }

}
