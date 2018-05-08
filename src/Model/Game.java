package Model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private Pane pane;
    private Board board;
    private ResourceManager rm;
    private Physics physics;
    private Sprite playerSprite;

    private ArrayList<ArrayList<Integer>> mapArray = new ArrayList<>();
    private ArrayList<Sprite> spriteList = new ArrayList<>();
    private ArrayList<Entity> entityList = new ArrayList<>();

    private List<String> level1 = Arrays.asList("../boardArray.txt", "/res/bg.png", "block.png", "./res/bgsound.wav", "SpriteSheetCat.png", "1");
    private List<String> level2 = Arrays.asList("src/res/boardArray1.txt", "/res/bg.png", "src/res/block.png", "src/res/bgsound.wav", "src/res/SpriteSheetCat.png", "2");

    public Game(Pane gamePane) throws IOException {
        this.pane = gamePane;
        this.board = new Board(level1);
        initGame();
        loadNewGame();
    }
    public Game(Pane gamePane, Board board) throws IOException {
        this.pane = gamePane;
        this.board = board;
        initGame();
        loadGameSave();
    }

    public void initGame() throws IOException {
        rm = new ResourceManager(pane,board);
        this.mapArray = rm.loadMap();
    }
    public void loadNewGame(){
        rm.loadEntity();
        this.spriteList = rm.getSpriteList();
        this.entityList = rm.getEntityList();
        this.playerSprite = rm.getPlayerSprite();
        physics = new Physics(mapArray, board.getTileSize(), playerSprite.getEntityCurrPos());
    }
    public void loadGameSave(){
        this.entityList = board.getEntityList();
        spriteList.clear();
        spriteList = rm.newGameSave(entityList);
        this.playerSprite = rm.getPlayerSprite();
        physics = new Physics(mapArray, board.getTileSize(), playerSprite.getEntityCurrPos());
    }
    public void removeSprite(){
        for(Sprite s : spriteList){
            s.clearSprite();
        }
    }

    public void tick(){
        for (int i = 0; i < spriteList.size(); i++) {
            Sprite sprite = spriteList.get(i);
            if(sprite.getID() == ID.Player){
                sprite.setEntityPosInfo(physics.calculateNext(sprite.getEntityCurrPos()));
            }
        }
    }

    public void render() {
        for (int i = 0; i < spriteList.size(); i++) {
            Sprite sprite = spriteList.get(i);
            if (sprite.getID() == ID.Player){
                sprite.render();
            }
        }
    }


    public void keyDown(KeyEvent e) {
        switch (e.getCode()){
            case LEFT:
                physics.setMovingLeft(true);
                break;
            case RIGHT:
                physics.setMovingRight(true);
                break;
            case UP:
                playerSprite.setEntityPosInfo(physics.calculateJump(playerSprite.getEntityCurrPos()));
                break;
        }
    }

    public void keyUp(KeyEvent e) {
        switch (e.getCode()){
            case LEFT:
                physics.setMovingLeft(false);
                break;
            case RIGHT:
                physics.setMovingRight(false);
        }
    }

    public Board getBoard() {
      this.board = board.saveGame(entityList);
        return board;
    }
}
