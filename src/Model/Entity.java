package Model;


import java.io.Serializable;

enum ID {
    Player, Enemy, Spike, Enemy3, powerUP1, powerUP2, powerUP3, Tile, CheckPoint
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

    protected int lifePoints = 15;


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
            return "src/checkPoint.png";
        }
        if(this.getId() == ID.Spike){
            return "src/spike.png";
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
        if (this.getId() == ID.Spike){
            this.count = 1;
        }
        if(this.getId() == ID.CheckPoint){
            this.count = 4;
        }
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getColumns() {
        if (this.getId() == ID.Spike){
            this.columns = 1;
        }
        if(this.getId() == ID.CheckPoint){
            this.columns = 4;
        }
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getOffsetX() {
        if (this.getId() == ID.Spike || this.getId() == ID.CheckPoint){
            this.offsetX = 0;
        }
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        if (this.getId() == ID.Spike|| this.getId() == ID.CheckPoint){
            this.offsetY = 0;
        }
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getWidth() {
        if (this.getId() == ID.Spike){
            this.width = 32;
        }
        if(this.getId() == ID.CheckPoint){
            this.width = 64;
        }
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        if (this.getId() == ID.Spike){
            this.height = 32;
        }
        if(this.getId() == ID.CheckPoint){
            this.height = 64;
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

    public int getLifePoints() { return lifePoints; }
    public void setLifePoints(int lifePoints) {  this.lifePoints = lifePoints; }

    public void setStartingPos(double[] posXY) {
        this.startingX = posXY[0];
        this.startingY = posXY[1];
    }
}
