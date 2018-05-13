package Controller;

import Model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Pane mainPane, startPane, menuPane, gamePane, HUD, gameOverPane;
    @FXML
    Group group;
    @FXML
    Label deathCounterLabel, timerLabel;
    private SoundHandler soundHandler;
    private int deathCounter = 0;
    private double startTime = 0;
    private double endTime = 0;
    private double gameSaveTime = 0;
    public Board board;
    public Game game;
    public boolean running = false;
    public boolean newGame = true;
    AnimationTimer aTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Game started");
        soundHandler = new SoundHandler();
        soundHandler.bgStart();
        group.setFocusTraversable(true);
        group.requestFocus();
        setBG();
        mainPane.setVisible(true);
        menuPane.setVisible(false);
        gamePane.setVisible(false);
        gameOverPane.setVisible(false);
        HUD.setVisible(false);
        startPane.setVisible(true);
        keyInputHandler();
    }

    public void newGame() throws IOException {
        if (aTimer != null){
            game.removeAll();
        }
        if(newGame && !running){
            newGame = false;
            running = true;
            startPane.setVisible(false);
            gamePane.setVisible(true);
        }
        if(!running){
            running = true;
            gameOverPane.setVisible(false);
            menuPane.setVisible(false);
            gamePane.setVisible(true);
        }
        System.out.println("New game started");
        game = new Game(gamePane);
        startGame();
    }

    public void startGame() {
        startTime = System.nanoTime();
        deathCounter = game.getDeathCounter();
        setTime();
        System.out.println("Starting game loop");
        HUD.setVisible(true);
        aTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    game.tick();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                game.render();
                HUDhandler();
            }
        };
        aTimer.start();
    }

    public void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void keyInputHandler() {

        group.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.P) {

            }
            if (e.getCode() == KeyCode.L) {

            }
            if (e.getCode() == KeyCode.ESCAPE) {
                if (running) {
                    gamePane.setVisible(false);
                    menuPane.setVisible(true);
                    aTimer.stop();
                    running = !running;
                } else {
                    aTimer.start();
                    menuPane.setVisible(false);
                    gamePane.setVisible(true);
                    running = !running;
                }

            }
            game.keyDown(e);
        });
        group.setOnKeyReleased(e -> game.keyUp(e));
    }

    public void saveGame() {
        try {
            FileOutputStream fileOut = new FileOutputStream("gameSave.bin");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            game.saveGame(deathCounter, getTime());
            out.writeObject(game.getBoard());
            out.close();
            fileOut.close();
            System.out.printf("Game data is saved");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadGameSave() throws IOException {
        if (aTimer != null) {
            aTimer.stop();
            game.removeAll();
            System.out.println("Game loop stoped");
        }
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose game save");
        fc.setInitialDirectory(new File("./"));
        File file = fc.showOpenDialog(startPane.getParent().getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                System.out.println(file.toString());
                ObjectInputStream in = new ObjectInputStream(fileIn);
                this.board = (Board) in.readObject();
                System.out.println("Game Loaded");
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
            } catch (ClassNotFoundException c) {
                System.out.println("Game class not found");
                c.printStackTrace();
            }
            startPane.setVisible(false);
            gamePane.setVisible(true);
            menuPane.setVisible(false);
            gameOverPane.setVisible(false);
            running = !running;
            game = new Game(gamePane, board);
            System.out.println("Game save loaded");
            startGame();
        }
    }

    public void resumeGame() {
        menuPane.setVisible(false);
        gamePane.setVisible(true);
        aTimer.start();
    }

    public void setBG() {
        BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResource("/bg.png").toString(), 800, 500, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(myBI);
        mainPane.setBackground(bg);
    }

    public void HUDhandler() {
        if (game.getDeathCounter() > 15) {
            running = !running;
            game.removeAll();
            gameOverPane.setVisible(true);
            gamePane.setVisible(false);
            aTimer.stop();
        } else {
            deathCounterLabel.setText("" + game.getDeathCounter());
        }
        gameTimer();
    }

    public void gameTimer() {
        endTime = System.nanoTime();
        timerLabel.setText("" + (int) (gameSaveTime + (endTime - startTime) / 1000000000));
    }

    public int getTime() {
        return (int) (endTime - startTime);
    }

    public void setTime() {
        gameSaveTime = (int) game.getGameTime() / 1000000000;
    }

}
