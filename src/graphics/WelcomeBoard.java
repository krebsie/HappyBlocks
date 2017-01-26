/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Graphical animated view that displays to user upon opening the application.
 * Once the user begins a new game this board disappears.
 *
 * @author LauraKrebs
 */
public class WelcomeBoard {
   public static final Color SKYCOLOR = new Color(0, 235, 252);
    
   private Board board;
   private Block[] blockArray = new Block[20];
   private int height;
   private int width;
   private Cloud cloudC;
   private CloudTimer cTimer;

   public WelcomeBoard(Board board, int h) throws InterruptedException {
      this.board = board;
      this.height = h;
      this.width = height;

      for (int i = 0; i < blockArray.length; i++) {
         blockArray[i] = new Block(this.board, height);
      }
      cTimer = new CloudTimer(this.board, 100);
      cloudC = new Cloud(board, this.height);
      Random generator = new Random();
      for (int i = 0; i < 40; i++) {
         int x = generator.nextInt(width);
         int y = generator.nextInt(height);
         cloudC.add(new Point2D.Double(x, y));
      }
      cTimer.start();
   }

   public void drawMe(Graphics2D g2) {
      g2.setColor(SKYCOLOR);
      g2.fillRect(0, 0, board.getWidth(), board.getHeight());

      //Drawing the Clouds
      g2.setColor(Color.WHITE);
//      Cloud cloudC = new Cloud(board, board.getHeight());
//      Random generator = new Random();
//      for (int i = 0; i < 40; i++) {
//         int x = generator.nextInt(board.getWidth());
//         int y = generator.nextInt(board.getHeight());
//         cloudC.add(new Point2D.Double(x, y));
//      }
      cloudC.draw(g2);

      //Draw the Happy Blocks
      for (Block aBlockArray : blockArray) {
         aBlockArray.drawMe(g2);
      }

   }

   public void update() {

      //calls the Block class update method on the block objects
      for (Block block : blockArray) {
         block.update();
      }
   }

   public Block[] getBlockArray() {
      return this.blockArray;
   }

   public Board getBoard() {
      return this.board;
   }

   public class Block extends JPanel {

      private int blockXLocation;
      private int blockSize;
      private int blockYLocation;
      private int fallSpeed = 1;
      private Board board;
      private Random rand = new Random();
      private int blockheight;
      private Color color;
      /*
       * sets the blockWidth and block fallSpeed to a random value for every
       * block created
       */

      public Block(Board board, int height) {
         this.board = board;
         blockYLocation = height;
//         blockYLocation = 0;
         this.blockheight = height;

         blockSize = generateRandomBlockSize();
         blockXLocation = generateRandomXLocation();
         fallSpeed = generateRandomFallSpeed();

         if (rand.nextInt() % 2 == 0) {
            color = Color.CYAN;
         } else if (rand.nextInt() % 3 == 0) {
            color = Color.RED;
         } else if (rand.nextInt() % 5 == 0) {
            color = Color.PINK;
         } else {
            color = Color.MAGENTA;
         }

      }

      /*
       //creates a random value between 1-50 and stores it in blockWidth
       */
      public int generateRandomBlockSize() {
         blockSize = rand.nextInt(70);
         return blockSize;
      }

      /*
       //creates a random value inside the window and stores it in blockXLocation
       */
      public int generateRandomXLocation() {
         if (rand.nextInt(blockheight - blockSize) <= 0) {
            while (rand.nextInt(height - blockSize + 1) <= 0) {
               blockXLocation = rand.nextInt(height - blockSize + 1);
            }
         } else {
            blockXLocation = rand.nextInt(height - blockSize);
         }
         return blockXLocation;
      }

      /*
       //creates a random value that is not zero and stores it in fallSpeed(so blocks do not get stuck at 0 speed)
       */
      public int generateRandomFallSpeed() {
         return fallSpeed = rand.ints(1, 1, 10).findFirst().getAsInt();
      }

      /*
       //paints the block with the variables generated in the random methods
       */
      public void drawMe(Graphics2D g2) {

         g2.setColor(color);
         //Block itself
         g2.fill(new Rectangle2D.Double(blockXLocation, blockYLocation, blockSize, blockSize));

         //Outline around the block
         g2.setColor(Color.BLACK);
         g2.setStroke(new BasicStroke((float) 2.5));
         g2.draw(new Rectangle2D.Double(blockXLocation, blockYLocation, blockSize, blockSize));
         g2.draw(new Line2D.Double(blockXLocation, blockYLocation + 1, blockXLocation + blockSize, blockYLocation + 1));

         //Happy face
         g2.setColor(Color.BLACK);
         //First eye
         g2.fillOval(blockXLocation + (int) (blockSize * 0.25), (blockYLocation + blockSize) - (int) (0.3 * blockSize), blockSize / 10, blockSize / 10);

         //Second eye
         g2.fillOval(blockXLocation + (int) (0.6 * blockSize), (blockYLocation + blockSize) - (int) (0.3 * blockSize), blockSize / 10, blockSize / 10);

//         if (rand.nextInt() % 2 == 0) {
         //Unhappy Block
//         g2.fillArc(blockXLocation + (int) (blockSize * 0.25), blockYLocation + (int) (blockSize * 0.5), (int) (blockSize * 0.3), (int) (blockSize * 0.2), 0, -180);
//         } else {
//
//            //Smile
         g2.fillArc(blockXLocation + (int) (0.25 * blockSize), (blockYLocation + blockSize) - (int) (blockSize * 0.6), (int) (blockSize * 0.3), (int) (blockSize * 0.2), 0, 180);
//         }
      }

      public void update() {

         //changes the blocks xLocation and fallSpeed if the created block reaches the bottom of the screen
         if (blockYLocation <= 0) {
            blockXLocation = generateRandomXLocation();
            fallSpeed = generateRandomFallSpeed();
            blockSize = generateRandomBlockSize();
            blockYLocation = height;
         }

         //moves the block down if the block is inside the window
         if (blockYLocation >= 0) {
            blockYLocation -= fallSpeed;
         }
//         if (blockXLocation >= height) {
//            blockXLocation = 0;
//            fallSpeed = generateRandomFallSpeed();
//            blockSize = generateRandomBlockSize();
//            blockYLocation = 0;
//         }
//
//         if (blockXLocation <= height) {
//            blockXLocation += fallSpeed;
//         }
      }
   }
}
