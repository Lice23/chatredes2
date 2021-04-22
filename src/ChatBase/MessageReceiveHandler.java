package ChatBase;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class MessageReceiveHandler extends Thread {
    private final Socket socket;
    private final ChatWindowController controller;

    public MessageReceiveHandler(Socket socket, ChatWindowController controller) {
        this.socket = socket;
        this.controller = controller;
        start();
    }

    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            while(!interrupted()){
                boolean isText = dataInputStream.readBoolean();
                if(isText){
                    int numChars = dataInputStream.readInt();
                    for(int i = 0; i < numChars; i++){
                        stringBuilder.append(dataInputStream.readChar());
                    }
                    String message = stringBuilder.toString();
                    controller.receiveMessage(message);
                    stringBuilder = new StringBuilder();
                } else {
                    int numBytes = dataInputStream.readInt();
                    byte[] data = new byte[numBytes];
                    int numReadBytes = dataInputStream.read(data, 0, numBytes);
                    if (numReadBytes != numBytes) {
                        System.out.println("Error: Number of bytes written is not equal to number of bytes read.");
                    } else {
                        controller.receiveMessage(data);
                    }
                }
            }
        } catch(IOException e) {
            //e.printStackTrace();
            System.out.println("Socket closed.");
        }
    }
}
