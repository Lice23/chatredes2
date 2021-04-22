package ChatBase;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.LinkedList;

public abstract class ChatWindowController {
    public TextArea chatTextArea;
    public ListView<Object> chatListView;
    public TextField ipTextField;
    public TextField portTextField;
    public Button sendButton;
    public Button recordButton;
    public ImageView recordImage;
    public Button connectButton;
    public Button endButton;
    public Audio audio;
    private final String stopImageUrl = "file:/E:/IntelliJ%20Projects/Chat/Resources/rounded-black-square-shape.png";
    private final String recordImageUrl = "file:/E:/IntelliJ%20Projects/Chat/Resources/microphone-black-shape.png";
    protected LinkedList<AudioMessage> audioMessages = new LinkedList<>();

    public abstract void startConnection(ActionEvent actionEvent);
    public abstract void endConnection(ActionEvent actionEvent);
    public abstract void sendMessage(ActionEvent actionEvent);
    public abstract void sendAudioMessage(AudioMessage audioMessage);

    public void checkForSend(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.getKeyCode("Enter")){
            if(!keyEvent.isShiftDown()){
                sendMessage(new ActionEvent());
            } else {
                chatTextArea.appendText("\n");
            }
        }
    }

    public void startRecording(ActionEvent actionEvent) {
        if (!audio.isRecording) {
            audio.recordAudio();
            Platform.runLater(() -> {
                recordImage.setImage(new Image(stopImageUrl));
            });
        } else {
            audio.setRecording(false);
            Platform.runLater(() -> {
                recordImage.setImage(new Image(recordImageUrl));
            });

            AudioMessage audioMessage = new AudioMessage(audio.getRecordedAudio());
            audioMessages.add(audioMessage);
            sendAudioMessage(audioMessage);
            addAudioMessageToListView(audioMessage, true);
        }
    }

    public void receiveMessage(String msg){
        if(msg.equals(Message.clientDisconnected) || msg.equals(Message.serverDisconnected)){
            addMessageToListView(msg);
            disableFromDisconnect();
        } else {
            addMessageToListView(msg, false);
        }
    }

    public void receiveMessage(byte[] data){
        AudioMessage audioMessage = new AudioMessage(data);
        audioMessages.add(audioMessage);
        addAudioMessageToListView(audioMessage, false);
    }

    public int validatePort(String port){
        int res;
        try{
            res = Integer.parseInt(port);
        } catch(NumberFormatException e){
            System.out.println("Porta inválida. Porta deveria ser composta somente de números.");
            res = -1;
        }
        return res;
    }

    public void addMessageToListView(String msg, boolean you){
        String prefix = you ? Message.sentPrefix : Message.receivedPrefix;
        addMessageToListView(prefix + msg);
    }

    public void addMessageToListView(String msg){
        if(msg.equals(Message.sentPrefix) || msg.equals(Message.receivedPrefix) || !isMessageValid(msg)) return;
        Platform.runLater(() -> {
            chatListView.getItems().add(msg);
            chatTextArea.setText("");
        });
    }

    public void addAudioMessageToListView(AudioMessage audioMessage, boolean you){
        String text = you ? Message.sentPrefix : Message.receivedPrefix;
        text = text.substring(0, text.length()-2);
        ButtonLineListener audioMessageButton = new ButtonLineListener(text, audio, audioMessage);
        Platform.runLater(() -> {
            chatListView.getItems().add(audioMessageButton);
        });
    }

    public void disableFromDisconnect(){
        chatTextArea.setDisable(true);
        sendButton.setDisable(true);
        recordButton.setDisable(true);
        endButton.setDisable(false);

        if(ipTextField != null) ipTextField.setDisable(true);
        portTextField.setDisable(true);
        connectButton.setDisable(true);
    }

    public void disableChat(boolean disable) {
        chatTextArea.setDisable(disable);
        sendButton.setDisable(disable);
        recordButton.setDisable(disable);
        endButton.setDisable(disable);

        if(ipTextField != null) ipTextField.setDisable(!disable);
        portTextField.setDisable(!disable);
        connectButton.setDisable(!disable);
    }

    public void enableChatWithMessage(){
        disableChat(false);
        addMessageToListView("Conectado");
    }

    public void disableChatWithMessage(){
        disableFromDisconnect();
        endButton.setDisable(true);
        addMessageToListView("Desconectado. Reinicie o chat para começar de novo.");
    }

    public boolean isMessageValid(String msg){
        return !msg.isBlank() && !msg.isEmpty();
    }
}
