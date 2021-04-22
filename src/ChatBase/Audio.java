package ChatBase;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Audio {

    public boolean isRecording = false;
    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    public TargetDataLine targetDataLine;
    public SourceDataLine sourceDataLine;
    private byte[] tempBuffer = new byte[500];
    protected byte[] readData;

    /*public static void main(String[] args) {
        Audio audio = new Audio();
        audio.captureAndPlayAudio();
    }*/

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

    public void captureAndPlayAudio() {
        Thread captureAndPlayThread = new captureAndPlayThread();   //thread to capture and play audio
        captureAndPlayThread.start();
    }

    public byte[] getRecordedAudio() {
        return readData;
    }

    public void captureAudio() {
        setRecording(true);
        new RecordAudioHandler(tempBuffer, tempBuffer.length);
    }

    /*public void playAudio(byte[] data, int length) {
        new PlayAudioHandler(data, length, sourceDataLine);
    }*/

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
            System.out.println("Available mixers:");
            Mixer mixer = null;
            for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
                System.out.println(cnt + " " + mixerInfo[cnt].getName());
                mixer = AudioSystem.getMixer(mixerInfo[cnt]);

                Line.Info[] lineInfos = mixer.getTargetLineInfo();
                if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                    System.out.println(cnt + " Mic is supported!");
                    break;
                }
            }

            audioFormat = getAudioFormat();     //get the audio format
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

    class captureAndPlayThread extends Thread {

        @Override
        public void run() {
            byteArrayOutputStream = new ByteArrayOutputStream();
            isRecording = false;
            try {
                int readCount;
                while (!isRecording) {
                    readCount = targetDataLine.read(tempBuffer, 0, tempBuffer.length);  //capture sound into tempBuffer
                    if (readCount > 0) {
                        byteArrayOutputStream.write(tempBuffer, 0, readCount);
                        sourceDataLine.write(tempBuffer, 0, 500);   //playing audio available in tempBuffer
                    }
                }
                byteArrayOutputStream.close();
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            }
        }
    }

    class RecordAudioHandler extends Thread {
        private final byte[] buffer;
        private final int numBytesToRead;

        RecordAudioHandler(byte[] buffer, int numBytesToRead) {
            this.buffer = buffer;
            this.numBytesToRead = numBytesToRead;
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
                    int readCount = targetDataLine.read(buffer, 0, numBytesToRead);
                    if (readCount > 0) {
                        byteArrayOutputStream.write(tempBuffer, 0, readCount);
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