package clientClipboardCLI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.net.Socket;

/**
 *
 * @author anakim
 */
public interface messageHandler {
    public void TextMessageReceiveFromClient(Socket clientSocket, String data);
    public void FileReceiveFromClient(Socket clientSocket, byte[] data);
    public void ImgReceivedFromClient(Socket clientSocket, byte[] data);
    public void failedToSendMessage(Socket clientSocket, byte[] data);

}
