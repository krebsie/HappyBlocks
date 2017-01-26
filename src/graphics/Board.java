/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import backend.Game;
import backend.GamePiece;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JComponent;
import listeners.DropListener;
import listeners.SpinListener;

/**
 * Graphical view of the game available to the user. This class is the main
 * canvas for drawing the various graphics used by the game. This class extends
 * JComponent so that it may be added to a frame.
 *
 * @author Laura
 */
public class Board extends JComponent {

   /**
    * Variable gridSize represents a gridSize x gridSize game board.
    */
   private int GRIDSIZE;
   private Game game;
   private int rows, columns, dropColumn;
   private boolean mouseActive;
   private DropTimer timer;
   private WelcomeTimer wTimer;
   private CloudTimer cTimer;
   private WelcomeBoard wBoard;
   private LinkedList<HappyBlock> blockList;
   private LinkedList<Integer> idList;
   private Cloud cloudClass;
   private DropListener dropListener;
   private SpinListener spinListener;
   private int fontSize = 14;
   private Font fontCoordSys = new Font("serif", Font.PLAIN, fontSize);

   public Board(Game game, int w, int h) throws InterruptedException {
      super();
      this.game = game;
      rows = game.getBoardSize();
      columns = game.getBoardSize();
      GRIDSIZE = game.getBoardSize();

      Dimension boardim = new Dimension(w, h);

      setPreferredSize(boardim);
      dropListener = new DropListener(game, this);
      addMouseListener(dropListener);
      addMouseMotionListener(spinListener);
      addMouseListener(spinListener);
      timer = new DropTimer(5, 1000, 4, this);
      blockList = new LinkedList<HappyBlock>();
      idList = new LinkedList<Integer>();
      wTimer = new WelcomeTimer(this, 100);
      cTimer = new CloudTimer(this, 1000);
      wBoard = new WelcomeBoard(this, boardim.height);
      cloudClass = new Cloud(this, boardim.height);
      Random generator = new Random();
      for (int i = 0; i < 30; i++) {
         int x = generator.nextInt(boardim.width);
         int y = generator.nextInt(boardim.height);
         cloudClass.add(new Point2D.Double(x, y));
      }
      cTimer.start();
   }

   /**
    * What will render the visual representation of the Board
    *
    * @param g the graphics context passed in from the current instance of the
    * Board class.
    */
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;

      //Flip the y-axis coords so that the first piece dropped shows up at
      //the bottom of the board, not the top
      int m = getHeight() / 2;
      // last-specified-first-applied (see java.awt.Graphics2D#transform)
      // last shift "down" (positive) m pixels
      g2.translate(0, m);
      // flip about y-axis
      g2.scale(1, -1);
      // first shift "up" (negative) m pixels
      g2.translate(0, -m);

      int numberOfPieces = getGame().getNumberOfPieces();
      if (getGame().getNumberOfPieces() == 0) {
         blockList.clear();
         idList.clear();
      }

      //If game hasn't started yet, paint a welcome screen
      if (game.getGameStatus() == false) {
         try {

            wBoard.drawMe(g2);
            wTimer.start();
         } catch (Exception ex) {
            drawWelcomeScreen(g2);
         }
      } else {
         GRIDSIZE = game.getBoardSize();
         rows = game.getBoardSize();
         columns = game.getBoardSize();

         //Background Color
         g2.setColor(new Color(0, 235, 252));
         g2.fillRect(0, 50, this.getWidth(), this.getHeight());

         //Drawing the Clouds
         g2.setColor(Color.WHITE);

         cloudClass.draw(g2);

         //Drawing the Grid
         g2.setColor(Color.BLACK);
         g2.setStroke(new BasicStroke(2.0f));
         g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

         int rowHt = (this.getHeight() - 50) / (rows);
         for (int i = 0; i < rows; i++) {
            g2.drawLine(0, i * rowHt + 50, this.getWidth(), i * rowHt + 50);
         }
         int rowWid = this.getWidth() / (columns);
         for (int i = 0; i < columns; i++) {
            g2.drawLine(i * rowWid, 50, i * rowWid, this.getHeight());
         }

         //Draw the blocks
         for (int i = 0; i < blockList.size(); ++i) {
            g2.setColor(blockList.get(i).getColor());
            blockList.get(i).drawMe(g2);
         }

         // Drawing the arrows - first arrow
         int barb = 20;
         double phi = Math.PI / 6;
         double theta, x, y;
         g2.setStroke(new BasicStroke(3.0f));

         g2.setPaint(Color.blue);
         double x1 = getWidth() * 3 / 24, y1 = getHeight() * 3 / 52, x2 = getWidth() * 11 / 24, y2 = y1;
         g2.draw(new Line2D.Double(getWidth() - 150, y1, getWidth() - 50, y2));
         // draw this arrow head at point x2, y2 and measure
         // angle theta relative to same point, ie, y2 - and x2 -
         theta = Math.atan2(y2 - y1, x2 - x1);
         drawArrow(g2, theta, getWidth() - 50, y2, barb, phi);

         //Second arrow
         g2.setColor(Color.blue);
         x1 = getWidth() * 3 / 8;
         y1 = getHeight() * 13 / 15;
         x2 = getWidth() * 2 / 3;
         y2 = y1;
         g2.draw(new Line2D.Double(50, getHeight() * 3 / 52, 150, getHeight() * 3 / 52));
         theta = Math.atan2(y1 - y2, x1 - x2);
         drawArrow(g2, theta, 50, getHeight() * 3 / 52, barb, phi);

         // drawing spin strings string
         AffineTransform flip = new AffineTransform();
         flip.setToScale(1, -1);
         //Shift the font back to the baseline after reflection.
         AffineTransform lift = new AffineTransform();
         lift.setToTranslation(0, fontSize);
         flip.preConcatenate(lift);
         g2.setColor(Color.MAGENTA);
         Font fontUpsideDown = fontCoordSys.deriveFont(flip);
         g2.setFont(fontUpsideDown);
         String counterClockwise = "Spin Counter-Clockwise";
         g2.drawString(counterClockwise, getWidth() - 150, -10);
         String clockwise = "Spin Clockwise";
         g2.drawString(clockwise, 15, -10);
      }
   }

   private void drawArrow(Graphics2D g2, double theta, double x0, double y0, int barb, double phi) {
      double x = x0 - barb * Math.cos(theta + phi);
      double y = y0 - barb * Math.sin(theta + phi);
      g2.draw(new Line2D.Double(x0, y0, x, y));
      x = x0 - barb * Math.cos(theta - phi);
      y = y0 - barb * Math.sin(theta - phi);
      g2.draw(new Line2D.Double(x0, y0, x, y));
   }

   private void drawWelcomeScreen(Graphics2D g2) {
      rows = 8;
      columns = 8;
      GRIDSIZE = 8;

      //Background Color
      g2.setColor(new Color(0, 235, 252));
      g2.fillRect(0, 50, this.getWidth(), this.getHeight());

      //Drawing the Clouds
      g2.setColor(Color.WHITE);
      //Cloud cloudClass = new Cloud(this, getHeight());
      Random generator = new Random();
      for (int i = 0; i < 40; i++) {
         int x = generator.nextInt(getWidth());
         int y = generator.nextInt(getHeight());
         //c.add(new Point2D.Double(x, y));
      }
      //c.draw(g2);

      //Drawing the Grid
      g2.setColor(Color.BLACK);
      g2.setStroke(new BasicStroke(2.0f));
      g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

      int rowHt = (this.getHeight() - 50) / (rows);
      for (int i = 0; i < rows; i++) {
         g2.drawLine(0, i * rowHt + 50, this.getWidth(), i * rowHt + 50);
      }
      int rowWid = this.getWidth() / (columns);
      for (int i = 0; i < columns; i++) {
         g2.drawLine(i * rowWid, 50, i * rowWid, this.getHeight());
      }

      // Drawing the arrows - first arrow
      int barb = 20;
      double phi = Math.PI / 6;
      double theta, x, y;
      g2.setStroke(new BasicStroke(3.0f));

      g2.setPaint(Color.blue);
      double x1 = getWidth() * 3 / 24, y1 = getHeight() * 3 / 52, x2 = getWidth() * 11 / 24, y2 = y1;
      g2.draw(new Line2D.Double(getWidth() - 150, y1, getWidth() - 50, y2));
      // draw this arrow head at point x2, y2 and measure
      // angle theta relative to same point, ie, y2 - and x2 -
      theta = Math.atan2(y2 - y1, x2 - x1);
      drawArrow(g2, theta, getWidth() - 50, y2, barb, phi);

      //Second arrow
      x1 = getWidth() * 3 / 8;
      y1 = getHeight() * 13 / 15;
      x2 = getWidth() * 2 / 3;
      y2 = y1;
      g2.draw(new Line2D.Double(50, getHeight() * 3 / 52, 150, getHeight() * 3 / 52));
      theta = Math.atan2(y1 - y2, x1 - x2);
      drawArrow(g2, theta, 50, getHeight() * 3 / 52, barb, phi);
   }

   public Board getBoard() {
      return this;
   }

   public int getGridSize() {
      return this.getGRIDSIZE();
   }

   public void setGridSize(int size) {
      this.GRIDSIZE = size;
   }

   public int getRows() {
      return this.rows;
   }

   public void setRows(int row) {
      this.rows = row;
   }

   public int getColumns() {
      return this.columns;
   }

   public void setColumns(int col) {
      this.columns = col;
   }

   public Game getGame() {
      return this.game;
   }

   public void setGame(Game newGame) {
      this.game = newGame;
   }

   /**
    * @return the GRIDSIZE
    */
   public int getGRIDSIZE() {
      return GRIDSIZE;
   }

   /**
    * This method readies the board for repainting by starting the global timer
    * used in the happy blocks. Only starts timer if necessary.
    */
   public void readypaint() {
      if (timer.isRunning()) {
         timer.stop();
      }

      if (blockList.size() != game.getNumberOfPieces()) {
         for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
               if (game.isBoardSpaceOccupied(i, j)) {
                  int blockID = game.getIDOfGamePiece(i, j);
                  if (idList.contains(blockID)) {

                  } else {
                     Color color = game.getColorOfGamePiece(i, j);

                     int cellWidth = this.getWidth() / getGRIDSIZE();
                     int cellHeight = (this.getHeight() - 50) / getGRIDSIZE();

                     GamePiece gp = game.getGamePiece(i, j);

                     HappyBlock block = new HappyBlock(gp, cellWidth, cellHeight, timer, this, color);
                     blockList.add(block);
                     idList.add(blockID);
                  }
               }
            }
         }
      }

      //Restart the drawing status of all the blocks.
      for (int i = 0; i < blockList.size(); i++) {
         blockList.get(i).restartDraw();
         blockList.get(i).checkPosition();
      }

      boolean needTimer = false;
      for (int i = 0; i < blockList.size() && !needTimer; i++) {
         if (blockList.get(i).isMoving()) {
            needTimer = true;
         }
      }

      if (needTimer) {
         timer.setBlockCount(blockList.size());
         timer.start();
      } else {
         repaint();
      }

   }

   /**
    * overloaded for painting the welcome screen
    */
   public void readypaint(boolean isWelcomeScreen) {
      if (isWelcomeScreen) {
         repaint();
      }
   }

   public WelcomeBoard getWelcomeBoard() {
      return this.wBoard;
   }

   public Cloud getCloud() {
      return this.cloudClass;
   }

   public CloudTimer getCloudTimer() {
      return this.cTimer;
   }

   public void addMouse() {
      this.addMouseListener(dropListener);
   }

   public void removeMouse() {
      this.removeMouseListener(dropListener);
   }

   public void setMouseActive(boolean isActive) {
      this.mouseActive = isActive;
   }

   public boolean getMouseActive() {
      return this.mouseActive;
   }

   public void setMySize(int i) {
      this.setPreferredSize(new Dimension(i, i));
      this.setSize(i, i + 2);
   }

   public LinkedList<HappyBlock> getBlockList() {
      return this.blockList;
   }
}
