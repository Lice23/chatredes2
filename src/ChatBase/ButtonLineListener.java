package ChatBase;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class ButtonLineListener extends Button implements LineListener {
    public final String playImageUrl = "file:/E:/IntelliJ%20Projects/Chat/Resources/play-button.png";
    public final String stopImageUrl = "file:/E:/IntelliJ%20Projects/Chat/Resources/rounded-black-square-shape.png";
    private final ImageView buttonImage;

    ButtonLineListener(String text, Audio audio, AudioMessage audioMessage){
        this.setText(text);
        this.audio = audio;
        this.audioMessage = audioMessage;
        buttonImage = new ImageView(new Image(playImageUrl));
        buttonImage.setFitHeight(16);
        buttonImage.setFitWidth(16);
        this.setGraphic(buttonImage);
        audio.sourceDataLine.addLineListener(this);

        this.setOnAction(e -> {
            if(audioMessage.isPlaying){
                audioMessage.setPlaying(false);
                setPlayImage();
            } else {
                audioMessage.playAudio(audio.sourceDataLine);
                setStopImage();
            }
        });
    }

    @Override
    public void update(LineEvent event) {
        if(event.getType() == LineEvent.Type.STOP){
            setPlayImage();
        }
    }

    private void setStopImage(){
        Platform.runLater(() -> {
            buttonImage.setImage(new Image(stopImageUrl));
        });
    }

    private void setPlayImage(){
        Platform.runLater(() -> {
            buttonImage.setImage(new Image(playImageUrl));
        });
    }
}
