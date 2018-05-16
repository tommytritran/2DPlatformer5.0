package Model;


import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
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
        if (board.getMapArray() != null) {
            mapArray = board.getMapArray();
        }
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
        br.close();
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
        while (iterTile.hasNext()) {
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
                    Entity enemyEntity1 = new Entity(ID.Enemy1);
                    enemyEntity1.setPosXY(posXY);
                    Sprite enemySprite = new Sprite(pane, enemyEntity1, mapArray);
                    addSprite(enemySprite);
                    addEntity(enemyEntity1);
                }
                if (mapArray.get(i).get(j) == 4) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity enemyEntity2 = new Entity(ID.Enemy2);
                    enemyEntity2.setPosXY(posXY);
                    Sprite enemySprite2 = new Sprite(pane, enemyEntity2, mapArray);
                    addEntity(enemyEntity2);
                    addSprite(enemySprite2);
                }
                if (mapArray.get(i).get(j) == 5) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity spikeEntity;
                    if (board.getLevel() == 1){
                        spikeEntity = new Entity(ID.Spike);
                    }else{
                        spikeEntity = new Entity(ID.Spike11);
                    }
                    spikeEntity.setPosXY(posXY);
                    Sprite spikeSprite = new Sprite(pane, spikeEntity, mapArray);
                    addSprite(spikeSprite);
                    addEntity(spikeEntity);
                }
                if (mapArray.get(i).get(j) == 6) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity powerUPEntity1 = new Entity(ID.powerUP1);
                    powerUPEntity1.setPosXY(posXY);
                    Sprite powerUPSprite1 = new Sprite(pane, powerUPEntity1, mapArray);
                    addEntity(powerUPEntity1);
                    addSprite(powerUPSprite1);
                }
                if (mapArray.get(i).get(j) == 7) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity powerUPEntity2 = new Entity(ID.PowerUP2);
                    powerUPEntity2.setPosXY(posXY);
                    powerUPEntity2.setStartingPos(posXY);
                    Sprite powerUPSprite2 = new Sprite(pane, powerUPEntity2, mapArray);
                    addSprite(powerUPSprite2);
                    addEntity(powerUPEntity2);
                }
                if (mapArray.get(i).get(j) == 8) {
                    posXY = new double[]{(j * board.getTileSize()), (i * board.getTileSize()), 0, 0};
                    Entity checkPointEntity = new Entity(ID.CheckPoint);
                    checkPointEntity.setPosXY(posXY);
                    checkPointEntity.setStartingPos(posXY);
                    Sprite checkPointSprite = new Sprite(pane, checkPointEntity, mapArray);
                    addSprite(checkPointSprite);
                    addEntity(checkPointEntity);
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
            if (entityList.get(i).getId() == ID.PowerUP2) {
                Sprite checkpoint = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(checkpoint);
            }
            if (entityList.get(i).getId() == ID.Enemy1) {
                Sprite enemy1 = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(enemy1);
            }
            if (entityList.get(i).getId() == ID.Enemy2) {
                Sprite enemy2 = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(enemy2);
            }
            if (entityList.get(i).getId() == ID.Spike) {
                Sprite spike = new Sprite(pane, entityList.get(i), mapArray);
                addSprite(spike);
            }
            if (entityList.get(i).getId() == ID.CheckPoint) {
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
