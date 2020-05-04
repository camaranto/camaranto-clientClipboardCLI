/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientClipboardCLI;

import InputOutput.fileReader;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author anakim
 */
public class main {
    public static void main(String[] args) {
        if(args.length  == 0){
            System.out.println("error:missing arguments");
            System.exit(0);
        }
        ClipboardOwner owner = (Clipboard clipboard, Transferable contents) -> {
            System.out.println("text replaced");
            System.exit(0);
        };
        
        fileReader fr = new fileReader(new File("help")){
            @Override
            public void lineProcesser(String line){
                System.out.println(line);
            }
        };
        
        messageHandler HANDLER = new messageHandler() {
            @Override
            public void TextMessageReceiveFromClient(Socket clientSocket, String data) {
                //System.out.println("message receive from:" + clientSocket.getInetAddress().getHostAddress());
                System.out.println("msg: " + data);
                setStringToClipboard(data, owner);
                int waitTime = 10000;
                try {
                    System.out.println("you have " + waitTime/1000 + " seconds to copy the content");
                    Thread.sleep(waitTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void FileReceiveFromClient(Socket clientSocket, byte[] data) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void ImgReceivedFromClient(Socket clientSocket, byte[] data) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void failedToSendMessage(Socket clientSocket, byte[] data) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        if ("--help".startsWith(args[0]) || args[0].equals("-h")) {
            fr.readFile();
        }else if("--version".startsWith(args[0]) || args[0].equals("-v")){
            System.out.println("game of the year edition(definitive version)");
        }else if("--receptor".startsWith(args[0]) || args[0].equals("-r")){
            int port = 7800;
            if(args.length == 3 && "-port".startsWith(args[1])){
                port = Integer.parseInt(args[2]);
            }else if(args.length == 2 ){
                System.out.println("expected arguments: -port XXXX");
            }
            try {
                ReceptorTCP server = new ReceptorTCP(port, HANDLER);
                server.enableReception();
            } catch (UnknownHostException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else if("--send".startsWith(args[0]) || args[0].equals("-s")){
            if(args.length == 3 && isValidIPAddress(args[1])){
                String ip = args[1];
                int port = Integer.parseInt(args[2]);
                SenderTCP sender = new SenderTCP(ip, port, HANDLER);
                sender.connect();
                Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                String TEXT = "";
                if (systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){
                    try {
                        TEXT =  (String)systemClipboard.getData(DataFlavor.stringFlavor);
                    } catch (UnsupportedFlavorException | IOException ex) {
                        System.out.println("error: copying the content of the clipboard");
                        System.exit(0);
                    }
                }
                sender.send("TEXT", TEXT);
                sender.close();
            }
        }else{
            System.out.println("I have not idea what do you want");
        }
    }
    
    
    public static boolean isValidIPAddress(String ip) { 
        String zeroTo255  = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])"; 
        String regex = zeroTo255 + "\\."+ zeroTo255 + "\\."  + zeroTo255 + "\\." + zeroTo255; 
        return ip.matches(regex);
    }
    
    private static void setStringToClipboard(String str, ClipboardOwner owner){
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(str), owner);
    }
    
}
