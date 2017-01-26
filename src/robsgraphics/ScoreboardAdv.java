/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robsgraphics;

import backend.Game;
import backend.Player;
import graphics.Cloud;
import graphics.CloudTimer;
import graphics.HappyBlock;
import graphics.WelcomeBoard;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Random;
import javax.swing.JComponent;
import listeners.ScoreboardListener;

/**
 * Simple timer that repaints the scoreboard.
 * @author Rob
 */
public class ScoreboardAdv extends JComponent {
    //Data members
    private int width;
    private int height;
    private Game game;
    private SbActionArea[] actionAreas = new SbActionArea[2];
    private Cloud cloud;
    private RepaintTimer rptimer;
    
    /**
     * Constructor for the scoreboard
     * @param width width of paint area.
     * @param height height of paint area.
     */
    public ScoreboardAdv(int width, int height, Game game) {
        this.width = width;
        this.height = height;
        this.game = game;
        
        //Clouds
        cloud = new Cloud(height);
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            cloud.add(new Point2D.Double(x-50, y));
        }
        
        //Start the timer
        rptimer = new RepaintTimer(this, 100);
        rptimer.start();
        
        this.setPreferredSize(new Dimension(width, height));
        this.addMouseListener(new ScoreboardListener(this));
    }
    
    /**
     * repaint() method calls here.
     */
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      //Sky
      g2.setColor(WelcomeBoard.SKYCOLOR);
      g2.fillRect(0, 0, width, height);
      
      //Clouds
      drawClouds(g2);
      
      //Border
      g2.setColor(Color.black);
      g2.drawRect(0, 0, width-1, height-1);
      
      drawHeader(g2, new Point(15,55));
      
      if(game.getGameStatus()) {
         drawTurn(g2, new Point(width/2 -25,300));
      
        //Draw player panels
        for(int i = 0; i < game.getNumberOfPlayers(); i++) {
            Point ptDraw = new Point((int) (width *0.1), 400+ 60 * i);
            drawPlayerPanel(g2, ptDraw, i);
        }
      }

      
      drawButtons(g2, new Point(10, (int) (height *0.92)));

    }
    
    private void drawClouds(Graphics2D g2) {
      int m = height / 2;
      // last-specified-first-applied (see java.awt.Graphics2D#transform)
      // last shift "down" (positive) m pixels
      g2.translate(0, m);
      // flip about y-axis
      g2.scale(1, -1);
      // first shift "up" (negative) m pixels
      g2.translate(0, -m);        
      
        g2.setColor(Color.white);
        cloud.update();
        cloud.draw(g2);
        
      
      // last-specified-first-applied (see java.awt.Graphics2D#transform)
      // last shift "down" (positive) m pixels
      g2.translate(0, m);
      // flip about y-axis
      g2.scale(1, -1);
      // first shift "up" (negative) m pixels
      g2.translate(0, -m);        
    }
    
    private void drawHeader(Graphics2D g2, Point ptStart) {
        g2.setColor(Color.black);
        g2.setFont(new Font("Header", Font.BOLD, 36));
        this.printCenteredString(g2, "Happy Blocks" ,width-10, ptStart.x-10, ptStart.y-5);
        HappyBlock.drawMeStatic(g2,"o", width / 4 - 25, ptStart.y +25, 50, 50, new Color(30,144,255), true);
        HappyBlock.drawMeStatic(g2,"u", width / 2 - 25, ptStart.y +25, 50, 50, Color.YELLOW, true);
        HappyBlock.drawMeStatic(g2,"h", (int) (width*3/4-25), ptStart.y+25, 50, 50, new Color(250,128,114), true);     
        g2.drawRect(5, 5, width-10, 150);
    }

    private void drawTurn(Graphics2D g2, Point ptStart) {
        g2.setColor(Color.black);
        int x1 = ptStart.x;
        int x2 = ptStart.x +25;
        int y1 = ptStart.y;
        int y2 = ptStart.y -25;
        g2.drawLine(x2-50, y2, x1, y1);        
        g2.drawLine(x1-50, y1, x2-50, y2);
        
        //Draw the current player
        x1 = ptStart.x;
        y1 = ptStart.y;
        int index = game.getCurPlayerIndex();
        
        //Draw preceding player
        HappyBlock.drawMeStatic(g2,"o", (int) (width/2-35)+80, y1-90, 50, 50, 
                game.getPlayerColor(Math.floorMod(index+2, 
                        game.getNumberOfPlayers())), true);
        
        //Draw next player
        HappyBlock.drawMeStatic(g2,"o", (int) (width/2-35)+20, y1-95, 60, 60, 
                game.getPlayerColor((index+1) % game.getNumberOfPlayers()), true);         
        
        g2.setFont(new Font("Turn", Font.BOLD, 28));
        printCenteredString(g2, game.getPlayerName(index) + "'s Turn", 
                0, width/2, y1+28);
        HappyBlock.drawMeStatic(g2,"u", (int) (width/2-80), y1-100, 70, 70, 
                game.getPlayerColor(index), true);           
        
    }
    
    private void drawPlayerPanel(Graphics2D g2, Point ptStart, int i) {
        int x1 = ptStart.x;
        int y1 = ptStart.y;
        
        //Draw rectangle
        g2.setColor(Color.white);
        g2.fillRect(x1, y1, (int) (width * 0.8), 50);
        Color fillcolor = game.getPlayerColor(i);
        fillcolor = new Color(fillcolor.getRed(), fillcolor.getGreen(),
            fillcolor.getBlue(), 128);        
        if(game.getPlayerType(i) != Player.HUMAN) {
            fillcolor = new Color(fillcolor.getRed(), fillcolor.getGreen(),
                fillcolor.getBlue(), 128);
        }
        g2.setColor(fillcolor);
        g2.fillRect(x1, y1, (int) (width * 0.8), 50);
        g2.setColor(Color.black);
        g2.drawRect(x1, y1, (int) (width * 0.8), 50);
        
        HappyBlock.drawMeStatic(g2,"u", (int) x1+5, y1+5, 40, 40, 
                game.getPlayerColor(i), true);              
        
        g2.setFont(new Font("Name", Font.BOLD, 22));   
        if(game.getPlayerType(i) == Player.HUMAN) {     
            g2.drawString(game.getPlayerName(i), x1+50, y1+32);
        } else {
            g2.drawString(game.getPlayerName(i), x1+50, y1+25);            
            g2.setFont(new Font("little", Font.ITALIC, 14));               
            if(game.getPlayerType(i) == Player.AIENABLED) {
                g2.drawString("CPU Player", x1+50, y1+43);
            } else if(game.getPlayerType(i) == Player.NETWORK) {
                g2.drawString("Remote Player", x1+50, y1+43);                
            }
        }
        
        //Score
        Integer score = game.getPlayerScore(i);
        g2.setFont(new Font("Score", Font.BOLD, 26));            
        printRightAlignedString(g2, score.toString(), (int) (width *0.8)-60, 
                x1+50, y1+32);
    }
    
    private void drawButtons(Graphics2D g2, Point stPoint) {
        int x1 = stPoint.x;
        int y1 = stPoint.y;
        Color buttoncolor = new Color(200,200,200,192);
        
        g2.setColor(buttoncolor);
        g2.fillRect(x1, y1, (int) (width * 0.7), 50);
        g2.setColor(Color.black);
        g2.drawRect(x1, y1, (int) (width * 0.7), 50);
        actionAreas[0] = new SbActionArea(x1, y1, (int) (width * 0.7), 50, 
            SbActionArea.NEWGAMEAREA, game);
        
        g2.setColor(buttoncolor);
        g2.fillRect(x1 +(int) (width*0.7)+10, y1, (int) (width * 0.3)-30, 50);
        g2.setColor(Color.black);         
        g2.drawRect(x1 +(int) (width*0.7)+10, y1, (int) (width * 0.3)-30, 50);
        actionAreas[1] = new SbActionArea(x1 +(int) (width*0.7)+10, y1, 
                (int) (width * 0.3)-30, 50, SbActionArea.ABOUTAREA, game);
        
        g2.setFont(new Font("Score", Font.BOLD, 32));         
        printCenteredString(g2, "New Game", (int) (width * 0.7), 
                x1, y1+35);
        g2.setFont(new Font("Score", Font.BOLD, 14));              
        printCenteredString(g2, "About", (int) (width * 0.3)-30, 
                x1 +(int) (width*0.7)+10, y1+32);        
    }
    
    
    
    //===============UTILITY METHODS==========================
    private void printCenteredString(Graphics2D g2, String s, int width, int XPos, int YPos){
        int stringLen = (int)
            g2.getFontMetrics().getStringBounds(s, g2).getWidth();
        int start = width/2 - stringLen/2;
        g2.drawString(s, start + XPos, YPos);
    }
    
    private void printRightAlignedString(Graphics2D g2, String s, int width, int XPos, int YPos){
        int stringLen = (int)
            g2.getFontMetrics().getStringBounds(s, g2).getWidth();
        int start = width - stringLen;
        g2.drawString(s, start + XPos, YPos);
    }
 
    
    //================GETTERS AND SETTERS======================
    /**
     * Gets the array of button areas for listener checking.
     * @return an action area representing the new game area.
     */
    public SbActionArea[] getActionAreas() {
        return actionAreas;
    }
    
    
    //================ADVANCED PAINT METHODS===================
    /**
     * Begins the painting process, should be called after moves.
     */
    public void readypaint() {
        repaint();
    }

}
