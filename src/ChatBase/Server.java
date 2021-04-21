package ChatBase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.sound.sampled.*;

class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket acceptedSocket;
    private final int port;
    private ServerWindowController controller;
    private OutputStreamWriter outputStreamWriter;

    Server(int port, ServerWindowController controller) {
        this.port = port;
        this.controller = controller;
        try {
            //Initiating ServerSocket with TCP port
            serverSocket = new ServerSocket(this.port);
            start();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run() {
        try {
            System.out.println("Servidor escutando na porta: " + this.port);
            acceptedSocket = serverSocket.accept();
            new MessageReceiveHandler(acceptedSocket, controller);
            outputStreamWriter = new OutputStreamWriter(acceptedSocket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            message += Message.terminator;
            outputStreamWriter.write(message);
            outputStreamWriter.flush(); // response sent
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void endConnection() {
        try{
            acceptedSocket.close();
            serverSocket.close();
            interrupt();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}