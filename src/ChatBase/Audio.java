package ChatBase;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Audio {

    public boolean isRecording = false;
    public TargetDataLine targetDataLine;
    public SourceDataLine sourceDataLine;
    private final byte[] tempBuffer = new byte[500];
    protected byte[] readData;

    public Audio() {
        getMicAndSpeaker();
    }

    public void setRecording(boolean recording) {
        this.isRecording = recording;
        if(!isRecording && targetDataLine != null){
            targetDataLine.stop();
            targetDataLine.drain();
        }
    }

    public byte[] getRecordedAudio() {
        return readData;
    }

    public void recordAudio() {
        setRecording(true);
        new RecordAudioHandler(tempBuffer, tempBuffer.length);
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    private void getMicAndSpeaker() {
        try {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();    //get available mixers
            //System.out.println("Available mixers:");
            Mixer mixer = null;
            for (Mixer.Info info : mixerInfo) {
                //System.out.println(cnt + " " + mixerInfo[cnt].getName());
                mixer = AudioSystem.getMixer(info);

                Line.Info[] lineInfos = mixer.getTargetLineInfo();
                if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                    //System.out.println(cnt + " Mic is supported!");
                    break;
                }
            }

            AudioFormat audioFormat = getAudioFormat();     //get the audio format
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start(); // mic

            DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start(); // speaker
        } catch (LineUnavailableException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    class RecordAudioHandler extends Thread {
        private final byte[] buffer;
        private final int bufferSize;

        RecordAudioHandler(byte[] buffer, int bufferSize) {
            this.buffer = buffer;
            this.bufferSize = bufferSize;
            start();
        }

        @Override
        public void run() {
            if(!targetDataLine.isActive())
                targetDataLine.start();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                int timeout = 0;
                while (isRecording) {
                    int numBytesRead = targetDataLine.read(buffer, 0, bufferSize);
                    if (numBytesRead > 0) {
                        byteArrayOutputStream.write(tempBuffer, 0, numBytesRead);
                    } else {
                        if(++timeout >= 3)
                            setRecording(false);
                    }
                }
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            readData = byteArrayOutputStream.toByteArray();
            System.out.println("Finished recording! Bytes read: " + readData.length);
        }
    }
}