package utils;

import characters.background.mapCha;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URISyntaxException;

public class musicThread implements Runnable {

    private String filename;

    public musicThread(String wavfile) {
        filename = wavfile;
    }

    public void run() {
        try {
            filename = musicThread.class.getResource("/music/bgm.wav").toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File soundFile = new File(filename);
//        System.out.println(soundFile.getAbsoluteFile());
        //调用音频流
        AudioInputStream ais;
        try {
            ais = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        AudioFormat format = ais.getFormat();
        SourceDataLine auline;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        auline.start();
        int nBytesRead = 0;
        byte[] abData = new byte[1024];
        try {
            while (nBytesRead != -1) {
                nBytesRead = ais.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            auline.drain();
            auline.close();
        }
    }
}
