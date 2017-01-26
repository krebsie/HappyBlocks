/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import backend.Game;
import backend.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import network.NetworkException;
import network.NetworkInterface;

/**
 * The NewGameUI is a frame that allows the user to begin a new game. Options
 * such as players names and colors can be selected.
 *
 * @author Rob
 */
public class NewGameDialog extends JDialog {

   protected final int MAXNUMBEROFPLAYERS = 4;
   protected final int MAXGRIDSIZE = 20;
   protected final int MINGRIDSIZE = 6;

   private LinkedList<Player> players;

   private NewGamePlayerPanel[] playerPanels;
   private Game storedGame;
   private boolean gamePrepared = false;
   private final JComboBox cboGridSizes;
   private boolean networkGame = false;

   private JButton btnOk = new JButton("Start");
   private JButton btnCancel = new JButton("Cancel");
   private JCheckBox chkNetwork = new JCheckBox("Network Game");
   private JRadioButton rdoHost = new JRadioButton("Host Game");
   private JRadioButton rdoJoin = new JRadioButton("Join Game");
   private JButton btnNetwork = new JButton();
   private JTextField txtIP = new JTextField();
   private JTextField txtPort = new JTextField();
   private NetworkInterface ni;
   private Thread niThread;

   /**
    * Constructor for new game UI. Constructs the GUI representation.
    *
    * @param game Game to work with.
    */
   public NewGameDialog(Game game) {
      instantiateDataMembers(game);

      //SETUP GUI
      this.setPreferredSize(new Dimension(650, 500));
      this.setTitle("New Game");
      this.setLayout(new BorderLayout());

      //GridSize Combo Box
      cboGridSizes = new JComboBox();
      cboGridSizes.setPreferredSize(new Dimension(80, 20));
      for (int i = 6; i <= MAXGRIDSIZE; i++) {
         cboGridSizes.addItem(i);

      }
      cboGridSizes.setPreferredSize(new Dimension(80, 20));

      //Radio Buttons
      ButtonGroup hostjoinGroup = new ButtonGroup();
      hostjoinGroup.add(rdoHost);
      hostjoinGroup.add(rdoJoin);

      //Buttons
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setPreferredSize(new Dimension(200, 50));
      btnOk.setPreferredSize(new Dimension(80, 30));
      btnCancel.setPreferredSize(new Dimension(80, 30));
      chkNetwork.setPreferredSize(new Dimension(120, 30));

      buttonPanel.add(btnOk);
      buttonPanel.add(btnCancel);

      btnOk.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            prepareGame();
            if (!networkGame) {
               ni.shutdownNetwork();
            }
            gamePrepared = true;
            int counter = 1;
            if (networkGame) {
               for (int i = 1; i < players.size(); i++) {
                  Player p = players.get(i);
                  if (p.getPlayerType() != Player.DISABLED) {
                     counter++;
                  }
               }

               Player[] playerArray = new Player[counter];
               int gridsize = (Integer) cboGridSizes.getSelectedItem();
               for (int i = 0; i < counter; i++) {
                  playerArray[i] = players.get(i);
               }
               ni.sendNewGame((int) cboGridSizes.getSelectedItem(), playerArray);
               storedGame.setNI(ni);
            }
            closeWindow();
         }
      });

      btnCancel.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            storedGame = null;
            if (ni != null) {
               ni.shutdownNetwork();
            }
            closeWindow();
         }
      });

      chkNetwork.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            networkGame = chkNetwork.isSelected();
            if (!networkGame & ni != null) {
               ni.shutdownNetwork();
            }
            refreshUI();
         }
      });

      rdoHost.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            refreshUI();
         }
      });

      rdoJoin.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            refreshUI();
         }
      });

      btnNetwork.addActionListener(makeNetworkButtonListener());

      this.add(makeTitlePanel(), BorderLayout.NORTH);
      this.add(makeCenterPanel(), BorderLayout.CENTER);
      this.add(buttonPanel, BorderLayout.SOUTH);
      this.pack();
   }

   private void instantiateDataMembers(Game game) {
      //Instantiate Data Members
      players = new LinkedList<>();
      Random rand = new Random();
      int randomInt = rand.nextInt(8);
      NewGamePlayerPanel staticPP
          = new NewGamePlayerPanel(new Player("", Color.BLACK), -1);

      players.add(new Player("P1", staticPP.getColor((randomInt + 0) % 8)));
      players.add(new Player("P2", staticPP.getColor((randomInt + 1) % 8)));
      players.add(new Player("P3", staticPP.getColor((randomInt + 2) % 8)));
      players.add(new Player("P4", staticPP.getColor((randomInt + 3) % 8)));
      storedGame = game;
   }

   private JPanel makeTitlePanel() {
      JPanel titlePanel = new JPanel(new FlowLayout());
      JLabel titleLabel = new JLabel("Happy Blocks!");
      titleLabel.setFont(new Font("HBlocks1", Font.BOLD, 36));
      titlePanel.add(titleLabel);
      return titlePanel;
   }

   private JPanel makeCenterPanel() {
      GridBagLayout g1 = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
      JPanel centerPanel = new JPanel(g1);

      //Game Options Area
      JPanel optionsPanel = new JPanel();
      optionsPanel.setPreferredSize(new Dimension(150, 280));
      optionsPanel.setLayout(new GridBagLayout());
      GridBagConstraints cO = new GridBagConstraints();
      cO.ipady = 10;
      cO.gridwidth = 5;
      cO.gridx = 0;
      cO.gridy = 0;
      cO.weightx = 1.;
      cO.anchor = GridBagConstraints.LINE_START;
      cO.fill = GridBagConstraints.HORIZONTAL;

      optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
      optionsPanel.add(new JLabel("Grid Size:"), cO);
      cO.gridy++;
      cboGridSizes.setSelectedIndex(10);
      optionsPanel.add(cboGridSizes, cO);
      cO.gridy++;
      optionsPanel.add(chkNetwork, cO);

      cO.gridy++;
      optionsPanel.add(rdoHost, cO);
      cO.gridy++;
      optionsPanel.add(rdoJoin, cO);

      cO.weightx = 0.2;
      cO.gridwidth = 1;
      cO.gridy++;
      optionsPanel.add(new JLabel("IP:"), cO);
      cO.gridx = 1;
      cO.weightx = 0.8;
      cO.gridwidth = 4;
      optionsPanel.add(txtIP, cO);
      cO.gridx = 0;
      cO.gridy++;
      cO.gridwidth = 1;
      cO.weightx = 0.2;
      optionsPanel.add(new JLabel("Port:"), cO);
      cO.gridx = 1;
      cO.gridwidth = 4;
      cO.weightx = 0.8;
      optionsPanel.add(txtPort, cO);
      txtPort.setText("31333");

      cO.gridx = 0;
      cO.gridy++;
      cO.gridwidth = 5;
      optionsPanel.add(btnNetwork, cO);

      //Player Panels code
      JPanel mainPlayerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      mainPlayerPanel.add(optionsPanel);
      playerPanels = new NewGamePlayerPanel[4];
      for (int i = 0; i < playerPanels.length; i++) {
         playerPanels[i] = new NewGamePlayerPanel(players.get(i), i);
         mainPlayerPanel.add(playerPanels[i]);
      }

      //FINAL ADDS TO MAIN PANEL
      c.gridwidth = 1;
      c.gridx = 0;
      c.gridy = 0;
      c.gridx++;
      centerPanel.add(mainPlayerPanel, c);

      rdoHost.setSelected(true);
      refreshUI();
      return centerPanel;
   }

   //Returns a color
   private Color changePlayerColor(int i) {
      return JColorChooser.showDialog(this,
          "Choose Player Color", players.get(i).getColor());
   }

    //Prepare the storedGame member using a new game and the data about the
   //players.  Called by the Ok button.
   private void prepareGame() {
      LinkedList<Integer> enabledPlayers = new LinkedList<Integer>();
      int selectedNumberPlayers = 0;
      for (int i = 0; i < this.MAXNUMBEROFPLAYERS; i++) {
         if (!playerPanels[i].getPlayerType().equals("Disabled")) {
            selectedNumberPlayers++;
            enabledPlayers.add(i);
         }
      }

      Player[] playerArray = new Player[selectedNumberPlayers];
      for (int i = 0; i < selectedNumberPlayers; i++) {
         playerArray[i] = players.get(enabledPlayers.get(i));
      }
      int gridsize = (int) this.cboGridSizes.getSelectedItem();

      storedGame.gmvBeginNewGame(gridsize, playerArray);
   }

   //Close the window.  Called by the stored Ok and Cancel buttons.
   public void closeWindow() {
      this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
   }

   private void refreshUI() {

      btnOk.setEnabled(!networkGame);
      txtIP.setEnabled(networkGame);
      txtPort.setEnabled(networkGame);
      rdoHost.setEnabled(networkGame);
      rdoJoin.setEnabled(networkGame);
      btnNetwork.setEnabled(networkGame);

      if (networkGame) {
         if (rdoHost.isSelected()) {
            btnNetwork.setText("Listen");
            try {
               txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
            } catch (UnknownHostException uex) {
               txtIP.setText("error");
            }
            for (int i = 1; i < playerPanels.length; i++) {
               playerPanels[i].setVisible(true);
            }
            playerPanels[0].panelHostWaiting();

         } else {
            for (int i = 1; i < playerPanels.length; i++) {
               playerPanels[i].setVisible(false);
            }
            playerPanels[0].panelJoinWaiting();
            btnNetwork.setText("Join");
            txtIP.setText("127.0.0.1");
         }
      } else {
         btnNetwork.setText("Listen");
         txtIP.setText("");
         for (int i = 0; i < playerPanels.length; i++) {
            playerPanels[i].panelNetworkOff();
         }
      }

   }

   /**
    * @return the storedGame
    */
   public boolean gamePrepared() {
      return gamePrepared;
   }

   private ActionListener makeNetworkButtonListener() {
      return (ActionEvent ae) -> {
         ni = new NetworkInterface(storedGame, this);
         niThread = new Thread(ni);
         String ip = txtIP.getText();
         int port = -1;
         try {
            port = Integer.parseInt(txtPort.getText());
         } catch (NumberFormatException ex) {
            new NetworkException("Invalid text in port field");
            return; //bail
         }

         //Start up network
         ni.initializeNetwork(rdoHost.isSelected(), ip, port);
         niThread.start();

      };
   }

   public Player getPlayerOne() {
      return players.get(0);
   }

   public void setupNetworkPlayer(Player player) {
      playerPanels[1].setNetworkConnected(player);
      playerPanels[0].panelHostReady();
      btnOk.setEnabled(true);
   }

   /**
    * Called by the NetworkClient when it connects to a server.
    */
   public void notifyJoinConnected() {
      playerPanels[0].panelJoinConnected();
   }

   /**
    * Called by the NetworkClient when it attempts to connect to a server.
    */
   public void notifyJoinConnecting() {
      playerPanels[0].panelJoinConnecting();
   }

   /**
    * Called by the NetworkClient when it is fails to connect, or when the
    * player presses joins/.
    */
   public void notifyJoinWaiting() {
      playerPanels[0].panelJoinWaiting();
   }

   /**
    * Called by the Network Interface when the server is listening but not
    * connected to a client.
    */
   public void notifyHostListening() {
      playerPanels[0].panelHostListeneing();
      playerPanels[1].panelHostClientDisconnected();
      btnOk.setEnabled(false);
   }

   /**
    * Called by the Network Interface when the server is selected but not
    * actively listening.
    */
   public void notifyHostWaiting() {
      playerPanels[0].panelHostWaiting();
      playerPanels[1].panelHostClientDisconnected();
      refreshUI();
   }

}
