package Model;


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        System.out.println(new File(board.getTileURL()).toURI().toString());
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
                    player.setStartingPos(posXY);
                    Sprite playerSprite = new Sprite(pane,player, mapArray);
                    addSprite(playerSprite);
                    addEntity(player);
                    System.out.println("Player added to list");
                }
               if (mapArray.get(i).get(j) == 3){
                   posXY = new double[] {(j*board.getTileSize()), (i*board.getTileSize()),0,0};
                   Entity enemy1 = new Entity(ID.Enemy);
                   enemy1.setPosXY(posXY);
                   Sprite enemySprite = new Sprite(pane,enemy1, mapArray);
                   addSprite(enemySprite);
                   addEntity(enemy1);
                   System.out.println("Enemy type 3 added to list");
               }
               if(mapArray.get(i).get(j) == 4){
                   posXY = new double[] {(j*board.getTileSize()), (i*board.getTileSize()),0,0};
                   Entity powerUP1Entity = new Entity(ID.powerUP1);
                   powerUP1Entity.setPosXY(posXY);
                   Sprite powerUP1 = new Sprite(pane,powerUP1Entity, mapArray);
                   addEntity(powerUP1Entity);
                   addSprite(powerUP1);
               }
            }
        }

        spriteList.sort((s1, s2) -> {
            if(s1.getID() == ID.Player) return -1;
            if(s2.getID() == ID.Player) return 1;
            return 0;
        });
        System.out.println("entity and sprite list loaded");
    }

    public ArrayList<Sprite> newGameSave(ArrayList<Entity> entityList){
        this.entityList.clear();
        this.spriteList.clear();
        this.entityList = entityList;
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i).getId() == ID.Player){
                Sprite playerSprite = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(playerSprite);
                System.out.println("Player loaded");
            }
            if(entityList.get(i).getId() == ID.powerUP1){
                Sprite powerUP1 = new Sprite(pane, entityList.get(i),mapArray);
                addSprite(powerUP1);
            }
            if(entityList.get(i).getId() == ID.Enemy){
                Sprite enemy = new Sprite(pane, entityList.get(i),mapArray);
                addSprite(enemy);
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
