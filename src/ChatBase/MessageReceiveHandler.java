package ChatBase;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReceiveHandler extends Thread {
    private final Socket socket;
    private final ChatWindowController controller;

    MessageReceiveHandler(Socket socket, ChatWindowController controller) {
        this.socket = socket;
        this.controller = controller;
        start(); // will load the run method
    }

    public void run() {
        try {
            //getting input stream and its reader, for reading request or acknowledgement
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            while(true){
                int x = inputStreamReader.read();
                while(x != Message.terminator || x == -1) {
                    stringBuilder.append((char) x);
                    x = inputStreamReader.read();
                }
                String message = stringBuilder.toString();

                controller.receiveMessage(message);
                stringBuilder = new StringBuilder();
            }
        } catch(IOException e) {
            //e.printStackTrace();
            System.out.println("Socket closed.");
        }
    }
}
