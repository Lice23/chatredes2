package ChatBase;

import javafx.event.ActionEvent;

import java.io.IOException;

public class ClientWindowController extends ChatWindowController {
    private Client client;

    @Override
    public void startConnection(ActionEvent event){
        int port = validatePort(portTextField.getText());
        if(port == -1) return;
        client = new Client(ipTextField.getText(), port, this);
        try{
            client.initialize();
            disableChat(false);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void endConnection(ActionEvent actionEvent) {
        client.endConnection();
        disableChat(true);
    }

    @Override
    public void sendMessage(ActionEvent actionEvent) {
        if(!isMessageValid(chatTextArea.getText())) return;
        client.sendMessage(chatTextArea.getText());
        addMessageToListView(chatTextArea.getText(), true);
    }
}
