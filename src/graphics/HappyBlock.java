/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import backend.GamePiece;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author LauraKrebs
 */
public class HappyBlock {

   private int xPosition, yPosition, cellWidth, cellHeight,
       row, column, startingYPosition, finalYPosition;
   private Board board;
   private DropTimer timer;
   private Color color;
   private GamePiece gp;
   private boolean timerReported = false;
   private boolean spun = false;

   public HappyBlock(GamePiece gp, int cellWidth, int cellHeight,
       DropTimer timer, Board board, Color color) {
      this.gp = gp;
      this.row = gp.getRow();
      this.column = gp.getColumn();

      this.xPosition = column * cellWidth;
      this.startingYPosition = cellHeight * board.getGRIDSIZE() + 50;
      this.finalYPosition = row * cellHeight + 50;
      this.yPosition = startingYPosition;
      this.cellWidth = cellWidth;
      this.cellHeight = cellHeight;

      this.timer = timer;
      this.board = board;
      this.color = color;
   }

   public void drawMe(Graphics2D g2) {
      //Check to see if the position has changed
      if (spun) {
         drawMeFalling(g2);
      }
      checkPosition();
      if (yPosition == finalYPosition) {
         if (!timerReported) {
            timer.reportDone();
            timerReported = true;
         }
         drawMeStationary(g2);
      } else {
         drawMeFalling(g2);
      }

   }

   private void drawMeStationary(Graphics2D g2) {

      if (gp.isScored()) {
         color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
      }

      //Block itself
      g2.setColor(color);
      g2.fill(new Rectangle2D.Double(xPosition + 1, yPosition + 1, cellWidth - 2, cellHeight - 2));

      //Outline around the block
      g2.setColor(Color.BLACK);
      g2.setStroke(new BasicStroke((float) 2.5));
      g2.draw(new Rectangle2D.Double(xPosition, yPosition, cellWidth, cellHeight));
      g2.draw(new Line2D.Double(xPosition, yPosition + 1, xPosition + cellWidth, yPosition + 1));

      if ((column + 1) == board.getGridSize()) {
         g2.setStroke(new BasicStroke(4.0f));
//         g2.draw(new Line2D.Double(xPosition + cellWidth, yPosition, xPosition + cellWidth, yPosition + cellHeight));
         g2.fillRect(xPosition + cellWidth, yPosition, board.getWidth() - (board.getColumns() * cellWidth), cellHeight);
      }

      if ((row + 1) == board.getGridSize()) {
         g2.fillRect(xPosition, yPosition + cellHeight, cellWidth, board.getHeight() - (board.getRows() * cellHeight));
      }

      //Happy face
      g2.setColor(Color.BLACK);
      //First eye
      g2.fillOval(xPosition + (int) (cellWidth * 0.25), (yPosition + cellHeight) - (int) (0.3 * cellHeight), cellWidth / 10, cellHeight / 10);

      //Second eye
      g2.fillOval(xPosition + (int) (0.6 * cellWidth), (yPosition + cellHeight) - (int) (0.3 * cellHeight), cellWidth / 10, cellHeight / 10);

      //Unhappy Block
//      g2.fillArc(xPosition + (int) (cellWidth * 0.25), yPosition + (int) (cellHeight * 0.5), (int) (cellWidth * 0.3), (int) (cellHeight * 0.2), 0, -180);
      //Smile
      g2.fillArc(xPosition + (int) (0.25 * cellWidth), (yPosition + cellHeight) - (int) (cellHeight * 0.6), (int) (cellWidth * 0.3), (int) (cellHeight * 0.2), 0, 180);

      if (gp.isScored()) {
         color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
         g2.setColor(color);
         g2.setStroke(new BasicStroke(5.0f));
         g2.drawLine(xPosition, finalYPosition, xPosition + cellWidth, finalYPosition + cellHeight);
         g2.drawLine(xPosition, finalYPosition + cellHeight, xPosition + cellWidth, finalYPosition);
      }

   }

   public void drawMeFalling(Graphics2D g2) {

      yPosition = startingYPosition - timer.getValue();
      if (yPosition < finalYPosition) {
         yPosition = finalYPosition;
      }
      Color color = g2.getColor();

      //Block itself
      g2.setColor(color);
      g2.fill(new Rectangle2D.Double(xPosition + 1, yPosition + 1, cellWidth - 2, cellHeight - 2));

      //Outline around the block
      g2.setColor(Color.BLACK);
      g2.setStroke(new BasicStroke((float) 2.5));
      g2.draw(new Rectangle2D.Double(xPosition, yPosition, cellWidth, cellHeight));
      g2.draw(new Line2D.Double(xPosition, yPosition + 1, xPosition + cellWidth, yPosition + 1));
//      g2.draw(new Line2D.Double());

      //Happy face
      g2.setColor(Color.BLACK);
      //First eye
      g2.fillOval(xPosition + (int) (cellWidth * 0.25), (yPosition + cellHeight) - (int) (0.3 * cellHeight), cellWidth / 10, cellHeight / 10);

      //Second eye
      g2.fillOval(xPosition + (int) (0.6 * cellWidth), (yPosition + cellHeight) - (int) (0.3 * cellHeight), cellWidth / 10, cellHeight / 10);

      //O-mouth
      g2.fillOval(xPosition + (int) (cellWidth * 0.423), (yPosition + cellHeight) - (int) (0.6 * cellHeight), cellWidth / 8, cellHeight / 8);

   }

   /**
    * For faceShape - pass in an "o' for the 'o' face, a 'h' for happy face, and
    * 'f' for frowny face
    *
    * @param g2
    * @param faceShape
    */
   public static void drawMeStatic(Graphics2D g2, String faceShape, int x, int y,
       int w, int h, Color col, boolean flip) {
      //Block itself
      g2.setColor(col);
      g2.fill(new Rectangle2D.Double(x, y, w, h));

      //Outline around the block
      g2.setColor(Color.BLACK);
      g2.setStroke(new BasicStroke((float) 2.5));
      g2.draw(new Rectangle2D.Double(x, y, w, h));

      //Happy face
      g2.setColor(Color.BLACK);
      if (flip) {
         //First eye
         g2.fillOval(x + (int) (w * 0.25), y + (h) - (int) (0.7 * h), w / 10, h / 10);

         //Second eye
         g2.fillOval(x + (int) (0.6 * w), y + (h) - (int) (0.7 * h), w / 10, h / 10);

         switch (faceShape) {
            case "o":
               g2.fillOval(x + (int) (w * 0.423), y + (h) - (int) (0.4 * h), w / 8, h / 8);
               break;
            case "h":
               g2.fillArc(x + (int) (0.25 * w), y + (h) - (int) (h * 0.4), (int) (w * 0.3), (int) (h * 0.2), 0, 180);
               break;
            case "u":
               g2.fillArc(x + (int) (w * 0.25), y + (int) (h * 0.5), (int) (w * 0.3), (int) (h * 0.2), 0, -180);
               break;
            default:
               g2.fillArc(x + (int) (0.25 * w), y + (h) - (int) (h * 0.4), (int) (w * 0.3), (int) (h * 0.2), 0, 180);
               break;
         }
      } else {
         //First eye
         g2.fillOval(x + (int) (w * 0.25), y + (h) - (int) (0.3 * h), w / 10, h / 10);

         //Second eye
         g2.fillOval(x + (int) (0.6 * w), y + (h) - (int) (0.3 * h), w / 10, h / 10);

         switch (faceShape) {
            case "o":
               g2.fillOval(x + (int) (w * 0.423), y + (h) - (int) (0.6 * h), w / 8, h / 8);
               break;
            case "h":
               g2.fillArc(x + (int) (0.25 * w), y + (h) - (int) (h * 0.6), (int) (w * 0.3), (int) (h * 0.2), 0, 180);
               break;
            case "u":
               g2.fillArc(x + (int) (w * 0.25), y + (int) (h * 0.5), (int) (w * 0.3), (int) (h * 0.2), 0, -180);
               break;
            default:
               g2.fillArc(x + (int) (0.25 * w), y + (h) - (int) (h * 0.6), (int) (w * 0.3), (int) (h * 0.2), 0, 180);
               break;
         }
      }

   }

   public boolean checkPosition() {
      if (spun) {
         row = gp.getRow();
         column = gp.getColumn();
         this.yPosition = gp.getPreTumbleRow() * cellHeight + 50;
         this.startingYPosition = yPosition;
         this.xPosition = column * cellWidth;
         this.finalYPosition = row * cellHeight + 50;

      }
      boolean changed = false;
      if (row != gp.getRow()) {
         row = gp.getRow();
         changed = true;
      }
      if (column != gp.getColumn()) {
         column = gp.getColumn();
         changed = true;
      }

      if (changed) {
         this.yPosition = gp.getPreTumbleRow() * cellHeight + 50;
         this.startingYPosition = yPosition;
         this.xPosition = column * cellWidth;
         this.finalYPosition = row * cellHeight + 50;
      }
      return changed;
   }

   public int getXPosition() {
      return this.xPosition;
   }

   public void setXPosition(int xPos) {
      this.xPosition = xPos;
   }

   public int getYPosition() {
      return this.yPosition;
   }

   public void setYPosition(int yPos) {
      this.yPosition = yPos;
   }

   public int getRow() {
      return this.row;
   }

   public void setRow(int r) {
      this.row = r;
   }

   public int getColumn() {
      return this.column;
   }

   public void setColumn(int c) {
      this.column = c;
   }

   public int getCellWidth() {
      return this.cellWidth;
   }

   public void setCellWidth(int w) {
      this.cellWidth = w;
   }

   public int getCellHeight() {
      return this.cellHeight;
   }

   public void setCellHeight(int h) {
      this.cellHeight = h;
   }

   /**
    * Method for finding if the block is in its correct position.
    *
    * @return
    */
   public boolean isMoving() {
      return !(yPosition == finalYPosition);
   }

   /**
    * Method called when the drawing needs to be reset.
    */
   public void restartDraw() {
      timerReported = false;
   }

   public Color getColor() {
      return this.color;
   }

   public void setSpun(boolean isSpun) {
      this.spun = isSpun;
   }

   public boolean getSpun() {
      return this.spun;
   }

}
