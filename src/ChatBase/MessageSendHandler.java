package ChatBase;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

// Essa classe é necessária?
public class MessageSendHandler extends Thread {
    private final OutputStreamWriter outputStreamWriter;
    private final ChatWindowController controller;

    MessageSendHandler(OutputStreamWriter outputStreamWriter, ChatWindowController controller) {
        this.outputStreamWriter = outputStreamWriter;
        this.controller = controller;
        start(); // will load the run method
    }

    public void run() {
        try {
            // receive input
            //String response = "Mensagem recebida#";
            while(true){
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();

                /*if(message.length() == 1 && message.charAt(0) == Message.terminator){
                    break;
                }*/

                //System.out.println("Você: " + message);
                //controller.sendMessage(message);
                message += Message.terminator;
                outputStreamWriter.write(message);
                outputStreamWriter.flush(); // response sent
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
