package Model;


import java.io.Serializable;
import java.util.Arrays;

enum ID {
    Player, Enemy, spike, Enemy3, powerUP1, powerUP2, powerUP3, Tile, CheckPoint
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
            return "src/timmy.png";
        }
        if(this.id == ID.Enemy){
            return "src/enemy1.png";
        }
        if(this.getId() == ID.powerUP1){
            return "src/powerUP1.png";
        }
        if (this.getId() == ID.CheckPoint){
            return "src/pika1.jpg";
        }
        if(this.getId() == ID.spike){
            return "src/spike11.png";
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
        if (this.getId() == ID.spike){
            this.count = 1;
            return count;
        }
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getColumns() {
        if (this.getId() == ID.spike){
            this.columns = 1;
            return columns;
        }
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getOffsetX() {
        if (this.getId() == ID.spike){
            this.offsetX = 0;
            return offsetX;
        }
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        if (this.getId() == ID.spike){
            this.offsetY = 0;
            return offsetY;
        }
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getWidth() {
        if (this.getId() == ID.spike){
            this.width = 32;
            return width;
        }
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        if (this.getId() == ID.spike){
            this.height = 32;
            return height;
        }
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
