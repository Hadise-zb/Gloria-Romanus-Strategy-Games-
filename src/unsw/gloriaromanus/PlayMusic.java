package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;




public class PlayMusic {
    private Clip clip;
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
                //long clipTimePosition = clip.getMicrosecondPosition();
                //clip.stop();
                //JOptionPane.showMessageDialog(null, "Press Ok to stop");
            } else {
                System.out.println("Can't find the file");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public void musicplay1() {
        //String filepath = "/home/comp2511-student/comp2511/w11b-hello_java/Music_background/Fate Grand Order.wav";
        //PlayMusic musicObject = new PlayMusic();
        //musicObject.playMusic(filepath);
    }

    public void musicplay2() {
        //String filepath = "/home/comp2511-student/comp2511/w11b-hello_java/Music_background/fate1.7.wav";
        //PlayMusic musicObject = new PlayMusic();
        //musicObject.playMusic(filepath);
    }

    public void music_stop() {
        //long clipTimePosition = clip.getMicrosecondPosition();
        this.clip.stop();
    }

    public static void main(String[] args) {
        String filepath = "/home/comp2511-student/comp2511/w11b-hello_java/src/unsw/gloriaromanus/bensound-creativeminds.wav";
        PlayMusic musicObject = new PlayMusic();
        musicObject.playMusic(filepath);
        musicObject.music_stop();
    }

}
