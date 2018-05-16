package Model;


import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

/**
 * This class handles the behaviour of our sprites and animation
 * 
 * @author Carlo Nguyen
 * @author Tommy Tran
 * @author Marius Haugen
 */

public class Sprite extends Transition {
	
	/**
	 *  Objects
	 */
    ImageView sprite;
    Entity e;
    Pane pane;
    Physics physics;

    /**
     * Data field
     */
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
    

    /**
     * This is the constructor of the class Sprite
     * 
     * @param first argument is a JavaFX pane
     * @param second argument is an Entity object
     * @param third argument is the an Array-representation of the gameboard
     */
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
        if (e.getId()== ID.Enemy1 || e.getId() == ID.Enemy2){
            movingRight=true;
            physics.maxSpeed=3;
        }
        setCycleDuration(Duration.millis(1000));
        setInterpolator(Interpolator.LINEAR);
    }

    /**
     * It defines actual behavior of the animation. The method interpolate() is called by the runtime in every frame while the animation is playing. 
     * The value 0.0 marks the start of the animation, the value 1.0 the end.
     *  
     * @param A double value between 0.0 and 1.0 (both inclusive) is passed in, which specifies the current position.
     */
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
    
    /**
     * This method initiates the sprite ImageView object. 
     * Sets the height and width and the starting frame of the spritesheet.
     * Executes render() method and adds the object to pane.
     */
    public void initSprite() {
        sprite = new ImageView(new File(e.getTexture()).toURI().toString());
        sprite.setFitHeight(49);
        sprite.setFitWidth(49);
        sprite.setViewport(new Rectangle2D(e.getOffsetX(), e.getOffsetY(), e.getWidth(), e.getHeight()));
        offsetX = e.getOffsetX();
        offsetY = e.getOffsetY();
        currPos = e.getPosXY();
        render();
        pane.getChildren().addAll(sprite);
    }

    /**
     * This methods sets the X and Y position of the sprite
     */
    public void render() {
        sprite.setTranslateX(e.getPosX());
        sprite.setTranslateY(e.getPosY());
    }

    /**
     * ???
     * 
     * @param pos
     */
    public void setEntityPosInfo(double[] pos) {
        e.setPosXY(pos);
    }

    /**
     * ???
     * 
     * @return
     */
    public double[] getEntityCurrPos() {
        e.setPosX(sprite.getTranslateX());
        e.setPosY(sprite.getTranslateY());
        return e.getPosXY();
    }

    /**
     * @return an object of type Entity
     */
    public Entity getEntity(){
        return e;
    }
    
    /**
     * @return the ID of the respective entity
     */
    public ID getID() {
        return e.getId();
    }

    /**
     * @return an ImageView of the sprite
     */
    public ImageView getSprite() {
        return sprite;
    }

    /**
     * This method removes a sprite from JavaFX pane
     */
    public void clearSprite() {
        pane.getChildren().remove(this.sprite);
    }

    /**
     * This method sets the Idle animation to our player
     */
    public void setIdleAnimation() {
        if (e.getId() == ID.Player) {
            e.setCount(4);
            e.setColumns(4);
            e.setOffsetX(0);
            e.setOffsetY(0);
        }
    }

    /**
     * This method sets the animation of "walking right" to our player
     */
    public void walkRight() {
        if (e.getId() == ID.Player) {
            e.setCount(4);
            e.setColumns(4);
            e.setOffsetX(0);
            e.setOffsetY(64);
        }
    }

    /**
     * This method sets the animation of "walking left" to our player
     */
    public void walkLeft() {
        if (e.getId() == ID.Player) {
            e.setCount(6);
            e.setColumns(6);
            e.setOffsetX(0);
            e.setOffsetY(64*2);
        }
    }

    /**
     * This method sets the animation of "jump" to our player
     */    
    public void animateJump() {
        if (e.getId() == ID.Player) {
            e.setCount(1);
            e.setColumns(1);
            e.setOffsetX(0);
            e.setOffsetY(64*3);
        }
    }
    
    public void die() {
            physics.die();
            setIdleAnimation();
    }

    public void deathAnimation() {
        e.setColumns(3);
        e.setCount(3);
        e.setOffsetY(64*4);
        
    }
    
    /**
     * This method gets the current life points to our player and returns the value
     * @return the value points of life to our player
     */
    public int getLifePoints(){
        return e.getLifePoints();
    }
    
    /**
     * This method sets the jumpspeed to 20 when player consumes an item ingame
     */
    public void powerUP1() {
        physics.jumpSpeed = 20;
    }
    
    /**
     * This method sets(increases) the maxspeed and acceleration when player consumes an item ingame
     */
    public void powerUP2() {
        physics.maxSpeed = 15;
        physics.acceleration = 0.8;
    }

    /**
     * This method sets a boolean value to movingLeft
     * @param b
     */
    public void setMovingLeft(boolean b) {
        movingLeft = b;
    }

    /**
     * This method sets a boolean value to movingRight
     * @param b
     */
    public void setMovingRight(boolean b) {
        movingRight = b;
    }

    /**
     * This method gets the boolean value to movingLeft
     * @param b
     */
    public boolean getMovingLeft() {
        return movingLeft;
    }

    /**
     * This method gets the boolean value to movingLeft
     * @param b
     */
    public boolean getMovingRight() {
        return movingRight;
    }


    public void updatePosition() {
        setEntityPosInfo(
                physics.calculateNext(
                        getEntityCurrPos()));
    }

    public void jump() {
        physics.jumpNextFrame = true;
    }

    /**
     * This class handles the behaviour of our sprites as well as physics
     * 
     * @author Carlo Nguyen
     * @author Tommy Tran
     * @author Marius Haugen
     */
    
    private class Physics {

        final private double GRAVITY = 0.75;     //def 0.75
        final private double DECAY = 0.4;        //def 0.4
        final private int TILESIZE = 50;      //def 50
        final private int BUFFER = 10;

        private double acceleration = 0.6; //def 0.6
        private int jumpSpeed = 18;     //def 15
        private int maxSpeed = 10;      //def 10
        private boolean jumpNextFrame = false;
        private boolean hasJumped = false;
        private boolean hasCollided = false;

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

        void updateValues(double[] posInfo) {
            this.Xpos = posInfo[0];
            this.Ypos = posInfo[1];
            this.speedX = posInfo[2];
            this.speedY = posInfo[3];
        }

        private double[] positionInfo() {
            return new double[]{outXpos, outYpos, speedX, speedY};
        }


        private void updateRightSpeed() {
            speedX = (speedX > maxSpeed) ? maxSpeed : speedX + acceleration;
        }

        private void updateLeftSpeed() {
            speedX = (speedX < -maxSpeed) ? -maxSpeed : speedX - acceleration;
        }

        private void updateGravitySpeed() {
            speedY = (speedY > maxSpeed) ? maxSpeed : speedY + GRAVITY;
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

        void die() {
            if (e.getId() == ID.Player){
                e.setLifePoints(e.getLifePoints()-1);
                outXpos = e.getStartingX();
                outYpos = e.getStartingY();
                speedX = speedY = 0;
            }
        }


        private void collisionUp(double UPos, double LPos, double RPos) {
            int arrayPos = (int) ((UPos + (speedY / TILESIZE)) / TILESIZE);
            if (board.get(arrayPos).get((int) (LPos / TILESIZE)) == 1 || //x = -L+R y = -U+D
                    board.get(arrayPos).get((int) (RPos / TILESIZE)) == 1) {
                outYpos = (TILESIZE * ((int) (Ypos + TILESIZE) / TILESIZE));
                speedY = 0;
            }
        }

        private void collisionDown(double DPos, double LPos, double RPos) {
            int arrayPos = (int) ((DPos + (speedY / TILESIZE)) / TILESIZE);
            if (board.get(arrayPos).get((int) (LPos / TILESIZE)) == 1 ||
                    board.get(arrayPos).get((int) (RPos / TILESIZE)) == 1) {
                outYpos = (TILESIZE * (int) (Ypos / TILESIZE));
                speedY = 0;
                hasJumped = false;
            }
        }

        private boolean collisionLeft(double LPos, double UPos, double DPos) {
            int arrayRow = (int) (LPos + (speedX / TILESIZE)) / TILESIZE;

            if (board.get((int) (UPos / TILESIZE)).get(arrayRow) == 1 ||
                    board.get((int) (DPos / TILESIZE)).get(arrayRow) == 1) {
                outXpos = (TILESIZE * ((int) (LPos + TILESIZE) / TILESIZE));
                speedX = 0;
                return true;
            }
            return false;
        }

        private boolean collisionRight(double RPos, double UPos, double DPos) {
            int arrayRow = (int) (RPos + (speedX / TILESIZE)) / TILESIZE;
            if (board.get((int) (UPos / TILESIZE)).get(arrayRow) == 1 ||
                    board.get((int) (DPos / TILESIZE)).get(arrayRow) == 1) {
                outXpos = (TILESIZE * ((int) (RPos / TILESIZE) - 1));
                speedX = 0;
                return true;
            }
            return false;
        }


        double[] calculateNext(double[] posInfo) {
            //imports position values from entity object
            updateValues(posInfo);

            //checks collisions
            try {
                if (speedY < 0) { // going up
                    collisionUp(Ypos, Xpos + BUFFER, Xpos + TILESIZE - BUFFER);
                } else {
                    collisionDown(Ypos + TILESIZE,Xpos + BUFFER, Xpos + TILESIZE - BUFFER);
                }
                if (speedX < 0) { // going left
                    hasCollided = collisionLeft(Xpos, Ypos + BUFFER, Ypos + TILESIZE- BUFFER);
                } else if (speedX > 0) { //going right
                    hasCollided = collisionRight(Xpos + TILESIZE, Ypos + BUFFER, Ypos + TILESIZE - BUFFER);
                }
            } catch (IndexOutOfBoundsException e) {
                if (outYpos > 600 || outXpos < 0 ) { //To the left or below the map
                    die();
                }
            }
            if (e.getId() == ID.Enemy1 && hasCollided || e.getId() == ID.Enemy2 && hasCollided){
                swapDirection();
                if(e.getOffsetY() == 64 ) {
                    e.setOffsetY(0);
                } else {
                    e.setOffsetY(64);
                }
            }
            //speeds are updated
            decaySpeed();
            updateGravitySpeed();
            if (jumpNextFrame) {
                jumpNextFrame = false;
                jump();}

            if (getMovingRight()){ updateRightSpeed();}
            if (getMovingLeft()) { updateLeftSpeed();}

            //entity positions are updated with delta position
            outXpos += speedX;
            outYpos += speedY;


            return positionInfo();
        }

        private void swapDirection() {
            movingLeft = !movingLeft;
            movingRight = !movingRight;
        }

    }

}
