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

import java.io.*;
import java.net.URL;
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
                gc.strokeRect(i*width,j*heigth,width,heigth);
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

    public Board playMap(){
        this.board = new Board();
        board.setMapURL("");
        return this.board;
    }
    public void drawTile(double posX, double posY)throws IndexOutOfBoundsException{
        int x = (int) (posX / width);
        int y = (int) (posY / heigth);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        try{
            switch (mapArray.get(x).get(y)){
                case 0:
                    mapArray.get(x).set(y, 1);
                    Image tile = new Image(new File("src/block.png").toURI().toString());
                    gc.setFill(new ImagePattern(tile));
                    gc.fillRect(x*width,y*heigth,width,heigth);
                    break;
                case 1:
                    mapArray.get(x).set(y, 2);
                    ImageView player = new ImageView(new File("src/timmy.png").toURI().toString());
                    player.setViewport(new Rectangle2D(0,0,64,64));
                    player.relocate(x*width,y*heigth);
                    pane.getChildren().addAll(player);
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x*width,y*heigth,x*width-1,y*heigth-1);
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
                    case 0:
                        Image tile = new Image(new File("src/block.png").toURI().toString());
                        gc.setFill(new ImagePattern(tile));
                        gc.fillRect(i*width,j*heigth,width,heigth);
                        break;
                    case 1:
                        ImageView player = new ImageView(new File("src/timmy.png").toURI().toString());
                        player.setViewport(new Rectangle2D(0,0,64,64));
                        player.relocate(i*width,j*heigth);
                        pane.getChildren().addAll(player);
                        gc.setFill(Color.WHITE);
                        gc.fillRect(i*width,j*heigth,i*width-1,j*heigth-1);
                        break;
                }
            }
        }
    }
}