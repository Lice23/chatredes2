package ChatBase;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ChatModule {
    protected ChatWindowController controller;
    protected int port;
    protected DataOutputStream dataOutputStream;
    protected Socket socket;

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

    public abstract void endConnection();
}
