/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.awt.Color;
import java.io.Serializable;

/**
 * A representation of one of the players in the game.
 * @author Rob
 */
public class Player implements Serializable {
    //Strings for player type.
    public static final int HUMAN = 1;
    public static final int AIENABLED = 2;
    public static final int NETWORK = 3;
    public static final int DISABLED = -1;
    
    //Data Members
    private Color color;    //Default color
    private String name;    //Name of player
    private int score;    //Score of player
    private transient AI ai;
    private int playertype;
    
    /**
     * Default constructor for the player class.
     * @param name Name of the player.
     * @param color Color of the pieces to be drawn for this player.
     */
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        ai = null;
        score = 0;
        playertype = HUMAN;
    }
    
    public Player(String name, Color color, AI ai) {
        this.name = name;
        this.color = color;
        this.ai =ai;
        score = 0;
        playertype = AIENABLED;
    }
    
    
    
    
    //=======GETTERS and SETTERS=============

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param color the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * @param name the score to set
     */
    public void setName(String name) {
        this.name = name;
    }    
    
    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    public AI getAI() {
       return this.ai;
    }

    public void setAI(AI artificialIntelligence) {
        ai = artificialIntelligence;
    }
    
    public int getPlayerType() {
        return playertype;
    }
    
    public void setPlayerType(int newtype) {
        playertype = newtype;
    }
}
