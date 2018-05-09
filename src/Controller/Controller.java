package Controller;

import Model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Pane mainPane, startPane, menuPane, gamePane;
    @FXML
    Group group;
    public Board board;
    public Game game;
    public boolean running = false;
    AnimationTimer aTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Game started");
        group.setFocusTraversable(true);
        group.requestFocus();
        setBG();
        mainPane.setVisible(true);
        menuPane.setVisible(false);
        gamePane.setVisible(false);
        startPane.setVisible(true);
        keyInputHandler();
    }

    public void newGame() throws IOException {
        startPane.setVisible(false);
        gamePane.setVisible(true);
        if (!running) {
            this.running = !running;
            System.out.println("New game started");
            game = new Game(gamePane);
        }
        startGame();


    }

    public void startGame() {
        System.out.println("Starting game loop");
        aTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.tick();
                game.render();
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
            System.out.println(e.getCode());
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
            System.out.println("Game loop stoped");
        }
        game.removeSprite();
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
                running = !running;
                game = new Game(gamePane, board);
                System.out.println("Game save loaded");
                startGame();
        }
    }

    public void resumeGame() {
        if (!running) {
            running = !running;
            menuPane.setVisible(false);
            gamePane.setVisible(true);
            aTimer.start();
        }
    }

    public void setBG(){
        BackgroundImage myBI= new BackgroundImage(new Image(new File("bg.png").toURI().toString(),800,500,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        startPane.setBackground(new Background(myBI));
    }

}
