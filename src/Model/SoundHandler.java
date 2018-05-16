package Model;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Comments
 * 
 * @author Carlo Nguyen
 * @author Tommy Tran
 * @author Marius Haugen
 */


public class SoundHandler {
    private Board board;
    private URL path;
    private Media media;
    private MediaPlayer mediaPlayer;

    private AudioClip audioClip;

    public SoundHandler(Board board){
        this.board = board;

    }
    public SoundHandler(){}

    public void bgStart(){
        path = getClass().getResource("/res/Arcade-Puzzler.mp3");
        media = new Media(path.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void powerup1(){
        System.out.println("powerup1 sound");
        path = getClass().getResource("/res/powerup1.mp3");
        audioClip = new AudioClip(path.toString());
        audioClip.play();
    }


    public void dieSound() {
        System.out.println("die sound");
        path = getClass().getResource("/res/powerdown1.mp3");
        audioClip = new AudioClip(path.toString());
        audioClip.play();
    }

    public void checkPoint() {
        System.out.println("checkpoint sound");
        path = getClass().getResource("/res/checkpoint.mp3");
        audioClip = new AudioClip(path.toString());
        audioClip.play();
    }
}
