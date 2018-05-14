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

    private ArrayList<ArrayList<Integer>> mapArray = new ArrayList<>();
    private ArrayList<Sprite> spriteList = new ArrayList<>();
    private ArrayList<Entity> entityList = new ArrayList<>();

    private List<String> level1 = Arrays.asList("src/boardArray.txt", "/res/bg.png", "src/block.png", "/res/Arcade-Puzzler.mp3", "SpriteSheetCat.png", "1");
    private List<String> level2 = Arrays.asList("src/boardArray1.txt", "/res/bg.png", "src/block2.png", "/res/bgsound.wav", "SpriteSheetCat.png", "2");

    public Game(Pane gamePane) throws IOException {
        this.pane = gamePane;
        this.board = new Board(level1);
        initGame();
        loadNewGame();
    }

    public Game(Pane gamePane, Board board) throws IOException {
        this.pane = gamePane;
        this.board = board;
        if (board.getMapArray() != null) {
            loadEditorMap();
        } else {
            initGame();
            loadGameSave();
        }
    }

    public void loadEditorMap() throws IOException {
        rm = new ResourceManager(pane, board);
        soundHandler = new SoundHandler(board);
        rm.renderBoard();
        rm.loadEntity();
        this.entityList = rm.getEntityList();
        this.spriteList = rm.getSpriteList();
        this.playerSprite = rm.getPlayerSprite();
        setPlayerEntity();
        vph = new ViewPortHandler(pane);
        ch = new CollisionHandler(spriteList);

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
            if (sprite.getID() == ID.Enemy) {
                sprite.updatePosition();
                sprite.render();
            }
            if (ch.collCheck(playerSprite, sprite)) {
                if (sprite.getID() == ID.Enemy) {
                    playerSprite.die();
                }
                if (sprite.getID() == ID.powerUP1) {
                    spriteList.remove(sprite);
                    sprite.clearSprite();
                    playerSprite.powerUp1();
                }
                if (sprite.getID() == ID.CheckPoint) {
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
