package Model;

import java.util.ArrayList;

public class CollisionHandler {
    ArrayList<Sprite> spriteList;

    public CollisionHandler(ArrayList<Sprite> sl) {
        this.spriteList = sl;
    }

    public boolean collCheck(Sprite player, Sprite entity) {
        if (entity.getID() != ID.Player) {
            if (player.getSprite().getBoundsInParent().intersects(entity.getSprite().getBoundsInParent())) {
                System.out.println("hit");
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

