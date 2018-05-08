package Model;


import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;

public class Sprite extends Transition {
    ImageView sprite;
    Entity e;
    Pane pane;

    private double[] currPos;
    private int offsetX;
    private int offsetY;

    public Sprite(Pane gamePane, Entity e) {
        this.e = e;
        this.pane = gamePane;
        initSprite();
    }

    @Override
    protected void interpolate(double frac) {
        final int index = Math.min((int) Math.floor(e.getCount() * frac), e.getCount() - 1);
        final int x = (index % e.getColumns()) * e.getWidth() + offsetX;
        final int y = (index / e.getColumns()) * e.getHeight() + offsetX;
        sprite.setViewport(new Rectangle2D(x, y, e.getWidth(), e.getHeight()));
    }

    public void initSprite() {
        sprite = new ImageView(new File(e.getTexture()).toURI().toString());
        System.out.println(e.getTexture());
        sprite.setViewport(new Rectangle2D(e.getOffsetX(), e.getOffsetY(), e.getWidth(), e.getHeight()));
        offsetX = e.getOffsetX();
        offsetY = e.getOffsetY();
        currPos = e.getPosXY();
        render();
        pane.getChildren().addAll(sprite);
    }

    public void render() {
        sprite.setTranslateX(e.getPosX());
        sprite.setTranslateY(e.getPosY());
    }

    public void setEntityPosInfo(double[] pos){
        e.setPosXY(pos);
    }
    public double[] getEntityCurrPos(){
        currPos[0] = sprite.getTranslateX();
        currPos[1] = sprite.getTranslateY();
        return currPos;
    }

    public ID getID(){
        return e.getId();
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void clearSprite() {
        this.sprite.imageProperty().set(null);
    }
}
