package ChatBase.Client;

import ChatBase.MessageReceiveHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Client {
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private final String ip;
    private final int port;
    private final ClientWindowController controller;
    private MessageReceiveHandler messageReceiveHandler;

    Client(String ip, int port, ClientWindowController controller){
        this.ip = ip;
        this.port = port;
        this.controller = controller;
    }

    public void initialize() throws IOException{
        boolean flag = false;
        if(this.ip.equals("")){
            System.out.println("IP não foi inicializado.");
            flag = true;
        } if(this.port == -1){
            System.out.println("Porta não foi inicializada.");
            flag = true;
        }
        if(flag) return;

        initialize(this.ip, this.port);
    }

    public void initialize(String ip, int port) throws IOException {
        socket = new Socket(ip , port);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        listenForMessages();
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

    public void listenForMessages()  {
        messageReceiveHandler = new MessageReceiveHandler(socket, controller);
    }

    public void endConnection() {
        try {
            messageReceiveHandler.interrupt();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}