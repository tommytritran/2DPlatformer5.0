package Model;


import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class Sprite extends Transition {
    ImageView sprite;
    Entity e;
    Pane pane;

    Physics physics;

    private boolean movingLeft = false;
    private boolean movingRight = false;

    private double[] currPos;
    private int offsetX;
    private int offsetY;
    private int columns;
    private int count;
    private int width;
    private int height;
    private int lastIndex;

    public Sprite(Pane gamePane, Entity e, ArrayList<ArrayList<Integer>> board) {
        this.e = e;
        this.offsetX = e.getOffsetX();
        this.offsetY = e.getOffsetY();
        this.columns = e.getColumns();
        this.count = e.getCount();
        this.width = e.getWidth();
        this.height = e.getHeight();
        currPos = e.getPosXY();
        this.pane = gamePane;
        initSprite();
        this.physics = new Physics(board, e.getPosXY());
        setCycleDuration(Duration.millis(1000));
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        final int index = Math.min((int) Math.floor(e.getCount() * frac), e.getCount() - 1);
        if (index != lastIndex) {
            final int x = (index % e.getColumns()) * e.getWidth() + e.getOffsetX();
            final int y = (index / e.getColumns()) * e.getHeight() + e.getOffsetY();
            sprite.setViewport(new Rectangle2D(x, y, e.getWidth(), e.getHeight()));
            lastIndex = index;
        }
    }

    public void initSprite() {
        sprite = new ImageView(new File(e.getTexture()).toURI().toString());
        sprite.setFitHeight(50);
        sprite.setFitWidth(50);
        sprite.setViewport(new Rectangle2D(e.getOffsetX(), e.getOffsetY(), e.getWidth(), e.getHeight()));
        offsetX = e.getOffsetX();
        offsetY = e.getOffsetY();
        currPos = e.getPosXY();
        render();
        pane.getChildren().addAll(sprite);
    }

    public void render() {
        sprite.setTranslateX(e.getPosX());
        sprite.setTranslateY(e.getPosY());
    }

    public void setEntityPosInfo(double[] pos) {
        e.setPosXY(pos);
    }

    public double[] getEntityCurrPos() {
        e.setPosX(sprite.getTranslateX());
        e.setPosY(sprite.getTranslateY());
        return e.getPosXY();
    }

    public Entity getEntity(){
        return e;
    }
    public ID getID() {
        return e.getId();
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void clearSprite() {
        pane.getChildren().remove(this.sprite);
    }

    public void setIdleAnimation() {
        if (e.getId() == ID.Player) {
            e.setCount(4);
            e.setColumns(4);
            e.setOffsetX(0);
            e.setOffsetY(0);
        }
    }

    public void walkRight() {
        if (e.getId() == ID.Player) {
            e.setCount(4);
            e.setColumns(4);
            e.setOffsetX(0);
            e.setOffsetY(64);
        }
    }

    public void walkLeft() {
        if (e.getId() == ID.Player) {
            e.setCount(6);
            e.setColumns(6);
            e.setOffsetX(0);
            e.setOffsetY(64*2);
        }
    }

    public void animateJump() {
        if (e.getId() == ID.Player) {
            e.setCount(1);
            e.setColumns(1);
            e.setOffsetX(0);
            e.setOffsetY(64*3);
        }
    }

    public void die() {
        e.setDeathCounter(e.getDeathCounter()+1);
        physics.die();
        System.out.println("die");
        setIdleAnimation();
    }

    public void deathAnimation() {
        e.setColumns(3);
        e.setCount(3);
        e.setOffsetY(64*4);
        
    }

    public void powerUp1() {
        System.out.println("powerUP1");
        e.setCount(2);
        e.setColumns(2);
        e.setOffsetY(64*5);
        physics.powerUP1();
    }

    public void setMovingLeft(boolean b) {
        movingLeft = b;
    }

    public void setMovingRight(boolean b) {
        movingRight = b;
    }

    public boolean getMovingLeft() {
        return movingLeft;
    }

    public boolean getMovingRight() {
        return movingRight;
    }

    public boolean getHasJumped() {
        return physics.getHasJumped();
    }

    public void updatePosition() {
        setEntityPosInfo(
                physics.calculateNext(
                        getEntityCurrPos()));
    }

    public void jump() {
        setEntityPosInfo(
                physics.calculateJump(
                        getEntityCurrPos()));
    }

    private class Physics {

        final private double GRAVITY = 0.75;     //def 0.75
        final private double ACCELERATION = 0.6; //def 0.6
        final private double DECAY = 0.4;        //def 0.4
        private int jumpSpeed = 15;     //def 15
        final private int MAXSPEED = 10;      //def 10
        final private int TILESIZE = 50;      //def 50

        boolean hasJumped = false;


        double speedX;
        double speedY;
        double Xpos;
        double Ypos;
        double outXpos;
        double outYpos;
        ArrayList<ArrayList<Integer>> board;

        Physics(ArrayList<ArrayList<Integer>> board, double[] posInfo) {
            this.board = board;
            updateValues(posInfo);
            outXpos = Xpos;
            outYpos = Ypos;
        }

        public void updateValues(double[] posInfo) {
            this.Xpos = posInfo[0];
            this.Ypos = posInfo[1];
            this.speedX = posInfo[2];
            this.speedY = posInfo[3];
        }


        private void setRightSpeed() {
            speedX = (speedX > MAXSPEED) ? MAXSPEED : speedX + ACCELERATION;
        }

        private void setLeftSpeed() {
            speedX = (speedX < -MAXSPEED) ? -MAXSPEED : speedX - ACCELERATION;
        }

        private void setGravitySpeed() {
            speedY = (speedY > MAXSPEED) ? MAXSPEED : speedY + GRAVITY;
        }

        private void moveX(Double speed) {
            outXpos += speed;
        }

        private void moveY(Double speed) {
            outYpos += speed;
        }

        public boolean getHasJumped() {
            return hasJumped;
        }

        private void jump() {
            if (!hasJumped) {
                hasJumped = true;
                speedY -= jumpSpeed;
            }
        }

        private void decaySpeed() {
            if (speedX > DECAY) {
                speedX -= DECAY;
            } else if (speedX < -DECAY) {
                speedX += DECAY;
            } else if (speedX < DECAY && speedX > 0) {
                speedX = 0;
            } else if (speedX > -DECAY && speedX < 0) {
                speedX = 0;
            }
        }

        private void collisionCheck() {
            double LPos, RPos, UPos, DPos; //Left, Right, Up, Down boundaries of the entity
            LPos = Xpos;
            RPos = Xpos + TILESIZE;
            UPos = Ypos;
            DPos = Ypos + TILESIZE;

            try {
                if (speedY < 0) { // going up
                    collisionUp(UPos, LPos, RPos);
                } else {
                    collisionDown(DPos, LPos, RPos);
                }
                if (speedX < 0) { // going left
                    collisionLeft(LPos, UPos, DPos);
                } else if (speedX > 0) { //going right
                    collisionRight(RPos, UPos, DPos);
                }
            } catch (IndexOutOfBoundsException e) {

                if (outYpos > 600 && outXpos > 0) {
                    die();
                }
                if (outXpos < 0) {
                    outXpos = 200;
                }

            }
        }

        public void die() {
            outXpos = e.getStartingX();
            outYpos = e.getStartingY();
            speedX = speedY = 0;
        }

        private void collisionUp(double UPos, double LPos, double RPos) {
            int arrayPos = (int) ((UPos + (speedY / TILESIZE)) / TILESIZE);
            RPos -= 10;
            LPos += 10;
            if (board.get(arrayPos).get((int) (LPos / TILESIZE)) == 1 || //x = -L+R y = -U+D
                    board.get(arrayPos).get((int) (RPos / TILESIZE)) == 1) {
                outYpos = (TILESIZE * ((int) (Ypos + TILESIZE) / TILESIZE));
                speedY = 0;
            }


        }

        private void collisionDown(double DPos, double LPos, double RPos) {
            int arrayPos = (int) ((DPos + (speedY / TILESIZE)) / TILESIZE);
            LPos += 1;
            RPos -= 1;
            if (board.get(arrayPos).get((int) (LPos / TILESIZE)) == 1 ||
                    board.get(arrayPos).get((int) (RPos / TILESIZE)) == 1) {

                outYpos = (TILESIZE * (int) (Ypos / TILESIZE));
                speedY = 0;
                hasJumped = false;
            }
        }

        private void collisionLeft(double LPos, double UPos, double DPos) {
            int arrayRow = (int) (LPos + (speedX / TILESIZE)) / TILESIZE;
            DPos -= 10; //buffer for tick-related positioning errors
            UPos += 10;

            if (board.get((int) (UPos / TILESIZE)).get(arrayRow) == 1 ||
                    board.get((int) (DPos / TILESIZE)).get(arrayRow) == 1) {
                outXpos = (TILESIZE * ((int) (LPos + TILESIZE) / TILESIZE));
                speedX = 0;
            }

        }

        private void collisionRight(double RPos, double UPos, double DPos) {
            int arrayRow = (int) (RPos + (speedX / TILESIZE)) / TILESIZE;
            DPos -= 10; //buffer for tick-related positioning errors
            UPos += 10;

            if (board.get((int) (UPos / TILESIZE)).get(arrayRow) == 1 ||
                    board.get((int) (DPos / TILESIZE)).get(arrayRow) == 1) {

                outXpos = (TILESIZE * ((int) (RPos / TILESIZE) - 1));
                speedX = 0;
            }
        }

        private double[] positionInfo() {
            return new double[]{outXpos, outYpos, speedX, speedY};
        }

        public double[] calculateNext(double[] posInfo) {
            updateValues(posInfo);
            collisionCheck();

            //speeds are updated
            decaySpeed();
            setGravitySpeed();
            if (getMovingRight()) {
                setRightSpeed();
            }
            if (getMovingLeft()) {
                setLeftSpeed();
            }

            //entity positions are updated
            moveX(speedX);
            moveY(speedY);


            return positionInfo();

        }

        public double[] calculateJump(double[] posInfo) {
            updateValues(posInfo);
            collisionCheck();

            //speeds are updated
            jump();
            decaySpeed();
            setGravitySpeed();
            if (getMovingRight()) {
                setRightSpeed();
            }
            if (movingLeft) {
                setLeftSpeed();
            }

            //entity positions are updated
            moveX(speedX);
            moveY(speedY);

            return positionInfo();
        }

        public void powerUP1() {
            this.jumpSpeed = 20;
        }
    }

}
