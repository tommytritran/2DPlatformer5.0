package Model;

import java.util.ArrayList;

public class Physics {

    final private double gravitySpeed = 0.75; //def 0.75
    final private double movSpeed     = 0.6; //def 0.6
    final private double decayAmount  = 0.4; //def 0.4
    final private int    jumpSpeed    = 15; // def 10

    boolean hasJumped = false;
    boolean movingLeft = false;
    boolean movingRight = false;

    int tileSize;


    double speedX;
    double speedY;
    double Xpos;
    double Ypos;
    double outXpos;
    double outYpos;
    ArrayList<ArrayList<Integer>> board;

    Physics(ArrayList<ArrayList<Integer>> board, int tileSize, double[] posInfo) {
        this.board = board;
        this.tileSize = tileSize;
        updateValues(posInfo);
        outXpos = Xpos;
        outYpos = Ypos;
    }

    public void updateValues(double[] posInfo){
        this.Xpos   = posInfo[0];
        this.Ypos   = posInfo[1];
        this.speedX = posInfo[2];
        this.speedY = posInfo[3];
    }

    public void setMovingLeft(boolean b){ movingLeft = b; }

    public void setMovingRight(boolean b){ movingRight = b; }

    private void setRightSpeed() {speedX = (speedX > 10) ? 10 : speedX + movSpeed; }

    private void setLeftSpeed() { speedX = (speedX < -10) ? -10 : speedX - movSpeed; }

    private void setGravitySpeed() { speedY = (speedY > 10) ? 10: speedY + gravitySpeed; }

    private void moveX(Double speed) { outXpos += speed; }

    private void moveY(Double speed) { outYpos += speed; }

    private void jump() {
        if(!hasJumped) {
            hasJumped=true;
            speedY -= jumpSpeed;
        }
    }

    private void decaySpeed() {
        if (speedX > decayAmount) {
            speedX -= decayAmount;
        } else if (speedX < -decayAmount) {
            speedX += decayAmount;
        } else if (speedX < decayAmount && speedX > 0) {
            speedX = 0;
        } else if (speedX > -decayAmount && speedX < 0) {
            speedX = 0;
        }
    }

    public void die(){
        outYpos = 400;
        outXpos = 150;
        speedX = speedY = 0;
        System.out.println("Deaths: ");
    }


    private void collisionCheck() {
        double LPos,RPos,UPos,DPos; //Left, Right, Up, Down boundaries of the entity
        LPos = Xpos;
        RPos = Xpos + tileSize;
        UPos = Ypos;
        DPos = Ypos + tileSize;

        if (speedY < 0) { // going up
            collisionUp(UPos,LPos,RPos);
        } else {
            collisionDown(DPos,LPos,RPos);
        }
        if (speedX < 0){ // going left
            collisionLeft(LPos,UPos,DPos);
        } else if (speedX > 0) { //going right
            collisionRight(RPos,UPos,DPos);
        }

    }

    private void collisionUp(double UPos, double LPos, double RPos) {
        int arrayPos = (int)((UPos + (speedY / tileSize))/tileSize);
        RPos -= 10;
        LPos += 10;
        if (board.get(arrayPos).get((int)(LPos/tileSize)) == 1 || //x = -L+R y = -U+D
                board.get(arrayPos).get((int)(RPos/tileSize)) == 1) {
            System.out.println("COLLISION UP");
            outYpos = (tileSize * ((int) (Ypos+tileSize) / tileSize));
            speedY = 0;
        }


    }

    private void collisionDown(double DPos, double LPos, double RPos) {
        int arrayPos = (int) ((DPos + (speedY / tileSize)) / tileSize);
        LPos += 1;
        RPos -= 1;
        if (board.get(arrayPos).get((int) (LPos / tileSize)) == 1 ||
                board.get(arrayPos).get((int) (RPos / tileSize)) == 1) {

            outYpos = (tileSize * (int) (Ypos / tileSize));
            speedY = 0;
            hasJumped = false;
        }
    }

    private void collisionLeft(double LPos, double UPos, double DPos) {
        int arrayRow = (int) (LPos + (speedX / tileSize)) / tileSize;
        DPos -=10; //buffer for tick-related positioning errors
        UPos +=10;

        if (board.get((int) (UPos / tileSize)).get(arrayRow) == 1 ||
                board.get((int) (DPos / tileSize)).get(arrayRow) == 1) {

            System.out.println("COLLISION LEFT");
            outXpos = (tileSize * ((int)(LPos + tileSize) / tileSize));
            speedX = 0;
        }

    }

    private void collisionRight(double RPos, double UPos, double DPos) {
        int arrayRow = (int) (RPos + (speedX / tileSize)) / tileSize;
        DPos -= 10; //buffer for tick-related positioning errors
        UPos += 10;

        if (board.get((int) (UPos / tileSize)).get(arrayRow) == 1 ||
                board.get((int) (DPos / tileSize)).get(arrayRow) == 1) {

            System.out.println("RPOS " + RPos + "  block : " + RPos/tileSize);
            outXpos = (tileSize * ((int) (RPos / tileSize)-1));
            speedX = 0;
        }
    }

    private double[] positionInfo(){
        return new double[]{outXpos, outYpos, speedX, speedY};
    }

    public double[] calculateNext(double[] posInfo) {
        updateValues(posInfo);
        collisionCheck();



        //speeds are updated
        decaySpeed();
        setGravitySpeed();
        if (movingRight){setRightSpeed();}
        if (movingLeft) {setLeftSpeed();}

        //entity positions are updated
        moveX(speedX);
        moveY(speedY);

        return positionInfo();

    }

    public double[] calculateJump(double[] posInfo){
        updateValues(posInfo);
        collisionCheck();

        //speeds are updated
        jump();
        decaySpeed();
        setGravitySpeed();
        if (movingRight){setRightSpeed();}
        if (movingLeft) {setLeftSpeed();}

        //entity positions are updated
        moveX(speedX);
        moveY(speedY);

        return positionInfo();
    }




}