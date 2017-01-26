/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Opens a dialog box for the about screen.
 * @author Rob
 */
public class AboutDialog extends JDialog {
    
    /**
     * Constructs and sets up the dialog box.
     */
    public AboutDialog() {
        this.setPreferredSize(new Dimension(300,180));
        this.setTitle("About Happy Blocks");
        this.setLayout(new BorderLayout());
        
        JLabel label = new JLabel();
        
        StringBuilder about = new StringBuilder();
        about.append("<html>Happy Blocks");
        about.append("<br><br>");
        about.append("Rob DeGree<br>");
        about.append("Laura Krebs");
        about.append("<br><br>");
        about.append("Version 0.2");
        about.append("</html>");
        label.setText(about.toString());
        
        this.add(label, BorderLayout.CENTER);
        
        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                closeWindow();
            }
        });
        
        this.add(btnOk, BorderLayout.SOUTH);
        
        this.pack();
        this.setVisible(true);
    }
    
    /**
     * Disposes of this dialog box.
     */
    void closeWindow() {
        this.dispose();
    }
}
