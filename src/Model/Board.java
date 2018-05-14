package Model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private String mapURL;
    private String bgURL;
    private String tileURL = "src/block.png";
    private String soundURL;
    private String level;
    private double gameTime;
    private int entitySize = 50;
    private int tileSize = 50;
    ArrayList<Entity> entityList;
    ArrayList<ArrayList<Integer>> mapArray;

    public Board(List<String> level) {
        this.mapURL = level.get(0);
        this.bgURL = level.get(1);
        this.tileURL = level.get(2);
        this.soundURL = level.get(3);
        this.level = level.get(5);
        gameTime = 0;
    }

    public Board() {
    }


    public void setMapArray(ArrayList<ArrayList<Integer>> mapArray){
        this.mapArray = mapArray;
    }
    public ArrayList<ArrayList<Integer>> getMapArray(){
        return this.mapArray;
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
