/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import backend.CriticalException;
import backend.Game;
import backend.Player;
import ui.NewGameDialog;

/**
 * Class represents a games' connection to the outside world. Implements
 * runnable so it can be multi-threaded and listen.
 *
 * @author Rob
 */
public class NetworkInterface implements Runnable {
   //Static variables for Network Status.
   //Currently unused but may be useful in the future.

   /**
    * public static int STATUSOFF = -1; public static int STATUSWAITING = 0;
    * public static int STATUSCONNECTING = 1; public static int STATUSCONNECTED
    * = 2; private int status = STATUSOFF;
    */
   //Data Members
   private NetworkPlayer networkPlayer;
   private Game game;
   private NewGameDialog nui;

   /**
    * Constructor for the interface.
    *
    * @param game Game to perform network actions on.
    * @param nui The user interface used to setup the network game.
    */
   public NetworkInterface(Game game, NewGameDialog nui) {
      this.game = game;
      this.nui = nui;

   }

   /**
    * Tells the client / server to check for connections. When a connection is
    * established, the client / server will begin listening.
    *
    * @throws network.NetworkException
    */
   @Override
   public void run() {
      try {
         nui.notifyJoinConnecting();
         networkPlayer.start();
      } catch (NetworkException nex) {
         game.abortGame();
         if (networkPlayer.getClass() == NetworkClient.class) {
            nui.notifyJoinWaiting();
         } else if (networkPlayer.getClass() == NetworkHost.class) {
            nui.notifyHostWaiting();
         } else {
            new CriticalException("Network player identified itself as neither"
                + " a client or a server.");
         }
      }
   }

   //=========== XFER ACTION METHODS =============
   /**
    * Sends out a Drop Piece command.
    *
    * @param column the column to drop in.
    */
   public void sendDropPiece(int column) {
      XferDropPiece xfer = new XferDropPiece(column);
      networkPlayer.sendOutput(xfer);
   }

   /**
    * Executes a drop piece command. Method is called by the XferDropPiece when
    * one is received from the network interface.
    *
    * @param column the column to drop in
    */
   public void executeDropPiece(int column) {
      game.gmvDropPiece(column, true);
   }

   /**
    * Sends out a Spin Board command.
    *
    * @param clockwise whether or not to spin clockwise (false is counter-
    * clockwise spin).
    */
   public void sendSpinBoard(boolean clockwise) {
      XferSpinBoard xfer = new XferSpinBoard(clockwise);
      networkPlayer.sendOutput(xfer);
   }

   /**
    * Executes a drop piece command. Method is called by the XferSpinBoard when
    * one is received from the network interface.
    *
    * @param clockwise whether or not to spin clockwise (false is counter-
    * clockwise spin).
    */
   public void executeSpinBoard(boolean clockwise) {
      game.gmvSpinBoard(clockwise, true);
   }

   /**
    * Sends out a begin new game command.
    *
    * @param gridsize the size of the grid selected by the host.
    * @param players an array of players to send over.
    */
   public void sendNewGame(int gridsize, Player[] players) {
      XferBeginGame xfer = new XferBeginGame(gridsize, players);
      networkPlayer.sendOutput(xfer);
   }

   /**
    * Executes a drop piece command. Method is called by the XferBeginGame when
    * one is received from the network interface.
    *
    * @param gridsize the size of the grid selected by the host.
    * @param players an array of players to send over.
    */
   public void executeNewGame(int gridsize, Player[] players) {
      players[0].setPlayerType(Player.NETWORK);
      players[1].setPlayerType(Player.HUMAN);
      for (int i = 2; i < players.length; i++) {
         if (players[i].getPlayerType() != Player.DISABLED) {
            players[i].setPlayerType(Player.NETWORK);
         }
      }
      game.gmvBeginNewGame(gridsize, players);
      nui.closeWindow();
      game.setNI(this);
   }

   /**
    * Sends out a player joining game command. The player sent is always the
    * first player in the new game ui.
    */
   public void sendJoiningPlayer() {
      Player p = nui.getPlayerOne();
      XferJoinPlayer xfer = new XferJoinPlayer(p);
      nui.notifyJoinConnected();
      networkPlayer.sendOutput(xfer);
   }

   /**
    * Executes a player joining command. Method is called by the XferJoinPlayer
    * when one is received from the network interface.
    *
    * @param player The player class for the joining player.
    */
   public void executeJoiningPlayer(Player player) {
      nui.setupNetworkPlayer(player);
   }

   /**
    * Sends out a player left game command.
    *
    * @param player The player that has left the game.
    */
   public void sendLeftGame(Player[] players) {
      int i = 0;
      for (i = 0; i < players.length; ++i) {
         if (players[i].getPlayerType() != Player.NETWORK) {
            XferLeftGame xfer = new XferLeftGame(players[i].getName());
            networkPlayer.sendOutput(xfer);
         }
      }
      if (i == players.length) {
         XferLeftGame xfer = new XferLeftGame("");
         NetworkException ne = new NetworkException("Error leaving game.");
      }

   }

   /**
    * Executes a player leaving command. Method is called by the XferLeftGame
    * class.
    */
   public void executeLeftGame() {
      //COME BACK TO THIS
   }

   //===========NETWORK SETUP METHODS==============
   /**
    * Instantiates the network player as either a host or client and attempts to
    * establish a connection.
    *
    * @param isHost Flag for whether this user is a host.
    * @param ip The IP address to listen on / connect to.
    * @param port The port to listen on / connect to.
    */
   public void initializeNetwork(boolean isHost, String ip, int port) {
      if (isHost) {
         networkPlayer = new NetworkHost(this, ip, port);
      } else {
         networkPlayer = new NetworkClient(this, ip, port);
      }

   }

   /**
    * Shuts down the network and closes the sockets.
    */
   public void shutdownNetwork() {
      networkPlayer.stop();
   }

   /**
    * Called by the NetworkHost that the server is listening for connections.
    */
   public void notifyServerListening() {
      nui.notifyHostListening();
   }

}
