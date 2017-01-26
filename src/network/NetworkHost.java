/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A network player whom is the host of the game.  The class extends NetworkPlayer
 * for access to its listen() method.
 * @author Rob
 */
public class NetworkHost extends NetworkPlayer{
    private String ip;
    private int port;
    private ServerSocket serverSocket;
    private boolean startable = false;
    
    /**
     * The host constructor simply initializes the player fields.
     * @param ni The connected interface.
     * @param hp The protocol for deciphering and constructing strings.
     */
    public NetworkHost(NetworkInterface ni, String ip, int port) {
        super(ni);
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            startable = true;
        } catch(IOException ex) {
            new NetworkException("Unable to open a socket, is port busy?");
            startable = false;
        }
        
    }
    
    /**
     * Attempts to open the port and listen for an incoming connection.
     * Overrides the start() method of NetworkPlayer
     * @throws NetworkException Popup dialog exception for busy ports.
     */
    @Override
    public void start() throws NetworkException {
        if(!startable) return; //bail if not startable.
        System.out.println("Waiting for connection");
        ni.notifyServerListening();
        
        //Inside try ( ), wait for incoming connection.
        try ( 
            Socket clientSocket = serverSocket.accept();
            ObjectOutputStream out =
                new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(
                clientSocket.getInputStream()); 
        ) {
            //Connection established
            super.out = out;
            super.in = in;
            this.serverSocket = serverSocket;
            System.out.println("Host Connected");
            super.active = true;
            //Begin listening for incoming protocol strings.
            super.listen();
   
        } catch(IOException ex) {
            //Code for problems here
            //Most likely aborted while attempting to accept.
        } catch (NetworkException nex) {
            stop();
            throw nex;
        }
           
    }
    
     /**
     * Shuts down the Network Socket.
     */
    public void stop() {
        try {
            startable = false;
            if(!serverSocket.isClosed()) serverSocket.close();            
            if(in != null) in.close();
            if(out != null) out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            //Do nothing
        } catch (NullPointerException nex)  {
            nex.printStackTrace();
            //Server was aborted while attempting to listen for
            //incoming connections.
            //Do nothing
        }
    }
    
}
