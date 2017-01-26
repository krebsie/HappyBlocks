/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.awt.Color;
import java.util.LinkedList;
import network.NetworkInterface;
import ui.NewGamePlayerPanel;
import ui.UserInterface;

/**
 * Represents the game being played. The game class is the "main" class of the
 * application. It contains the specific state of the board and the players
 * playing it.
 *
 * @author Rob
 */
public class Game {
    //Data Members
    private LinkedList<Player> players;
    private Player curPlayer;
    private State state;
    private int n; // size of the grid
    private int moves; // integer representation of the number of moves performed.
    private boolean gameover = false;
    private UserInterface ui;
    private boolean gamestatus = false;
    private NetworkInterface ni;
    
    /**
     * The default constructor for a new game.
     * @param n The size of the grid.
     * @param playerArray An array of players to use.
     */
    public Game(int n, Player[] playerArray) {
        players = new LinkedList<Player>();
        gmvBeginNewGame(n, playerArray);
    }
    
    /**
     * An initializing constructor for the game.
     */
    public Game(UserInterface ui) {
        players = new LinkedList<Player>();
        this.ui = ui;
    }
    
    /**
     * The default constructor for a new game.
     * @param n The size of the grid.
     * @param playerArray An array of players to use.
     */
    public Game(int n, Player[] playerArray, UserInterface ui) {
        players = new LinkedList<Player>();
        gmvBeginNewGame(n, playerArray);
        this.ui = ui;
    }    
    
    
   //========UTILITY METHODS============
   /**
    * Utility method for advancing the turn to the next player in the list.
    * Method should be called after most "game moves".
    */
   private void nextPlayer() {
      moves++;
      curPlayer = players.get(moves % players.size());

      //If Neccessary, make a CPU move.
      if (curPlayer.getPlayerType() == Player.AIENABLED && !gameover) {
         makeAIMove();
      }
   }

   /**
    * Score the board, finding new chains.
    */
   private void score() {
      state.scoreBoard();
   }

   /**
    * Flips the gameover flag if the board is one space away from full. That way
    * the next dropPiece will flag as over AFTER the piece is dropped.
    */
   private void checkGameOver() {
      gameover = state.isBoardFull();
   }

   /**
    * Make an AI move.
    */
   private void makeAIMove() {

      try {
         curPlayer.getAI().makeCPUMove();
      } catch (CriticalException cex) {
         score();
         checkGameOver();
         ui.updateGUIAfterMove();         
         nextPlayer();
      } catch (InterruptedException iex) {

      }

   }

   //=======PUBLIC GAME ACTIONS========
   /**
    * A "game move" method for dropping a piece in the game board. The game
    * state will be updated to reflect an additional piece added to the lowest
    * numbered open row in that column.  This method is called from the
    * constructor.
    *
    * @param column The column number to attempt a piece drop.
    * @return True if the move is legal, false if otherwise.
    */
   public boolean gmvDropPiece(int column) {
       return doDropPiece(column, false);
   }
   
    /**
    * A "game move" method for dropping a piece in the game board. The game
    * state will be updated to reflect an additional piece added to the lowest
    * numbered open row in that column.  This method is called from an XferAction
    *
    * @param column The column number to attempt a piece drop.
    * @return True if the move is legal, false if otherwise.
    */
   public boolean gmvDropPiece(int column, boolean remote) {
       return doDropPiece(column, remote);
   }

   private boolean doDropPiece(int column, boolean remote) {
      boolean legalMove = false;

      if(curPlayer.getPlayerType() == Player.NETWORK && !remote) 
          return false;
      
      
      try {
         if (state.dropPieceInColumn(column, curPlayer)) {
            legalMove = true;
            score();
            checkGameOver();
            ui.updateGUIAfterMove();
            nextPlayer();
                try {
                    if(!remote) ni.sendDropPiece(column);
                } catch (NullPointerException ex) {
                    //Do nothing if ni is null.
                    //If ni is null then there is no network game in progress.
                }
         }
      } catch (IllegalMoveException imex) {
         return false;
      } catch (CriticalException critx) {
         return false;
      }
      

      
      return legalMove; 
   }
   
   /**
    * This method begins a new game.
    *
    * @param n the current board size.
    * @param playerArray the array of players.
    */
   public void gmvBeginNewGame(int n, Player[] playerArray) {
      this.n = n;
      moves = 0;
      players.clear();

      LinkedList<Color> usedColors = new LinkedList<>();
      
      //Modify for duplicate colors.
      for (int i = 0; i < playerArray.length; i++) {
          Player p = playerArray[i];
          while(true) {
              if(usedColors.contains(p.getColor())) {
                  p.setColor(NewGamePlayerPanel.getNextColor(p.getColor()));
              } else {
                  usedColors.add(p.getColor());
                  break;
              }
          }
          
          //Add the AI
          if(p.getPlayerType() == Player.AIENABLED) {
              p.setAI(new AI(AI.DIFFICULTY_EASY, this));
          }
          players.add(p);
      }
      

      gamestatus = true;
      state = new State(n);
      curPlayer = players.get(moves);
      if(ui != null) ui.updateGUIForNewGame();
   }

   /**
    * This method does a board spin as a player move.
    *
    * @param clockwise Whether the spin was done in a clockwise our
    * counterclockwise direction.
    */
   public boolean gmvSpinBoard(boolean clockwise) {
       return doSpinBoard(clockwise, false);
   }

    /**
    * This method does a board spin as a player move.
    *
    * @param clockwise Whether the spin was done in a clockwise our
    * counterclockwise direction.
    */
   public boolean gmvSpinBoard(boolean clockwise, boolean remote) {
       return doSpinBoard(clockwise, remote);
   }  
   
   private boolean doSpinBoard(boolean clockwise, boolean remote) {
      if(curPlayer.getPlayerType() != Player.HUMAN && !remote) 
          return false; 
       
      boolean legal = true;
      if (clockwise) {
         legal = state.spinBoardClockwise();
      } else {
         legal = state.spinBoardCounterClockwise();
      }
      score();
      ui.updateGUIAfterMove();
      nextPlayer();
      
      try {
          if(!remote) ni.sendSpinBoard(clockwise);
      } catch (NullPointerException ex) {
          //Do nothing if ni is null.
          //If ni is null then there is no network game in progress.
      }
      return legal;
   }

   //=======PUBLIC ACCESSOR METHODS=========
   /**
    * Gets the color of playing piece in a row or column.
    *
    * @param row Row index to be searched.
    * @param column Column index to be searched.
    * @return Color of the game piece in the row and column. If no game piece
    * exists in that spot, null is returned.
    */
   public Color getColorOfGamePiece(int row, int column) {
      Player p = state.getPlayerInSquare(row, column);

      if (p != null) {
         return p.getColor();
      }

      return null;
   }

   public int getIDOfGamePiece(int row, int column) {
      return state.getIDInSquare(row, column);
   }

   /**
    * Returns the current state of a board space (occupied or vacant).
    *
    * @param row Row index to be searched.
    * @param column Column index to be searched.
    * @return True if the current row and column is occupied with a game piece,
    * false if vacant.
    */
   public boolean isBoardSpaceOccupied(int row, int column) {
      if (state == null) {
         return false;
      }

      if (state.getPlayerInSquare(row, column) == null) {
         return false;
      }

      return true;
   }

   /**
    * Returns a flag for whether this piece has been scored. Note, if a piece is
    * not present in that location, false will be returned.
    *
    * @param row Row index to be searched.
    * @param column Column index to be searched.
    * @return True if the piece in the location is scored. False if not scored
    * or no game piece exists.
    */
   @Deprecated
   public boolean isGamePieceScored(int row, int column) {
      try {
         return state.getScoredState(row, column);
      } catch (NullPointerException e) {
         return false;
      }
   }

   public GamePiece getGamePiece(int row, int column) {
      return state.getGamePiece(row, column);
   }

   /**
    * Returns the player color of the specified index of player.
    *
    * @param i The index of the player to search for.
    * @return The color of the specified player, or null if the player index is
    * out of range.
    */
   public Color getPlayerColor(int i) {
      try {
         return players.get(i).getColor();
      } catch (IndexOutOfBoundsException nex) {
         return null;
      }
   }

   /**
    * Returns the name of the specified index of player.
    *
    * @param i The index of the player to search for.
    * @return The name of the specified player, or null if the player index is
    * out of range.
    */
   public String getPlayerName(int i) {
      try {
         return players.get(i).getName();
      } catch (IndexOutOfBoundsException nex) {
         return null;
      }
   }

   /**
    * Returns the current score of the specified index of player.
    *
    * @param i The index of the player to search for.
    * @return The score of the specified player, or -1 if the player index is
    * out of range.
    */
   public int getPlayerScore(int i) {
      try {
         return players.get(i).getScore();
      } catch (IndexOutOfBoundsException nex) {
         return -1;
      }
   }

   /**
    * Gets the number of players in the active game.
    *
    * @return Number of players in the game.
    */
   public int getNumberOfPlayers() {
      return players.size();
   }

   /**
    * Get the current board size.
    *
    * @return The size of the board (n x n).
    */
   public int getBoardSize() {
      return this.n;
   }

   /**
    * Gets the status of the game.
    *
    * @return True if the game is over. False if it continues.
    */
   public boolean getGameOver() {
      return gameover;
   }

   /**
    * Gets the index of the current player.
    *
    * @return the index of the current player, or -1 if no current player is
    * selected.
    */
   public int getCurPlayerIndex() {
      for (int i = 0; i < players.size(); i++) {
         if (players.get(i).equals(curPlayer)) {
            return i;
         }
      }
      return -1;
   }

   /**
    * Set the current UI instance for the game.
    */
   public void setUI(UserInterface ui) {
      this.ui = ui;
   }

   /**
    * Gets a flag telling whether the game is enabled, that is whether there is
    * a game in progress.
    */
   public boolean getGameStatus() {
      return gamestatus;
   }

   /**
    * Method for getting the bottom most empty row in a column.
    *
    * @param column
    * @return The bottom most empty row in a column, or -1 if row is full.
    */
   public int getBottomEmptyRow(int column) {
      try {
         int row = state.bottomEmptyRow(column);
         return row;
      } catch (CriticalException cex) {
         return -1;
      }

   }

   public int getNumberOfPieces() {
      if (state != null) {
         return state.getCountBlocks();
      } else {
         return -1;
      }
   }

    
    /**
     * Gets the network interface instance for the UI elements to interact with.
     */
    public NetworkInterface getNI() {
        return ni;
    }
    
    /**
     * Sets the network interface.
     */
    public void setNI(NetworkInterface ni) {
        this.ni = ni;
    }

    /**
     * Gets the current status of the player
     * @param i the index of player to return
     * @return players status
     */
    public int getPlayerType(int i) {
        return players.get(i).getPlayerType();
    }
    
    /**
     * Terminates the current game by setting the active flag to false.
     */
    public void abortGame() {
        this.gamestatus = false;
        ui.updateGUIForNewGame();
    }
}
