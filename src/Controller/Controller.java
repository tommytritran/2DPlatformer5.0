package Controller;

import Model.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Pane mainPane, startPane, menuPane, gamePane, HUD, gameOverPane, editorPane;
    @FXML
    Group group;
    @FXML
    Label lifePointsLabel, timerLabel, warningLabel, startPaneErrorLabel, editorErrorLabel;
    @FXML
    TextField mapWidth, mapHeight;
    private SoundHandler soundHandler;
    private Editor editor;
    private double startTime = 0;
    private double endTime = 0;
    private double gameSaveTime = 0;
    private String bg1 = "/res/bg.png";
    public Board board;
    public Game game;
    public boolean running = false;
    AnimationTimer aTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundHandler = new SoundHandler();
        soundHandler.bgStart();
        group.setFocusTraversable(true);
        group.requestFocus();
        setBG(bg1);
        mainPane.setVisible(true);
        editorPane.setVisible(false);
        menuPane.setVisible(false);
        gamePane.setVisible(false);
        gameOverPane.setVisible(false);
        HUD.setVisible(false);
        startPane.setVisible(true);
        keyInputHandler();
    }

    public void newGame() throws IOException {
        if (aTimer != null) {
            game.removeAll();
        }
        menuPane.setVisible(false);
        gameOverPane.setVisible(false);
        startPane.setVisible(false);
        editorPane.setVisible(false);
        gamePane.setVisible(true);
        game = new Game(gamePane, mainPane);
        startGame();
    }

    public void startGame() {
        startTime = System.nanoTime();
        setTime();
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
                try {
                    HUDhandler();
                } catch (NullPointerException e) {
                    startPaneErrorLabel.setText("Error cant load game");
                    menuPane.setVisible(false);
                    gameOverPane.setVisible(false);
                    gamePane.setVisible(false);
                    editorPane.setVisible(false);
                    HUD.setVisible(false);
                    startPane.setVisible(true);
                }
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
                System.out.println("ESC");
                if (running) {
                    gamePane.setVisible(false);
                    gameOverPane.setVisible(false);
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
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yy-ss");
            String fileName = ft.format(dNow);
            fileName += ".txt";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            game.saveGame(game.getLifePoints(), getTime());
            out.writeObject(game.getBoard());
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void loadGameSave() throws IOException {
        if (aTimer != null) {
            aTimer.stop();
            game.removeAll();
        }
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter fcFilter = new FileChooser.ExtensionFilter("TXT Files (.txt)", "*.txt");
        fc.getExtensionFilters().add(fcFilter);
        fc.setTitle("Choose game save");
        fc.setInitialDirectory(new File("./"));
        File file = fc.showOpenDialog(startPane.getParent().getScene().getWindow());
        if (file != null) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                this.board = (Board) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException i) {
                startPaneErrorLabel.setText("Error cant load game");
                menuPane.setVisible(false);
                gameOverPane.setVisible(false);
                gamePane.setVisible(false);
                HUD.setVisible(false);
                startPane.setVisible(true);
                if (aTimer != null) {
                    aTimer.stop();
                }
            } catch (ClassNotFoundException c) {
                startPaneErrorLabel.setText("Error cant load game");
                HUD.setVisible(false);
                menuPane.setVisible(false);
                gameOverPane.setVisible(false);
                gamePane.setVisible(false);
                startPane.setVisible(true);
            }
            try {
                game = new Game(gamePane, mainPane, board);
                menuPane.setVisible(false);
                gameOverPane.setVisible(false);
                startPane.setVisible(false);
                gamePane.setVisible(true);
                startGame();
            } catch (NullPointerException e) {
                startPaneErrorLabel.setText("Error cant load game");
                menuPane.setVisible(false);
                gameOverPane.setVisible(false);
                gamePane.setVisible(false);
                editorPane.setVisible(false);
                HUD.setVisible(false);
                startPane.setVisible(true);
            }
        }
    }

    public void resumeGame() {
        menuPane.setVisible(false);
        gamePane.setVisible(true);
        aTimer.start();
    }

    public void setBG(String path) {
        BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResource(path).toString(), 800, 500, false, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(myBI);
        mainPane.setBackground(bg);
    }

    public void HUDhandler() {
        if (game.getLifePoints() < 1) {
            running = !running;
            game.removeAll();
            gameOverPane.setVisible(true);
            gamePane.setVisible(false);
            aTimer.stop();
        } else {
            lifePointsLabel.setText("" + game.getLifePoints());
        }
        gameTimer();
    }

    public void gameTimer() {
        endTime = System.nanoTime();
        timerLabel.setText("" + (int) (gameSaveTime + (endTime - startTime) / 1000000000));
    }

    public int getTime() {
        int time = Integer.parseInt(timerLabel.getText());
        return time;
    }

    public void setTime() {
        gameSaveTime = (int) game.getGameTime();
    }

    public void startEditor() {
        startPane.setVisible(false);
        editorPane.setVisible(true);
        editor = new Editor(editorPane);
        mapWidth.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        mapHeight.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
    }


    public void mapSize() {
        int w = Integer.parseInt(mapWidth.getText());
        int h = Integer.parseInt(mapHeight.getText());
        editor.setSize(w, h);
    }

    public void drawTile(MouseEvent e) {
        try {
            editor.drawTile(e.getX(), e.getY());
        } catch (IndexOutOfBoundsException exception) {
            editorErrorLabel.setText("Please enter a valid map size");
        } catch (NullPointerException ex) {
            editorErrorLabel.setText("Please enter a valid map size");
        }
    }

    public void saveEditorMap() throws FileNotFoundException {
        editor.saveMap();
    }

    public void playEditorMap() throws IOException {
        this.board = editor.playThisMap();
        if (board == null) {
            warningLabel.setText("Please add a player!");
        } else {
            game = new Game(gamePane, gamePane, board);
            editorPane.setVisible(false);
            gamePane.setVisible(true);
            startGame();
        }
    }

    public void loadEditorMap() throws IOException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose game save");
        fc.setInitialDirectory(new File("src/res/"));
        File file = fc.showOpenDialog(startPane.getParent().getScene().getWindow());
        if (file != null) {
            editor.loadMap(file);
        }
    }

}
