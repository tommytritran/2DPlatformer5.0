package Model;

import javafx.animation.Animation;
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
    private SoundHandler soundHandler;
    private ViewPortHandler vph;
    private CollisionHandler collisionHandler;
    private Sprite playerSprite;
    private Entity playerEntity;
    private int level;

    private ArrayList<ArrayList<Integer>> mapArray = new ArrayList<>();
    private ArrayList<Sprite> spriteList = new ArrayList<>();
    private ArrayList<Entity> entityList = new ArrayList<>();

    private List<String> level1 = Arrays.asList("src/boardArray.txt", "src/block.png","1");
    private List<String> level2 = Arrays.asList("src/boardArray1.txt", "src/block2.png","2");

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
            if (sprite.getID() == ID.Enemy1) {
                sprite.updatePosition();
                sprite.render();
            }
            if (collisionHandler.collCheck(playerSprite, sprite)) {
                if (sprite.getID() == ID.Enemy1) {
                    playerSprite.die();
                }
                if (sprite.getID() == ID.powerUP1) {
                    spriteList.remove(sprite);
                    sprite.clearSprite();
                    soundHandler.powerup1();
                    playerSprite.powerUp1();
                }
                if (sprite.getID() == ID.Spike){
                    playerSprite.die();
                }
                if (sprite.getID() == ID.CheckPoint) {
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

}
