package Model;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewPortHandler {
    private Pane pane;

    //Thresholds for the center box in which the player can move freely before the camera starts following
    private final double RIGHT = 500;
    private final double LEFT = 300;
    private final double UP = 200;
    private final double DOWN = 300;
    private final double SMOOTHING = 0.1;

    public ViewPortHandler(Pane pane) {
        this.pane = pane;
    }

    public void tick(double[] playerPos) {

        if (-playerPos[0] < pane.getTranslateX()-RIGHT) {
            pane.setTranslateX(pane.getTranslateX() - (playerPos[0]+(pane.getTranslateX()-RIGHT))*SMOOTHING);
        }

        if (-playerPos[0] > pane.getTranslateX()-LEFT && pane.getTranslateX()<=0) {
            pane.setTranslateX(pane.getTranslateX() - (playerPos[0]+(pane.getTranslateX()-LEFT))*SMOOTHING);
        }

        if (-playerPos[1] > pane.getTranslateY()-UP) {
            pane.setTranslateY(pane.getTranslateY() - (playerPos[1]+(pane.getTranslateY()-UP))*SMOOTHING);
        }

        if (-playerPos[1] < pane.getTranslateY()-DOWN) {
            pane.setTranslateY(pane.getTranslateY() - (playerPos[1]+(pane.getTranslateY()-DOWN))*SMOOTHING);
        }
    }
}
