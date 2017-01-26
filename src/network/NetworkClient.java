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
import java.net.Socket;

/**
 * The client is responsible for connecting to a host.
 * @author Rob
 */
public class NetworkClient extends NetworkPlayer {
    //SPECIALTY PORT/HOST CLIENT DATA MEMBERS
    private int port;
    private String host;
    private Socket socket;
    
    /**
     * Initializes the data members of the super class and sets up the host
     * and port names.
     * @param ni The linked network interface.
     * @param hp The porotcol to use for strings
     * @param host Host IP Address (or URL) for connecting to.
     * @param port TCP port to connect to.
     */
    public NetworkClient(NetworkInterface ni, String host, int port) {
        super(ni);
        this.host = host;
        this.port = port;
    }
    
     /**
     * Attempts to connect to a host running the TCP socket.  Overrides the
     * start() method in the NetworkPlayer class
     */
    @Override
    public void start() throws NetworkException {
        //Attempt to connect to the host on that port.
        try (
            Socket socket = new Socket(host, port);
            ObjectOutputStream out =
                new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(
                socket.getInputStream()); 
            ) {
            //Client connected here.
            super.out = out;
            super.in = in;
            this.socket = socket;
            System.out.println("Client Connected");
            super.active = true;
            super.ni.sendJoiningPlayer();
            super.listen();
            
        } catch (IOException ex) {
            throw new NetworkException("Failed to connect on that IP/Host");
        } catch (NetworkException nex) {
            stop();
            throw nex;
        }
    }
    
    /**
     * Shuts down the Network Sockets.
     */
    public void stop() {
        try {
            if(socket != null) socket.close();            
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
