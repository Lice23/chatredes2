package ChatBase.Server;

import ChatBase.MessageReceiveHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket acceptedSocket;
    private final ServerWindowController controller;
    private DataOutputStream dataOutputStream;
    private MessageReceiveHandler messageReceiveHandler;

    Server(int port, ServerWindowController controller) {
        this.controller = controller;

        try {
            serverSocket = new ServerSocket(port);
            start();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run() {
        try {
            acceptedSocket = serverSocket.accept();
            messageReceiveHandler = new MessageReceiveHandler(acceptedSocket, controller);
            dataOutputStream = new DataOutputStream(acceptedSocket.getOutputStream());
            controller.enableChatWithMessage();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(String message) {
        try {
            dataOutputStream.writeBoolean(true); // É mensagem de texto
            dataOutputStream.writeInt(message.length());
            dataOutputStream.writeChars(message);
            dataOutputStream.flush(); // response sent
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAudioMessage(byte[] data, int numBytes){
        try {
            dataOutputStream.writeBoolean(false); // É mensagem de áudio
            dataOutputStream.writeInt(numBytes);
            dataOutputStream.write(data, 0, numBytes);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void endConnection() {
        try{
            messageReceiveHandler.interrupt();
            acceptedSocket.close();
            serverSocket.close();
            this.interrupt();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}