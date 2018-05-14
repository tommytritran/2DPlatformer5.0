package Model;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Editor {
    Pane pane;
    Canvas canvas;
    GraphicsContext gc;

    private Board board;
    private int width;
    private int heigth;
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
                gc.strokeRect(j*width,i*heigth,width,heigth);
            }
        }
        System.out.println("draw");
    }

    public void setSize(int width, int height){
        arraySizeX = width;
        arraySizeY = height;
        this.width = (int) (canvas.getWidth() /width);
        this.heigth = (int) (canvas.getHeight() / height);
        System.out.println(heigth + " w" + width);
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
        System.out.println(Arrays.toString(mapArray.toArray()));
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
        int y = (int) (posY / heigth);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        try{
            switch (mapArray.get(y).get(x)){
                case 0:
                    mapArray.get(y).set(x, 1);
                    Image tile = new Image(new File("src/block.png").toURI().toString());
                    gc.setFill(new ImagePattern(tile));
                    gc.fillRect(x*width,y*heigth,width,heigth);
                    break;
                case 1:
                    mapArray.get(y).set(x,2);
                    mapArray.get(y).set(x, 1);
                    Image player = new Image(new File("src/block.png").toURI().toString());
                    gc.setFill(new ImagePattern(player));
                    gc.fillRect(x*width,y*heigth,width,heigth);
                    break;
                case 2:
                    mapArray.get(x).set(y,3);
                    break;
                case 3:
                    mapArray.get(x).set(y, 4);
                    break;
                case 4:
                    mapArray.get(x).set(y, 5);
                    break;
                case 5:
                    mapArray.get(x).set(y, 6);
                    break;
                case 6:
                    mapArray.get(x).set(y, 7);
                    break;
                case 7:
                    mapArray.get(x).set(y, 8);
                    break;
                case 8:
                    mapArray.get(x).set(y, 20);
                    break;
                case 20:
                    mapArray.get(x).set(y,0);
            }
            System.out.println(Arrays.toString(mapArray.toArray()));
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        } 
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
        System.out.println(Arrays.toString(mapArray.toArray()));
        width = (int) (canvas.getWidth()/mapArray.get(0).size());
        heigth = (int)(canvas.getHeight()/mapArray.size());
        arraySizeX = mapArray.get(0).size();
        arraySizeY = mapArray.size();
        System.out.println("W: " + width + " H: " +heigth);
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
                        gc.fillRect(j*width,i*heigth,width,heigth);
                        gc.setFill(new ImagePattern(tile));
                        gc.fillRect(j*width,i*heigth,width,heigth);
                        break;
                    case 2:
                        ImageView player = new ImageView(new File("src/timmy.png").toURI().toString());
                        player.setViewport(new Rectangle2D(0,0,64,64));
                        player.getImage();
                        player.relocate(j*width,i*heigth);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(j*width,i*heigth,width,heigth);
                        gc.setFill(new ImagePattern(player.getImage()));
                        gc.fillRect(j*width,i*heigth,j*width-1,i*heigth-1);
                        break;
                }
            }
        }
    }

    public Board playThisMap() throws FileNotFoundException {
        if(this.mapArray != null){
            saveMap();
            board = new Board();
            board.setMapArray(this.mapArray);
            return this.board;
        }
        return null;
    }
}