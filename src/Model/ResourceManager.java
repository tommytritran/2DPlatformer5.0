package Model;


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ResourceManager {
    private Pane pane;
    private Board board;


    private ArrayList<ArrayList<Integer>> mapArray = new ArrayList<>();
    private ArrayList<Entity> entityList = new ArrayList<>();
    private ArrayList<Sprite> spriteList = new ArrayList<>();
    private ArrayList<Rectangle> tileList = new ArrayList<Rectangle>();

    public ResourceManager(Pane pane, Board board) {
        this.pane = pane;
        this.board = board;
    }

    public ArrayList<ArrayList<Integer>> loadMap() throws IOException {
        String currLine;
        BufferedReader br = new BufferedReader(new FileReader(new File(board.getMapURL())));
        while ((currLine = br.readLine()) != null) {
            ArrayList<Integer> row = new ArrayList<>();
            String[] values = currLine.trim().split(" ");
            for (String pos : values) {
                if (!pos.isEmpty()) {
                    int id = Integer.parseInt(pos);
                    row.add(id);
                }
            }
            mapArray.add(row);
        }
        renderBoard();
        return mapArray;
    }

    public void renderBoard() {
        if (!tileList.isEmpty()) {
            Iterator<Rectangle> iter = tileList.iterator();
            while (iter.hasNext()) {
                pane.getChildren().remove(iter.next());
            }
        }
        tileList.clear();
        Image tile = new Image(new File(board.getTileURL()).toURI().toString());
        for (int i = 0; i < mapArray.size(); i++) {
            for (int j = 0; j < mapArray.get(i).size(); j++) {
                if (mapArray.get(i).get(j) == 1) {
                    Rectangle rect = new Rectangle(board.getTileSize(), board.getTileSize());
                    rect.setFill(new ImagePattern(tile));
                    rect.relocate(j * board.getTileSize(), i * board.getTileSize());
                    tileList.add(rect);
                    pane.getChildren().addAll(rect);
                }
            }
        }
    }

    public void addSprite(Sprite sprite) {
        spriteList.add(sprite);
    }
    public void addEntity(Entity entity){ entityList.add(entity); }
    public ArrayList<Entity> getEntityList(){ return entityList; }
    public ArrayList<Sprite> getSpriteList(){ return spriteList; }


    public void removeAllSprite() {
        Iterator<Sprite> iter = spriteList.iterator();
        while (iter.hasNext()){
            System.out.println("test");
            pane.getChildren().remove(iter.next());
        }
    }


    public void loadEntity() {
        double[] posXY;
        for (int i = 0; i < mapArray.size(); i++) {
            for (int j = 0; j < mapArray.get(i).size(); j++) {
                if (mapArray.get(i).get(j) == 2) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0,0};
                    Entity player = new Entity(ID.Player);
                    player.setPosXY(posXY);
                    Sprite playerSprite = new Sprite(pane,player);
                    addSprite(playerSprite);
                    addEntity(player);
                    System.out.println("Player added to list");
                }
               if (mapArray.get(i).get(j) == 3){
                   posXY = new double[] {(j*board.getTileSize()), (i*board.getTileSize()),0,0};
                   Entity enemy1 = new Entity(ID.Enemy);
                   enemy1.setPosXY(posXY);
                   Sprite enemySprite = new Sprite(pane,enemy1);
                   addSprite(enemySprite);
                   addEntity(enemy1);
                   System.out.println("Enemy type 3 added to list");
               }
            }
        }
        System.out.println("entity and sprite list loaded");
    }

    public ArrayList<Sprite> newGameSave(ArrayList<Entity> entityList){
        this.entityList.clear();
        this.spriteList.clear();
        this.entityList = entityList;
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i).getId() == ID.Player){
                Sprite playerSprite = new Sprite(pane, entityList.get(i));
                addSprite(playerSprite);
                System.out.println("Player loaded");
            }
        }
        return spriteList;
    }


    public Sprite getPlayerSprite() {
        for (int i = 0; i < spriteList.size(); i++) {
            Sprite sprite = spriteList.get(i);
            if (sprite.getID() == ID.Player) {
                return sprite;
            }
        }
        return null;
    }
}
