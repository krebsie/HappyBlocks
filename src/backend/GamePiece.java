/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 * This class represents one square area of the board.
 *
 * @author Rob
 */
public class GamePiece {

   //Data Members

   private Player player;
   private boolean scored;
   private int id;
   private int column;
   private int row;
   private int preTumbleRow = -1;

   /**
    * Default constructor for the GamePiece class.
    *
    * @param player The player whose piece this is.
    */
   public GamePiece(Player player, int row, int column) {
      this.player = player;
      this.row = row;
      this.column = column;
      scored = false;
   }

    //========GETTERS AND SETTERS==========
   /**
    * @return the player
    */
   public Player getPlayer() {
      return player;
   }

   /**
    * @return the scored
    */
   public boolean isScored() {
      return scored;
   }

   /**
    * @param scored the scored to set
    */
   public void setScored(boolean scored) {
      this.scored = scored;
   }

    /**
     * @return the id number
     */   
   public int getID() {
      return this.id;
   }

    /**
     * @param number the id number to set
     */    
   public void setID(int number) {
      this.id = number;
   }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * With package access only, to change the column in State.java
     * @param column the column to set
     */
    void setColumn(int column) {
        this.column = column;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }
    
    /**
     * @return the pre tumble row
     */
    public int getPreTumbleRow() {
        return preTumbleRow;
    }  
    
    /**
     * With package access only, to change the preTumbleRow in State.java
     * @param row the row to set
     */
    void setPreTumbleRow(int row) {
        this.preTumbleRow = row;
    }    

    /**
     * With package access only, to change the column in State.java
     * @param row the row to set
     */
    void setRow(int row) {
        this.row = row;
    }
   

}
