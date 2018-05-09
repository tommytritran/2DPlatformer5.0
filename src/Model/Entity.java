package Model;


import java.io.Serializable;
import java.util.Arrays;

enum ID {
    Player, Enemy
}

public class Entity implements Serializable {
    protected double posX;
    protected double posY;
    protected double[] posXY;
    protected int size;
    protected ID id;

    protected int count = 15;
    protected int columns = 4;
    protected int offsetX = 0;
    protected int offsetY = 0;
    protected int width = 60;
    protected int height = 60;


    public Entity(ID id) {
        this.id = id;
    }

    public String getTexture(){
        if(this.id == ID.Player){
            return "SpriteSheetCat.png";
        }
        if(this.id == ID.Enemy){
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

    public void setHeight(int height) {
        this.height = height;
    }
}
