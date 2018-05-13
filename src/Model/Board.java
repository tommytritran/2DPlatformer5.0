package Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private String mapURL;
    private String bgURL;
    private String tileURL;
    private String soundURL;
    private String playerURL;
    private String level;
    private double gameTime;
    private int entitySize = 50;
    private int tileSize = 50;
    private double viewPortX;
    private double viewPortY;
    ArrayList<Entity> entityList;

    public Board(List<String> level) {
        this.mapURL = level.get(0);
        this.bgURL = level.get(1);
        this.tileURL = level.get(2);
        this.soundURL = level.get(3);
        this.playerURL = level.get(4);
        this.level = level.get(5);
        gameTime = 0;
    }

    public Board() {
    }

    public String getPlayerURL() {
        return playerURL;
    }

    public int getTileSize() {
        return tileSize;
    }

    public String getMapURL() {
        return mapURL;
    }

    public String getBgURL() {
        return bgURL;
    }

    public String getTileURL() {
        return tileURL;
    }

    public String getSoundURL() {
        return soundURL;
    }

    public String getLevel() {
        return level;
    }

    public double getViewPortX() {
        return viewPortX;
    }

    public double getViewPortY() {
        return viewPortY;
    }
    public int getEntitySize(){ return entitySize;}

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }


    public Board saveGame(ArrayList<Entity> entityList){
        this.entityList = entityList;
        return this;
    }
    public void setGameTime(Double gameTime){
        this.gameTime = gameTime;
    }
    public double getGameTime(){
        return gameTime;
    }

    public void setMapURL(String url) {
        this.mapURL = url;
    }
}
