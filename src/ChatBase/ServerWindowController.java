package ChatBase;

import javafx.event.ActionEvent;

public class ServerWindowController extends ChatWindowController {
    private Server server;

    @Override
    public void startConnection(ActionEvent event){
        int port = validatePort(portTextField.getText());
        if(port == -1) return;
        server = new Server(port, this);
        disableChat(false);
    }

    @Override
    public void endConnection(ActionEvent actionEvent) {
        server.endConnection();
        disableChat(true);
    }

    @Override
    public void sendMessage(ActionEvent actionEvent) {
        if(!isMessageValid(chatTextArea.getText())) return;
        server.sendMessage(chatTextArea.getText());
        addMessageToListView(chatTextArea.getText(), true);
    }
}
