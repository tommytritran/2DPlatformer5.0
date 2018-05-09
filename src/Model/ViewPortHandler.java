package Model;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewPortHandler {
    private double left;
    private double right;
    private double down;
    private double up;
    private Pane pane;
    public ViewPortHandler(Pane pane, double x, double y){
        this.pane = pane;
        pane.setTranslateX(x);
        pane.setTranslateY(y);
        left = x;
        right = x+800;
        down = y+500;
        up = y;
    }

    public void tick(double[] playerPos){

        if(playerPos[0] >= right-200){
            pane.setTranslateX(pane.getTranslateX()-playerPos[2]);
            right += playerPos[2];
            left += playerPos[2];
        }
        if(playerPos[0] <= left+150){
            pane.setTranslateX(pane.getTranslateX()-playerPos[2]);
            right += playerPos[2];
            left += playerPos[2];
        }
        if(playerPos[1] <= up+150){
            System.out.println("Up triggered");
            pane.setTranslateY(pane.getTranslateY()-playerPos[3]);
            up += playerPos[3];
            down += playerPos[3];
        }
        System.out.println("Pos Y: " + playerPos[1] + " down: " + down);
        if(playerPos[1] >= down-190 && playerPos[1] < 375){
            System.out.println("down trigger " + playerPos[3]);
            pane.setTranslateY(pane.getTranslateY()-playerPos[3]);
            up += playerPos[3];
            down += playerPos[3];
        }else if(playerPos[1] >= 300){
            pane.setTranslateY(0);
            up = 0;
            down = 500;
        }
    }
}
