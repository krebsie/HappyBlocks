/*
 *
 */
package graphics;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

/**
 * Graphical clouds that are drawn on the Board.
 *
 * @author LauraKrebs
 */
public class Cloud {

   private ArrayList<Point2D.Double> cloud;
   private Board board;
   private int height, xLocation;
   private Random rand = new Random();
   private int cloudSize = 40;
   private int fallSpeed = 1;

   public Cloud(Board board, int h) {
      cloud = new ArrayList<Point2D.Double>();
      this.board = board;
      this.height = h;
   }
   
   public Cloud(int h) {
      cloud = new ArrayList<Point2D.Double>();
      this.board = board;
      this.height = h;
   }
   

   public void add(Point2D.Double aPoint) {
      cloud.add(aPoint);
   }

   /**
    * This method is called in the paintComponent method of the Board class. The
    * graphics context that is passed in is the one created in the Board class.
    *
    * @param g2
    */
   public void draw(Graphics2D g2) {
      for (int i = 0; i < getNumberOfClouds(); i++) {
         Ellipse2D.Double smallCircle = new Ellipse2D.Double(cloud.get(i).getX(), cloud.get(i).getY(), 100, 40);
         g2.fill(smallCircle);
      }
   }

   public ArrayList<Point2D.Double> getCloud() {
      return this.cloud;
   }

   public int generateRandomXLocation() {
      if (rand.nextInt(height - cloudSize) <= 0) {
         while (rand.nextInt(height - cloudSize + 1) <= 0) {
            xLocation = rand.nextInt(height - cloudSize + 1);
         }
      } else {
         xLocation = rand.nextInt(height - cloudSize);
      }
      return xLocation;
   }

   public int generateRandomFallSpeed() {
      return rand.ints(1, 1, 10).findFirst().getAsInt();
   }

   public void update() {

      for (Point2D.Double cloudPoint : cloud) {
         if (cloudPoint.getY() >= height) {
            cloudPoint.x = generateRandomXLocation();
            cloudPoint.y = 0;
         }

         if (cloudPoint.getY() <= height) {
            cloudPoint.y += fallSpeed;
         }
      }

   }
   
   public int getNumberOfClouds() {
       return cloud.size();
   }
}
