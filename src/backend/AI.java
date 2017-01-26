/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.util.Random;

/**
 * This class represents a computer player.
 * @author Rob
 */
public class AI {
    //Static strings for difficulty levels.
    public static String DIFFICULTY_EASY = "Easy";
    
    
    //Data Members
    private final String difficulty;
    private final Game game;
    
    /**
     * Constructor for Artificial Intelligence that assigns difficulty level.
     * @param difficulty The string difficulty level.
     */
    public AI(String difficulty, Game game) {
        this.difficulty = difficulty;
        this.game = game;
    }
    
    /**
     * Executes a CPU move based on 
     * @return 
     */
    public boolean makeCPUMove()  throws CriticalException, 
            InterruptedException {
        Thread.sleep(500); //Wait 1/2 second.
        return easyCPUMove();
    }
    
    private boolean easyCPUMove() throws CriticalException{
        int n = game.getBoardSize();
        final int FAILSAFE = 10000000;
        int loops = 0;
        
        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt(n);
        
        while(game.getBottomEmptyRow(randomNum) == -1) {
            randomNum = rand.nextInt(n);
            loops++;
            if(loops > FAILSAFE) {
                //Over 10000000 integers generated.
                //Somethings bad
                throw new CriticalException("The AI was looping too long and the "
                        + "failsafe was triggered.");
            }
        }
        
        game.gmvDropPiece(randomNum);
        
        return true;
    }
    
    
}