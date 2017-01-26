/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import backend.CriticalException;

/**
 * Representation of a networked player on the game.  Provides superclass for 
 * host and client.  Class may be referenced but not constructed directly.
 * @author Rob
 */
public class NetworkPlayer {
    protected NetworkInterface ni;
    protected boolean active;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    private final int LISTENDELAY = 1000;

    /**
     * The constructor is called by the subclasses.
     * @param ni The linked network interface.
     * @param hp The protocol to use for deciphering strings.
     */
    protected NetworkPlayer(NetworkInterface ni) {
        this.ni = ni;
    }
    
    /**
     * Method for listening for incoming strings.  The listen() method is threaded,
     * and sleeps for a set time (LISTENDELAY) before checking to see if a new
     * string has been sent to the player.  If a new string is found, it is sent
     * to the protocol for processing.
     */
    protected void listen() throws NetworkException {
        Object input;
        Object output;
        
        try {
            while (true) {
                try {
                    Thread.sleep(LISTENDELAY);
                } catch (Exception ex) {

                }

                try {
                    if((input = in.readObject()) != null) {
                        XferAction xfer = (XferAction) input;
                        xfer.setTransientVars(ni);
                        xfer.execute();
                    }
                } catch(ClassNotFoundException ex) {
                    new CriticalException("Bad data transfered over the network.");
                }
                

                if (!active) break;
            }
        } catch (IOException ex) {
            String reason = "Remote Player Disconnected";
            throw new NetworkException(reason);
        }
    
    }
    
    
    public void stopServer() {
        System.out.println("Connection closed");
        active = false;
    }
    
    public void sendOutput(Object output) {
        try {
            out.writeObject(output);
        } catch (IOException ex) {
        }
        
    }   
    
    public void start() throws NetworkException {
        //Intended to be overridden
    }
    
     /**
     * Shuts down the Network Sockets.
     */
    public void stop() {
        //Intended to be overridden.
    }
}
