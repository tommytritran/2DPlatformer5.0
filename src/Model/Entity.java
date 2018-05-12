package Model;


import java.io.Serializable;
import java.util.Arrays;

enum ID {
    Player, Enemy, Enemy2, Enemy3, powerUP1, powerUP2, powerUP3, Tile, CheckPoint
}

public class Entity implements Serializable {
    protected double posX;
    protected double posY;
    protected double startingX;
    protected double startingY;
    protected double[] posXY;
    protected int size;
    protected ID id;

    protected int count = 4;
    protected int columns = 4;
    protected int offsetX = 0;
    protected int offsetY = 0;
    protected int width = 64;
    protected int height = 64;

    protected int deathCounter = 0;


    public Entity(ID id) {
        this.id = id;
    }

    public String getTexture(){
        if(this.id == ID.Player){
            return "timmy.png";
        }
        if(this.id == ID.Enemy){
            return "enemy1.png";
        }
        if(this.getId() == ID.powerUP1){
            return "powerUP1.png";
        }
        if (this.getId() == ID.CheckPoint){
            return "pika1.jpg";
        }
        return null;
    }
    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double[] getPosXY() {
        posXY[0] = this.posX;
        posXY[1] = this.posY;
        return posXY;
    }

    public void setPosXY(double[] posXY) {
        this.posXY = posXY;
        this.posX = posXY[0];
        this.posY = posXY[1];
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public double getStartingX(){return startingX;}
    public double getStartingY(){return startingY;}
    public void setStartingX(double x){startingX = x;}
    public void setStartingY(double y){startingX = y;}
    public void setHeight(int height) {
        this.height = height;
    }

    public int getDeathCounter() { return deathCounter; }
    public void setDeathCounter(int death) {  this.deathCounter = death; }

    public void setStartingPos(double[] posXY) {
        this.startingX = posXY[0];
        this.startingY = posXY[1];
    }
}
