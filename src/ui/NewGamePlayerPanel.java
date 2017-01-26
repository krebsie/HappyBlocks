/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import backend.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class allows for greater usability of the player panels in some of the
 * new game dialog boxes.
 * @author Rob
 */
public class NewGamePlayerPanel extends JPanel {
    private static final Color[] COLORS = {Color.RED, new Color(30,144,255), Color.GREEN,
        Color.YELLOW, new Color(255,128,0), Color.MAGENTA, new Color(0,128,128), Color.PINK
    };
    private Integer playerNumb;
    private Player p;
    
    //Combo Boxes
    private JComboBox cboColors = new JComboBox();
    private JComboBox cboPlayerType = new JComboBox();
    private JTextField txtName = new JTextField();
    
    //Network stuff
    private JLabel lblNetworkStatus = new JLabel();
    
    public NewGamePlayerPanel(Player p, int playerNumber) {
        final int PANELWIDTH = 100;
        final int PANELHEIGHT = 240;
        this.p = p;
        playerNumb = playerNumber + 1;        
        
        initializeTypes();
        initializeColors();
        
        //Set Boxes to relevant names.
        
        GridBagLayout g1 = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();        
        JPanel panel = new JPanel(g1);
        

        panel.setBorder(BorderFactory.createTitledBorder(
                "Player " + playerNumb.toString()));
        panel.setPreferredSize(new Dimension(PANELWIDTH,PANELHEIGHT));
        
        
        //Layout GUI
        c.ipady = 10;        
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx=1.;        
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(new JLabel("Type:"), c);
        c.gridy++;
        panel.add(cboPlayerType, c);
        
        c.gridy++;
        panel.add(new JLabel("Name:"), c);
        c.gridy++;
        c.fill=GridBagConstraints.HORIZONTAL;        
        txtName.setPreferredSize(new Dimension(PANELWIDTH, 20));
        txtName.setColumns(20);
        txtName.setText(p.getName());
        panel.add(txtName, c);
        
        c.gridx = 0;
        c.gridy++;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(new JLabel("Color:"), c);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy++;
        JPanel colorPanel = new JPanel(new GridLayout(2,1,0,0));
        colorPanel.setPreferredSize(new Dimension(PANELWIDTH, 50));
        JLabel lblColor = new JLabel("Select Color");
        panel.add(lblColor, c);
        c.gridy++;
        panel.add(cboColors, c);
        
        c.gridy++;
        panel.add(lblNetworkStatus, c);
        
        txtName.getDocument().addDocumentListener(              
            new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
              p.setName(txtName.getText());
            }
            public void removeUpdate(DocumentEvent e) {
              p.setName(txtName.getText());
            }
            public void insertUpdate(DocumentEvent e) {
              p.setName(txtName.getText());                
            }

          });
        
        this.setLayout(new FlowLayout());
        this.add(panel);
    }
    
    //Sets up the colors
    private void initializeColors() {
        cboColors.addItem("Red");
        cboColors.addItem("Blue");
        cboColors.addItem("Green");
        cboColors.addItem("Yellow");
        cboColors.addItem("Orange");
        cboColors.addItem("Magenta");
        cboColors.addItem("Teal");
        cboColors.addItem("Pink");
        
        cboColors.setSelectedIndex(getIndexByColor(p.getColor()));
        
        cboColors.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                p.setColor(COLORS[cboColors.getSelectedIndex()]);
            }
            
        });
    }
    
    /**
     * Returns the color in the array of the specified index.  Used to randomly
     * select the starting colors for the players.
     * @param i index to get.
     * @return 
     */
    public Color getColor(int i) {
        return COLORS[i];
    }
    
    //Returns the index in the cboColors that the specified color corresponds to.
    private int getIndexByColor(Color c) {
        for(int i = 0; i < COLORS.length; i++) {
            if (c.equals(COLORS[i])) return i;
        }
        return -1;
    }

    private void initializeTypes() {                
        cboPlayerType.addItem("Human");
        cboPlayerType.addItem("CPU");
        cboPlayerType.addItem("Network");
        if(playerNumb != 1) {
            cboPlayerType.addItem("Disabled");
        }
       
        cboPlayerType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if(cboPlayerType.getSelectedItem() == "Disabled") {
                    p.setPlayerType(Player.DISABLED);
                }
                if(cboPlayerType.getSelectedItem() == "Human") {
                    p.setPlayerType(Player.HUMAN);
                }
                if(cboPlayerType.getSelectedItem() == "CPU") {
                    p.setPlayerType(Player.AIENABLED);
                }
                if(cboPlayerType.getSelectedItem() == "Network") {
                    p.setPlayerType(Player.NETWORK);
                }
                typeCheckEnable();
            }
            
        });
        
        if(playerNumb > 2) {
            cboPlayerType.setSelectedItem("Disabled");
        }
    }
    
    private void typeCheckEnable() {
        if (p.getPlayerType() == Player.DISABLED) {
            cboColors.setEnabled(false);
            txtName.setEnabled(false);
        } else {
            cboColors.setEnabled(true);
            txtName.setEnabled(true);
        }
    }
    
    /**
     * Gets the active type of the player for UI purposes.
     * @return Player type as a string
     */
    public String getPlayerType() {
        return (String) cboPlayerType.getSelectedItem();
    }
    
    /**
     * Sets the network status to ready
     */
    public void panelHostClientDisconnected() {
        lblNetworkStatus.setText("Disconnected");
        lblNetworkStatus.setForeground(new Color(200,0,0));
        cboPlayerType.setSelectedItem("Network");
        cboPlayerType.setEnabled(false);
        txtName.setEnabled(false);
        cboColors.setEnabled(false);
    }
    
    /**
     * Sets the network status to connected
     */
    public void setNetworkConnected(Player player) {
        p.setName(player.getName());
        p.setColor(player.getColor());
        p.setPlayerType(Player.NETWORK);
        lblNetworkStatus.setText("Connected");
        lblNetworkStatus.setVisible(true);
        lblNetworkStatus.setForeground(new Color(0,128,32));
        txtName.setText(p.getName());
        cboColors.setSelectedIndex(getIndexByColor(p.getColor()));
        cboPlayerType.setSelectedItem("Network");
        cboPlayerType.setEnabled(false);
        txtName.setEnabled(false);
        cboColors.setEnabled(false);
    }
    
    /**
    * Gets the next color in the list.  Used by the Game class if
    * @param c the base color.
    * @return the next color in the list of colors.
    */
    public static Color getNextColor(Color c) {
        int i = 0;
        for(i = 0; i < COLORS.length; i++) {
            if(c.equals(COLORS[i])) break;
        }
        return COLORS[(i+1)%COLORS.length];
    }

    /**
     * Called by the NGUI when the interface reports the client has connected
     * to the server.
     */
    void panelJoinConnected() {
        lblNetworkStatus.setText("Connected");
        lblNetworkStatus.setForeground(new Color(0,128,32));
        lblNetworkStatus.setVisible(true);
        cboColors.setEnabled(false);
        cboPlayerType.setEnabled(false);
        txtName.setEnabled(false);
    }
    
    /**
     * Called by the NGUI when the interface chooses a join game option.
     */
    void panelJoinWaiting() {
        lblNetworkStatus.setText("Press Join");
        lblNetworkStatus.setForeground(new Color(164,100,0));
        lblNetworkStatus.setVisible(true);
        cboColors.setEnabled(true);
        cboPlayerType.setEnabled(false);
        cboPlayerType.setSelectedItem("Human");
        txtName.setEnabled(true);
    }    

    /**
     * Called by the NGUI when the interface is attempting to connect to a
     * host.
     */
    void panelJoinConnecting() {
        lblNetworkStatus.setText("Connecting");
        lblNetworkStatus.setForeground(new Color(164,164,32));
        lblNetworkStatus.setVisible(true);
        cboColors.setEnabled(false);
        cboPlayerType.setEnabled(false);
        txtName.setEnabled(false);
    }
    
    /**
     * Called by the NGUI when hosting but not actively listening for an
     * incoming connection.
     */
    void panelHostWaiting() {
        cboColors.setEnabled(true);
        cboPlayerType.setEnabled(false);
        cboPlayerType.setSelectedItem("Human");
        txtName.setEnabled(true);
        lblNetworkStatus.setText("Press Listen");
        lblNetworkStatus.setForeground(new Color(164,100,0));
        lblNetworkStatus.setVisible(true);        
    }
    
    /**
     * Called by the NGUI when hosting but not actively listening for an
     * incoming connection.
     */
    void panelHostListeneing() {
        cboColors.setEnabled(true);
        cboPlayerType.setEnabled(false);
        cboPlayerType.setSelectedItem("Human");
        txtName.setEnabled(true);
        lblNetworkStatus.setText("Listening");
        lblNetworkStatus.setForeground(new Color(164,164,32));
        lblNetworkStatus.setVisible(true);        
    }
    
    /**
     * Called by the NGUI when hosting and a client has successfully connected.
     */
    void panelHostReady() {
        cboColors.setEnabled(true);
        cboPlayerType.setEnabled(false);
        cboPlayerType.setSelectedItem("Human");
        txtName.setEnabled(true);
        lblNetworkStatus.setText("Ready");
        lblNetworkStatus.setForeground(new Color(64,64,255));
        lblNetworkStatus.setVisible(true);        
    }
    
    /**
     * Called by the NGUI when the user has depressed the network button.
     */
    void panelNetworkOff() {
        cboColors.setEnabled(true);
        cboPlayerType.setEnabled(playerNumb != 1);
        if(p.getPlayerType() == Player.NETWORK) 
            cboPlayerType.setSelectedItem("Human");
        txtName.setEnabled(true);
        lblNetworkStatus.setText("Ready");
        lblNetworkStatus.setForeground(new Color(64,64,255));
        lblNetworkStatus.setVisible(false);
        this.typeCheckEnable();
    }         
    
}
