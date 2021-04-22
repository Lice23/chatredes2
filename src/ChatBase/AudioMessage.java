package ChatBase;

import javax.sound.sampled.SourceDataLine;

public class AudioMessage {
    public byte[] data;
    public volatile boolean isPlaying = false;
    private SourceDataLine lastSource;

    AudioMessage(byte[] data) {
        this.data = data;
    }

    public void setPlaying(boolean playing){
        isPlaying = playing;
        if(!isPlaying && lastSource != null){
            lastSource.stop();
            lastSource.flush();
        }
    }

    public void playAudio(SourceDataLine sourceDataLine){
        setPlaying(true);
        lastSource = sourceDataLine;
        new PlayAudioHandler(data, data.length, lastSource);
    }

    class PlayAudioHandler extends Thread {
        private byte[] data;
        private int numBytes;
        private SourceDataLine sourceDataLine;

        PlayAudioHandler(byte[] data, int numBytes, SourceDataLine sourceDataLine){
            this.data = data;
            this.numBytes = numBytes;
            this.sourceDataLine = sourceDataLine;
            start();
        }

        @Override
        public void run() {
            if(!sourceDataLine.isActive())
                sourceDataLine.start();
            sourceDataLine.write(data, 0, numBytes);
            sourceDataLine.drain();
            setPlaying(false);
        }
    }
}

