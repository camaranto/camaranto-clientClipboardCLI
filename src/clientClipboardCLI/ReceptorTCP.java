package clientClipboardCLI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anakim
 */
public class ReceptorTCP extends Thread {
    
    private final String LOCAL_IP;
    private final String LOCAL_HOST_NAME;
    //private boolean isEnabled;
    private ServerSocket serverSocket;
    private final int PORT;
    private String TEXT;
    private final messageHandler caller;
    public ReceptorTCP(int port, messageHandler caller) throws UnknownHostException{
        this.LOCAL_IP = InetAddress.getLocalHost().getHostAddress();
        this.LOCAL_HOST_NAME = InetAddress.getLocalHost().getHostName();
        //this.isEnabled = false;
        this.PORT = port;
        this.caller = caller;
    }
    
    public void enableReception(){
        //isEnabled = true;
        this.start();
       
    }
    public void disableReception(){
        //isEnabled = false;
    }
    
    @Override
    public void run(){
        try {
            this.serverSocket=new ServerSocket(PORT);
            System.out.println("listening on port:" + PORT);
            try (Socket receivedSocket = serverSocket.accept()) {
                System.out.println("client received from " + receivedSocket.getInetAddress().getHostAddress());
                ObjectInputStream IS = new ObjectInputStream( receivedSocket.getInputStream());
                switch(IS.readUTF().split("/")[0]){
                    case "TEXT":
                        String load = IS.readUTF();
                        //System.out.println("msg: " + load);
                        caller.TextMessageReceiveFromClient(receivedSocket, load);
                        break;
                    case "FILE":

                        break;
                    case "IMGN":

                        break;
                    default:
                        System.out.println("trash");

                }
                IS.close();
                receivedSocket.close();
                serverSocket.close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ReceptorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    public String getLOCAL_IP() {
        return LOCAL_IP;
    }

    public String getLOCAL_HOST_NAME() {
        return LOCAL_HOST_NAME;
    }
    
    public String getReceivedText(){
        return TEXT;
    }
    
            
}
