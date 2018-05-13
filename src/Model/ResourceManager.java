package Model;


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.URL;
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
    private ArrayList<Rectangle> tileList = new ArrayList<>();

    private Sprite player;

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

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public ArrayList<Sprite> getSpriteList() {
        return spriteList;
    }


    public void removeAll() {
        Iterator<Sprite> iterSprite = spriteList.iterator();
        while (iterSprite.hasNext()) {
            pane.getChildren().remove(iterSprite.next().getSprite());
        }
        Iterator<Rectangle> iterTile = tileList.iterator();
        while (iterTile.hasNext()){
            pane.getChildren().remove(iterTile.next());
        }
        spriteList.clear();
        entityList.clear();
        tileList.clear();
    }


    public void loadEntity() {
        double[] posXY;
        for (int i = 0; i < mapArray.size(); i++) {
            for (int j = 0; j < mapArray.get(i).size(); j++) {
                if (mapArray.get(i).get(j) == 2) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity player = new Entity(ID.Player);
                    player.setPosXY(posXY);
                    player.setStartingPos(posXY);
                    Sprite playerSprite = new Sprite(pane, player, mapArray);
                    this.player = playerSprite;
                    addSprite(playerSprite);
                    addEntity(player);
                }
                if (mapArray.get(i).get(j) == 3) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity enemy1 = new Entity(ID.Enemy);
                    enemy1.setPosXY(posXY);
                    Sprite enemySprite = new Sprite(pane, enemy1, mapArray);
                    addSprite(enemySprite);
                    addEntity(enemy1);
                }
                if (mapArray.get(i).get(j) == 4) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity powerUP1Entity = new Entity(ID.powerUP1);
                    powerUP1Entity.setPosXY(posXY);
                    Sprite powerUP1 = new Sprite(pane, powerUP1Entity, mapArray);
                    addEntity(powerUP1Entity);
                    addSprite(powerUP1);
                }
                if (mapArray.get(i).get(j) == 20) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity checkpoint = new Entity(ID.CheckPoint);
                    checkpoint.setPosXY(posXY);
                    checkpoint.setStartingPos(posXY);
                    Sprite ckp = new Sprite(pane, checkpoint, mapArray);
                    addSprite(ckp);
                    addEntity(checkpoint);
                }
            }
        }
    }

    public ArrayList<Sprite> newGameSave(ArrayList<Entity> entityList) {
        this.entityList.clear();
        this.spriteList.clear();
        this.entityList = entityList;
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i).getId() == ID.Player) {
                Sprite playerSprite = new Sprite(pane, entityList.get(i), mapArray);
                this.player = playerSprite;
                addSprite(playerSprite);
            }
            if (entityList.get(i).getId() == ID.powerUP1) {
                Sprite powerUP1 = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(powerUP1);
            }
            if (entityList.get(i).getId() == ID.Enemy) {
                Sprite enemy = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(enemy);
            }
            if (entityList.get(i).getId() == ID.CheckPoint){
                Sprite checkpoint = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(checkpoint);
            }
        }
        return spriteList;
    }


    public Sprite getPlayerSprite() {
        return this.player;
    }
}
