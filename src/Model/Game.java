package Model;

import javafx.animation.Animation;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import javax.swing.text.View;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Game {
    private Pane pane;
    private Board board;
    private ResourceManager rm;
    private SoundHandler soundHandler;
    private ViewPortHandler vph;
    private CollisionHandler ch;
    private Sprite playerSprite;
    private Entity playerEntity;
    private boolean die = false;

    private ArrayList<ArrayList<Integer>> mapArray = new ArrayList<>();
    private ArrayList<Sprite> spriteList = new ArrayList<>();
    private ArrayList<Entity> entityList = new ArrayList<>();

    private List<String> level1 = Arrays.asList("../boardArray.txt", "/res/bg.png", "block.png", "/res/Arcade-Puzzler.mp3", "SpriteSheetCat.png", "1");
    private List<String> level2 = Arrays.asList("../boardArray1.txt", "/res/bg.png", "block2.png", "/res/bgsound.wav", "SpriteSheetCat.png", "2");

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

    public void initNewLevel() throws IOException {
        rm.removeAll();
        this.board = new Board(level2);
        initGame();
        loadNewGame();
    }


    public void initGame() throws IOException {
        rm = new ResourceManager(pane, board);
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
        ch = new CollisionHandler(spriteList);
        startAnimation();
    }

    public void loadGameSave() {
        this.entityList = board.getEntityList();
        spriteList.clear();
        spriteList = rm.newGameSave(entityList);
        this.playerSprite = rm.getPlayerSprite();
        vph = new ViewPortHandler(pane);
        setPlayerEntity();
        ch = new CollisionHandler(spriteList);
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

    public void removeSprite() {
        for (Sprite s : spriteList) {
            s.clearSprite();
        }
    }

    public void tick() throws IOException {

        for (int i = 0; i < spriteList.size(); i++) {
            Sprite sprite = spriteList.get(i);
            if (sprite.getID() == ID.Player) {
                vph.tick(sprite.getEntityCurrPos());
                sprite.updatePosition();
            }
            if (ch.collCheck(playerSprite, sprite)) {
                if (sprite.getID() == ID.Enemy) {
                    playerSprite.deathAnimation();
                }
                if (sprite.getID() == ID.powerUP1){
                    soundHandler.powerup1();
                    spriteList.remove(sprite);
                    sprite.clearSprite();
                    playerSprite.powerUp1();
                }
                if (sprite.getID() == ID.CheckPoint){
                    System.out.println("checkpoint");
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
        switch (e.getCode()){
            case LEFT:
                playerSprite.setMovingLeft(true);
                playerSprite.walkLeft();
                break;
            case RIGHT:
                playerSprite.setMovingRight(true);
                playerSprite.walkRight();
                break;
            case UP:
                if(!playerSprite.getHasJumped()) {
                    playerSprite.animateJump();
                    playerSprite.jump();
                }
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

    public void saveGame(int deathCounter, double time) {
        for (int i = 0; i < entityList.size(); i++) {
            Entity tmp = entityList.get(i);
            if (tmp.getId() == ID.Player)
                tmp.setDeathCounter(deathCounter);
        }
        board.setGameTime(time);
    }

    public double getGameTime() {
        return board.getGameTime();
    }

    public int getDeathCounter() {
        Entity player = playerSprite.getEntity();
        return player.getDeathCounter();
    }

}
