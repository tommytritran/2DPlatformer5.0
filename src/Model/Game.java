package Model;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private Pane pane, mainPane;
    private Board board;
    private ResourceManager rm;
    private SoundHandler soundHandler;
    private ViewPortHandler vph;
    private CollisionHandler collisionHandler;
    private Sprite playerSprite;
    private Entity playerEntity;
    private int level;
    private String bg1 = "/res/bg.png";
    private String bg2 = "/res/bg2.png";

    private ArrayList<ArrayList<Integer>> mapArray = new ArrayList<>();
    private ArrayList<Sprite> spriteList = new ArrayList<>();
    private ArrayList<Entity> entityList = new ArrayList<>();

    private List<String> level1 = Arrays.asList("src/boardArray.txt", "src/res/block.png","1");
    private List<String> level2 = Arrays.asList("src/boardArray1.txt", "src/res/block2.png","2");

    public Game(Pane gamePane, Pane mainPane) throws IOException {
        this.pane = gamePane;
        this.mainPane = mainPane;
        this.board = new Board(level2);
        this.level = board.getLevel();
        initGame();
        loadNewGame();
    }

    public Game(Pane gamePane, Pane mainPane,Board board) throws IOException {
        this.pane = gamePane;
        this.mainPane = mainPane;
        this.board = board;
        if (board.getMapArray() != null) {
            loadEditorMap();
        } else {
            initGame();
            loadGameSave();
        }
    }

    public void loadEditorMap() {
        rm = new ResourceManager(pane, board);
        soundHandler = new SoundHandler(board);
        rm.renderBoard();
        rm.loadEntity();
        this.entityList = rm.getEntityList();
        this.spriteList = rm.getSpriteList();
        this.playerSprite = rm.getPlayerSprite();
        setPlayerEntity();
        vph = new ViewPortHandler(pane);
        collisionHandler = new CollisionHandler(spriteList);
        startAnimation();
    }

    public void initNewLevel() throws IOException {
        rm.removeAll();
        this.board = new Board(level2);
        if(board.getLevel() == 1){
            setBG(bg1);
        }
        if (board.getLevel() == 2){
            setBG(bg2);
        }
        initGame();
        loadNewGame();
    }


    public void initGame() throws IOException {
        rm = new ResourceManager(pane, board);
        if(board.getLevel() == 1){
            setBG(bg1);
        }
        if (board.getLevel() == 2){
            setBG(bg2);
        }
        this.mapArray = rm.loadMap();
        soundHandler = new SoundHandler(board);
    }

    public void loadNewGame() {
        rm.loadEntity();
        this.spriteList = rm.getSpriteList();
        this.entityList = rm.getEntityList();
        this.playerSprite = rm.getPlayerSprite();
        vph = new ViewPortHandler(pane);
        setPlayerEntity();
        collisionHandler = new CollisionHandler(spriteList);
        startAnimation();
    }

    public void loadGameSave() {
        this.entityList = board.getEntityList();
        spriteList.clear();
        spriteList = rm.newGameSave(entityList);
        this.playerSprite = rm.getPlayerSprite();
        vph = new ViewPortHandler(pane);
        setPlayerEntity();
        collisionHandler = new CollisionHandler(spriteList);
        startAnimation();
    }

    public void startAnimation() {
        //loope spritelisten og init animation.play
        for (int i = 0; i < spriteList.size(); i++) {
            Sprite sprite = spriteList.get(i);
            sprite.setCycleCount(Animation.INDEFINITE);
            sprite.play();
        }
    }

    public void setPlayerEntity() {
        for (int i = 0; i < entityList.size(); i++) {
            Entity currEntity = entityList.get(i);
            if (currEntity.getId() == ID.Player) {
                this.playerEntity = currEntity;
            }
        }
    }

    public void removeAll() {
        rm.removeAll();
    }

    public void tick() throws IOException {
        for (int i = 0; i < spriteList.size(); i++) {
            Sprite sprite = spriteList.get(i);
            if (sprite.getID() == ID.Player) {
                vph.tick(sprite.getEntityCurrPos());
                sprite.updatePosition();
                sprite.render();
            }
            if (sprite.getID() == ID.Enemy1 || sprite.getID() == ID.Enemy2) {
                sprite.updatePosition();
                sprite.render();
            }
            if (collisionHandler.collCheck(playerSprite, sprite)) {
                if (sprite.getID() == ID.Enemy1 || sprite.getID() == ID.Enemy2) {
                    soundHandler.dieSound();
                    playerSprite.die();
                }
                if (sprite.getID() == ID.powerUP1) {
                    spriteList.remove(sprite);
                    sprite.clearSprite();
                    soundHandler.powerup1();
                    playerSprite.powerUP1();
                }
                if (sprite.getID() == ID.PowerUP2) {
                    spriteList.remove(sprite);
                    sprite.clearSprite();
                    soundHandler.powerup1();
                    playerSprite.powerUP2();
                }
                if (sprite.getID() == ID.Spike){
                    soundHandler.dieSound();
                    playerSprite.die();
                }
                if (sprite.getID() == ID.CheckPoint) {
                    soundHandler.checkPoint();
                    initNewLevel();
                }
            }
        }
    }

    public void render() {
        for (int i = 0; i < spriteList.size(); i++) {
            Sprite sprite = spriteList.get(i);
            if (sprite.getID() == ID.Player) {
                sprite.render();
            }
        }
    }

    public void keyDown(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT:
                playerSprite.setMovingLeft(true);
                playerSprite.walkLeft();
                break;
            case RIGHT:
                playerSprite.setMovingRight(true);
                playerSprite.walkRight();
                break;
            case UP:
                playerSprite.animateJump();
                playerSprite.jump();
                break;
        }
    }

    public void keyUp(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT:
                playerSprite.setIdleAnimation();
                playerSprite.setMovingLeft(false);
                break;
            case RIGHT:
                playerSprite.setMovingRight(false);
                playerSprite.setIdleAnimation();
                break;
            default:
                playerSprite.setIdleAnimation();
        }
    }


    public Board getBoard() {
        this.board = board.saveGame(entityList);
        return board;
    }

    public void saveGame(int lifePoints, double time) {
        for (int i = 0; i < entityList.size(); i++) {
            Entity tmp = entityList.get(i);
            if (tmp.getId() == ID.Player)
                tmp.setLifePoints(lifePoints);
        }
        board.setGameTime(time);
    }

    public double getGameTime() {
        return board.getGameTime();
    }

    public int getLifePoints() {
        return playerSprite.getLifePoints();
    }

    public void setBG(String path){
        BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResource(path).toString(), 800, 500, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(myBI);
        mainPane.setBackground(bg);
    }

}
