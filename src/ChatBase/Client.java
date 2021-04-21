package ChatBase;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Client {
    OutputStream outputStream;
    OutputStreamWriter outputStreamWriter;
    Socket socket;
    String ip = "";
    int port = -1;
    ClientWindowController controller;

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
        outputStream = socket.getOutputStream();
        outputStreamWriter = new OutputStreamWriter(outputStream);
        listenForMessages();
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

    public void listenForMessages()  {
        new MessageReceiveHandler(socket, controller);
    }

    public void endConnection() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}