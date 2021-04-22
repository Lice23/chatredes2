package ChatBase.Server;

import ChatBase.Audio;
import ChatBase.AudioMessage;
import ChatBase.ChatWindowController;
import javafx.event.ActionEvent;

public class ServerWindowController extends ChatWindowController {
    private Server server;

    @Override
    public void startConnection(ActionEvent event){
        int port = validatePort(portTextField.getText());
        if(port == -1) return;
        server = new Server(port, this);
        disableChat(false);
        audio = new Audio();
    }

    @Override
    public void endConnection(ActionEvent actionEvent) {
        server.endConnection();
        disableChat(true);
    }

    @Override
    public void sendMessage(ActionEvent actionEvent) {
        if(!isMessageValid(chatTextArea.getText())) return;
        server.sendTextMessage(chatTextArea.getText());
        addMessageToListView(chatTextArea.getText(), true);
    }

    @Override
    public void sendAudioMessage(AudioMessage audioMessage) {
        server.sendAudioMessage(audioMessage.data, audioMessage.data.length);
    }
}
