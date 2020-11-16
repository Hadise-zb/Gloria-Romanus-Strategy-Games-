package unsw.gloriaromanus;

import java.io.File;

import javax.swing.JOptionPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class PlayMusic {
    private Clip clip;
    long clipTimePosition;
    void playMusic(String musiclocation) {
        try{
            File musicPath = new File(musiclocation);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                this.clip = AudioSystem.getClip();
                this.clip.open(audioInput);
                this.clip.start();
                this.clip.loop(Clip.LOOP_CONTINUOUSLY);

                JOptionPane.showMessageDialog(null, "Press Ok to pause");
            
            } else {
                System.out.println("Can't find the file");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void music_stop() {
        //long clipTimePosition = clip.getMicrosecondPosition();
        this.clip.stop();
        this.clipTimePosition = clip.getMicrosecondPosition();
    }

    public void music_continue() {
        this.clip.setMicrosecondPosition(clipTimePosition);
        clip.start();
    }

}
