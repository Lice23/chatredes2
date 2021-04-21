package ChatBase;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class ChatWindowController {
    public TextArea chatTextArea;
    public ListView<String> chatListView;
    public TextField ipTextField;
    public TextField portTextField;
    public Button sendButton;

    public abstract void startConnection(ActionEvent actionEvent);
    public abstract void endConnection(ActionEvent actionEvent);
    public abstract void sendMessage(ActionEvent actionEvent);

    public void checkForSend(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.getKeyCode("Enter")){
            if(!keyEvent.isShiftDown()){
                sendMessage(new ActionEvent());
            } else {
                chatTextArea.appendText("\n");
            }
        }

    }

    public void receiveMessage(String msg){
        addMessageToListView(msg, false);
    }

    int validatePort(String port){
        int res;
        try{
            res = Integer.parseInt(port);
        } catch(NumberFormatException e){
            System.out.println("Porta inválida. Porta deveria ser composta somente de números.");
            res = -1;
        }
        return res;
    }

    void addMessageToListView(String msg, boolean you){
        String prefix = you ? Message.sentPrefix : Message.receivedPrefix;
        addMessageToListView(prefix + msg);
    }

    void addMessageToListView(String msg){
        if(msg.equals(Message.sentPrefix) || msg.equals(Message.receivedPrefix) || !isMessageValid(msg)) return;
        Platform.runLater(() -> {
            chatListView.getItems().add(msg);
            chatTextArea.setText("");
        });
    }

    void disableChat(boolean disable){
        chatTextArea.setDisable(disable);
        sendButton.setDisable(disable);
        if(disable){
            addMessageToListView("Desconectado");
        } else {
            addMessageToListView("Conectado");
        }
    }

    boolean isMessageValid(String msg){
        return !msg.isBlank() && !msg.isEmpty() && !msg.contains("#");
    }
}
