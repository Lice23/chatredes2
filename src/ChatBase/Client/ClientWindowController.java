package ChatBase.Client;

import ChatBase.Audio;
import ChatBase.AudioMessage;
import ChatBase.ChatWindowController;
import ChatBase.Message;
import javafx.event.ActionEvent;

public class ClientWindowController extends ChatWindowController {
    private Client client;

    @Override
    public void startConnection(ActionEvent event){
        int port = validatePort(portTextField.getText());
        if(port == -1) return;

        client = new Client(ipTextField.getText(), port, this);
        enableChatWithMessage();
        audio = new Audio();
    }

    @Override
    public void endConnection(ActionEvent actionEvent) {
        client.sendTextMessage(Message.clientDisconnected);
        client.endConnection();
        disableChatWithMessage();
    }

    @Override
    public void sendMessage(ActionEvent actionEvent) {
        if(!isMessageValid(chatTextArea.getText())) return;
        client.sendTextMessage(chatTextArea.getText());
        addMessageToListView(chatTextArea.getText(), true);
    }

    @Override
    public void sendAudioMessage(AudioMessage audioMessage) {
        client.sendAudioMessage(audioMessage.data, audioMessage.data.length);
    }
}
