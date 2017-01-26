/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.util.LinkedList;

/**
 * Representation of the current state of the game board. Contains the
 * information relevant to the current location of the pieces on the game board.
 *
 * @author Rob
 */
public class State {

   //Data Members
   private int n;  //Size of the board.
   private GamePiece[][] pieces;    //Array of objects for the state.
   private boolean boardFull; //Flag for a full board.
   private int countBlocks; //Maintains a count of the number of dropped blocks.

   /**
    * Default constructor for state. Initializes the game board to an all empty
    * position.
    *
    * @param n The size of the game board in n x n parameters.
    */
   State(int n) {
      this.n = n;
      pieces = new GamePiece[n][n];
      boardFull = false;
      countBlocks = 0;
   }

   //======GAMEPIECE INFORMATION==========
   /**
    * Returns the Player represented by the piece in the square.
    *
    * @param x Horizontal coordinate on the game board.
    * @param y Vertical coordinate on the game board.
    * @return Player. Returns a Player if a piece exists in the game board,
    * returns null if there is no piece;
    */
   Player getPlayerInSquare(int x, int y) {
      Player p;

      try {
         p = pieces[x][y].getPlayer();
      } catch (Exception ex) {
         p = null;
      }

      return p;
   }

   /**
    * Returns the scored state of the piece in the square.
    *
    * @param x Horizontal coordinate on the game board.
    * @param y Vertical coordinate on the game board.
    * @return True if the state is scored, false if not.
    */
   boolean getScoredState(int x, int y) {
      return (pieces[x][y].isScored());
   }

   /**
    * Returns the flag for a full board. Should be accessed in game class after
    * dropping a piece to check if the game is over.
    *
    * @return State of the board being full.
    */
   boolean isBoardFull() {
      return boardFull;
   }

   //======MOVES============
   /**
    * Attempts to put a piece in on the top empty row of the game board.
    *
    * @param column column index to attempt a drop.
    * @param player player who owns the piece.
    * @return true if drop was successful, false if not (illegal move).
    */
   boolean dropPieceInColumn(int column, Player player)
       throws IllegalMoveException, CriticalException {
      int row = -1;
      boolean topRow = false;

      //Error handling for outside column bounds.
      if (column >= n) {
         Integer errorColumn = column + 1;
         Integer errorBoardSize = n;
         throw new CriticalException("User attempted to drop a piece in"
             + " column " + errorColumn.toString() + ", but board is "
             + errorBoardSize.toString() + " columns wide.");
      }

      //Error handling for dropping onto a full board.
      if (boardFull) {
         throw new CriticalException("User attempted to drop a piece, "
             + "but the board is full.");
      }

      try {
         row = bottomEmptyRow(column);
         if (row <= n) {
            if (pieces[row][column] != null) {
               Integer errorColumn = column + 1;
               Integer errorRow = row + 1;
               Integer errorBoardSize = n;
               throw new CriticalException("In column " + errorColumn.toString()
                   + ", row " + errorRow.toString() + ", a piece was overwritten.");
            }

            topRow = true;
            GamePiece gp = new GamePiece(player, row, column);
            gp.setID(countBlocks);
            pieces[row][column] = gp;
            countBlocks++;
         }
      } catch (ArrayIndexOutOfBoundsException ex) {
         Integer errorColumn = column + 1;
         throw new IllegalMoveException("Player:" + player.getName()
             + " - Attempted to drop piece into a full column: "
             + errorColumn.toString());
      }

      //check for full board.
      if (countBlocks >= (n * n)) {
         boardFull = true;
      }

      return topRow;
   }

   boolean spinBoardCounterClockwise() {
      //Begin spin board.
      GamePiece[][] tempPieces = new GamePiece[n][n];

      //Make a temporary holding variable
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            tempPieces[i][j] = pieces[i][j];
         }
      }

      //Spin the pieces
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            pieces[i][j] = tempPieces[n - j - 1][i];
         }
      }
      resetPreTumbleIndecies();
      tumble();
      resetPieceIndecies();      

      return true;
   }

   boolean spinBoardClockwise() {
      //Begin spin board.
      GamePiece[][] tempPieces = new GamePiece[n][n];

      //Make a temporary holding variable
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            tempPieces[i][j] = pieces[i][j];
         }
      }

      //Spin the pieces
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            pieces[i][j] = tempPieces[j][n - 1 - i];
         }
      }
      resetPreTumbleIndecies();
      tumble();
      resetPieceIndecies();

      return true;
   }

   private void tumble() {
      //tumble the pieces
      for (int j = 0; j < n; j++) {
         LinkedList<Integer> empties = new LinkedList<Integer>();
         for (int i = 0; i < n; i++) {
            if (pieces[i][j] == null) {
               empties.add(i);
            }
            if (pieces[i][j] != null && !empties.isEmpty()) {
               pieces[empties.removeFirst()][j] = pieces[i][j];
               empties.add(i);
               pieces[i][j] = null;
            }
         }
      }
   }
   
    /**
    * Resets the pieces row pre-tumble indecies for use in the HappyBlock class.
    */
   private void resetPreTumbleIndecies() {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            if(pieces[i][j] != null) {
                pieces[i][j].setPreTumbleRow(i);
            }
         }
      }
   }
   
   /**
    * Resets the pieces row and column indecies for use in the HappyBlock class.
    */
   private void resetPieceIndecies() {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            if(pieces[i][j] != null) {
                pieces[i][j].setColumn(j);
                pieces[i][j].setRow(i);
            }
         }
      }
   }

   /**
    * Returns the top empty row in a column. Used by the drop piece in column
    * method in state as well as possibly other methods.
    *
    * @param column Column to search.
    * @return the bottom empty row index, or -1 if row is full.
    */
   int bottomEmptyRow(int column) throws CriticalException {
      if (column >= n) {
         Integer errorColumn = column + 1;
         Integer errorBoardSize = n;
         throw new CriticalException("CPU attempted to check on "
             + " column " + errorColumn.toString() + ", but board is "
             + errorBoardSize.toString() + " columns wide.");
      }

      int row = 0;
      try {
         boolean topRow = false;
         while (!topRow && row < n) {
            if (pieces[row][column] == null) {
               topRow = true;
               return row;
            }
            row++;
         }
         return -1;
      } catch (ArrayIndexOutOfBoundsException ex) {
         Integer errorRow = row + 1;
         Integer errorBoardSize = n;
         throw new CriticalException("While searching for a row, the CPU "
             + "attempted to find row " + errorRow + " but board is "
             + errorBoardSize.toString() + " rows tall.");
      }

   }

   //=========SCORING============
   /**
    * (Not Yet Implemented) This method scores the current state of the board.
    */
   void scoreBoard() {
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            //Loop through each board spot.
            Player p = getPlayerInSquare(i, j);
            if (p != null) {
               //A piece exists in that spot
               if (!getScoredState(i, j)) {
                  //Piece has not already been scored.
                  //Begin checking
                  //Check above.
                  boolean itsRight = true;
                  for (int k = 1; k < 4 && itsRight; k++) {
                     if (p.equals(getPlayerInSquare(i, j + k))) {
                        if(getScoredState(i, j+k)) {
                            //Piece is scored, set false.
                            itsRight = false;
                        }
                     } else {
                        //Above piece doesnt exist or is different.
                        itsRight = false;
                     }
                  }

                  boolean itsUp = true;
                  for (int k = 1; k < 4 && itsUp; k++) {
                     if (p.equals(getPlayerInSquare(i + k, j))) {
                        //Player to the right is the same do nothing.
                        if(getScoredState(i + k, j)) {
                            //Piece is scored, set false.
                            itsUp = false;
                        }                         
                     } else {
                        //right piece doesnt exist or is different.
                        itsUp = false;
                     }
                  }

                  //Looked at up and right.
                  if (itsRight) {
                     //The vertical four match.
                     //Set the score states
                     for (int k = 0; k < 4; k++) {
                        pieces[i][j + k].setScored(true);
                     }
                     //Score one for the player
                     p.setScore(p.getScore() + 1);
                  }

                  if (itsUp) {
                     //The horizontal four match.
                     //Set the score states
                     for (int k = 0; k < 4; k++) {
                        pieces[i + k][j].setScored(true);
                     }
                     //Score one for the player
                     p.setScore(p.getScore() + 1);
                  }
               }
            }
         }
      }
   }

   public int getCountBlocks() {
      return this.countBlocks;
   }

   int getIDInSquare(int row, int column) {
      GamePiece gp = pieces[row][column];
      if (gp != null) {
         return gp.getID();
      } else {
         return -1;
      }
   }
   
   public GamePiece getGamePiece(int row, int column) {
       return pieces[row][column];
   }
}
