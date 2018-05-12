package Model;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;


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
        path = getClass().getResource("/res/powerup1.mp3");
        audioClip = new AudioClip(path.toString());
        audioClip.play();
    }
}
