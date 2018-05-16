package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Comments
 * 
 * @author Carlo Nguyen
 * @author Tommy Tran
 * @author Marius Haugen
 */

public class Board implements Serializable {
    private String mapURL;
    private String tileURL = "src/res/block.png";
    private String level = "1";
    private double gameTime = 0;
    private int entitySize = 50;
    private int tileSize = 50;
    ArrayList<Entity> entityList;
    ArrayList<ArrayList<Integer>> mapArray;

    public Board(List<String> level) {
        this.mapURL = level.get(0);
        this.tileURL = level.get(1);
        this.level = level.get(2);
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


    public String getTileURL() {
        return tileURL;
    }


    public int getLevel() {
        return Integer.parseInt(level);
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
