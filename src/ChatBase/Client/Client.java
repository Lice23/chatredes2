package ChatBase.Client;

import ChatBase.MessageReceiveHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Client {
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private MessageReceiveHandler messageReceiveHandler;

    Client(String ip, int port, ClientWindowController controller){

        try{
            socket = new Socket(ip , port);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            messageReceiveHandler = new MessageReceiveHandler(socket, controller);
        } catch (IOException e) {
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
        try {
            messageReceiveHandler.interrupt();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}