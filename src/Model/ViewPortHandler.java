package Model;

import javafx.scene.layout.Pane;

/**
 * This class handles the camera/viewport
 * 
 * @author Carlo Nguyen
 * @author Tommy Tran
 * @author Marius Haugen
 */

public class ViewPortHandler {
    private Pane pane;

    private final double RIGHT = 500;
    private final double LEFT = 300;
    private final double UP = 200;
    private final double DOWN = 300;
    private final double SMOOTHING = 0.1;

    public ViewPortHandler(Pane pane) {
        this.pane = pane;
    }

    /**
     * This method consistently seeks the position of the player. 
     * Thresholds for the center box in which the player can move freely before the camera starts following
     * 
     * @param the position of the player
     */
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
