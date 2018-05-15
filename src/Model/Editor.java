package Model;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Editor {
    Pane pane;
    Canvas canvas;
    GraphicsContext gc;

    private Board board;
    private int width;
    private int height;
    private int arraySizeX;
    private int arraySizeY;
    private ArrayList<ArrayList<Integer>> mapArray;
    public Editor(Pane pane){
        this.pane = pane;
        this.canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        canvas.setHeight(400);
        canvas.setWidth(700);
        canvas.relocate(0,0);
        pane.getChildren().addAll(canvas);
    }

    public void drawGrid(){
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        for (int i = 0; i < arraySizeY; i++) {
            for (int j = 0; j < arraySizeX; j++) {
                gc.strokeRect(j*width,i* height,width, height);
            }
        }
    }

    public void setSize(int width, int height){
        arraySizeX = width;
        arraySizeY = height;
        this.width = (int) (canvas.getWidth() /width);
        this.height = (int) (canvas.getHeight() / height);
        drawGrid();
        initArrayList();
    }
    public void initArrayList(){
        mapArray = new ArrayList<>();
        for (int i = 0; i < arraySizeY; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < arraySizeX; j++) {
                row.add(0);
            }
            mapArray.add(row);
        }
    }
    public void saveMap() throws FileNotFoundException {
        PrintWriter pr = new PrintWriter("src/editorMap.txt");

        if(mapArray != null){
            for (int i = 0; i <mapArray.size() ; i++) {
                for (int j = 0; j <mapArray.get(i).size() ; j++) {
                    pr.print(mapArray.get(i).get(j) + " ");
                }
                pr.println();
            }
        }
        pr.close();
    }

    public void drawTile(double posX, double posY)throws IndexOutOfBoundsException{
        int x = (int) (posX / width);
        int y = (int) (posY / height);
        try{
            switch (mapArray.get(y).get(x)){
                case 0:
                    mapArray.get(y).set(x, 1);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y* height,width-1, height-1);
                    Image tile = new Image(new File("src/block.png").toURI().toString());
                    gc.setFill(new ImagePattern(tile));
                    gc.fillRect(x*width,y* height,width, height);
                    break;
                case 1:
                    if(!isSetPlayer()){
                        mapArray.get(y).set(x,2);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(x*width,y* height,width-1, height-1);
                        Image player = new Image(new File("src/editorTimmy.png").toURI().toString());
                        gc.setFill(new ImagePattern(player));
                        gc.fillRect(x*width,y* height,width, height);
                    }else{
                        mapArray.get(y).set(x,3);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(x*width,y* height,width-1, height-1);
                        Image enemey1 = new Image(new File("src/editorEnemy1.png").toURI().toString());
                        gc.setFill(new ImagePattern(enemey1));
                        gc.fillRect(x*width,y* height,width, height);
                    }
                    break;
                case 2:
                    mapArray.get(y).set(x, 3);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y* height,width-1, height-1);
                    Image enemy1 = new Image(new File("src/editorEnemy1.png").toURI().toString());
                    gc.setFill(new ImagePattern(enemy1));
                    gc.fillRect(x*width,y* height,width, height);
                    break;
                case 3:
                    mapArray.get(y).set(x, 4);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y* height,width-1, height-1);
                    Image enemy2 = new Image(new File("src/editorEnemy2.png").toURI().toString());
                    gc.setFill(new ImagePattern(enemy2));
                    gc.fillRect(x*width,y* height,width, height);
                    break;
                case 4:
                    mapArray.get(y).set(x, 5);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y* height,width-1, height-1);
                    Image spike = new Image(new File("src/spike.png").toURI().toString());
                    gc.setFill(new ImagePattern(spike));
                    gc.fillRect(x*width,y* height,width, height);
                    break;
                case 5:
                    mapArray.get(y).set(x, 6);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y* height,width-1, height-1);
                    Image powerUP1 = new Image(new File("src/editorPowerUP1.png").toURI().toString());
                    gc.setFill(new ImagePattern(powerUP1));
                    gc.fillRect(x*width,y* height,width, height);
                    break;
                case 6:
                    mapArray.get(y).set(x, 7);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y* height,width-1, height-1);
                    Image powerUP2 = new Image(new File("src/editorPowerUP2.png").toURI().toString());
                    gc.setFill(new ImagePattern(powerUP2));
                    gc.fillRect(x*width,y* height,width, height);
                    break;
                case 7:
                    mapArray.get(y).set(x, 0);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y* height,width-1, height-1);
                    break;
            }
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public boolean isSetPlayer() {
        for (int i = 0; i < mapArray.size(); i++) {
            for (int j = 0; j <mapArray.get(i).size() ; j++) {
                if (mapArray.get(i).get(j) == 2){
                    return true;
                }
            }
        }
        return false;
    }

    public void loadMap(File file) throws IOException {
        String currLine;
        mapArray = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
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

        width = (int) (canvas.getWidth()/mapArray.get(0).size());
        height = (int)(canvas.getHeight()/mapArray.size());
        arraySizeX = mapArray.get(0).size();
        arraySizeY = mapArray.size();
        drawGrid();
        drawLoadedMap();
    }

    public void drawLoadedMap(){
        for (int i = 0; i < mapArray.size(); i++) {
            for (int j = 0; j < mapArray.get(i).size(); j++) {
                switch (mapArray.get(i).get(j)){
                    case 1:
                        Image tile = new Image(new File("src/block.png").toURI().toString());
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i* height,width-1, height-1);
                        gc.setFill(new ImagePattern(tile));
                        gc.fillRect(j*width,i* height,width, height);
                        break;
                    case 2:
                        System.out.println("player loaded");
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i* height,width-1, height-1);
                        Image player = new Image(new File("src/editorTimmy.png").toURI().toString());
                        gc.setFill(new ImagePattern(player));
                        gc.fillRect(j*width,i* height,width, height);
                        break;
                    case 3:
                        ImageView enemy1 = new ImageView(new File("src/editorEnemy1.png").toURI().toString());
                        enemy1.setViewport(new Rectangle2D(0,0,64,64));
                        enemy1.getImage();
                        enemy1.relocate(j*width,i* height);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i* height,width-1, height-1);
                        gc.setFill(new ImagePattern(enemy1.getImage()));
                        gc.fillRect(j*width,i* height,j*width-1,i* height -1);
                    case 4:
                        ImageView enemy2 = new ImageView(new File("src/editorEnemy2.png").toURI().toString());
                        enemy2.setViewport(new Rectangle2D(0,0,64,64));
                        enemy2.getImage();
                        enemy2.relocate(j*width,i* height);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i* height,width-1, height-1);
                        gc.setFill(new ImagePattern(enemy2.getImage()));
                        gc.fillRect(j*width,i* height,j*width-1,i* height -1);
                    case 5:
                        ImageView spike = new ImageView(new File("src/spike.png").toURI().toString());
                        spike.setViewport(new Rectangle2D(0,0,64,64));
                        spike.getImage();
                        spike.relocate(j*width,i* height);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i* height,width-1, height-1);
                        gc.setFill(new ImagePattern(spike.getImage()));
                        gc.fillRect(j*width,i* height,j*width-1,i* height -1);
                    case 6:
                        ImageView powerUP1 = new ImageView(new File("src/editorPowerUP1.png").toURI().toString());
                        powerUP1.setViewport(new Rectangle2D(0,0,64,64));
                        powerUP1.getImage();
                        powerUP1.relocate(j*width,i* height);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i* height,width-1, height-1);
                        gc.setFill(new ImagePattern(powerUP1.getImage()));
                        gc.fillRect(j*width,i* height,j*width-1,i* height -1);
                    case 7:
                        ImageView powerUP2 = new ImageView(new File("src/editorPowerUP2.png").toURI().toString());
                        powerUP2.setViewport(new Rectangle2D(0,0,64,64));
                        powerUP2.getImage();
                        powerUP2.relocate(j*width,i* height);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i* height,width-1, height-1);
                        gc.setFill(new ImagePattern(powerUP2.getImage()));
                        gc.fillRect(j*width,i* height,j*width-1,i* height -1);
                        default:
                            gc.setFill(Color.WHITE);
                            gc.fillRect(j*width,i* height,width-1, height-1);

                }
            }
        }
    }

    public Board playThisMap() throws FileNotFoundException {
        if(this.mapArray != null && isSetPlayer()){
            saveMap();
            board = new Board();
            board.setMapArray(this.mapArray);
            return this.board;
        }
        return null;
    }
}