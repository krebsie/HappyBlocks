/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 * General exception for log printing when an illegal move is performed.
 * @author Rob
 */
class IllegalMoveException extends Exception {
    String reason;
    
    /**
     * Prints a reason to the system.out console.
     * @param reason The reason to print.
     */
    public IllegalMoveException(String reason) {
        this.reason = reason;
        System.out.println("ILLEGAL MOVE:" + reason);
    }
}
