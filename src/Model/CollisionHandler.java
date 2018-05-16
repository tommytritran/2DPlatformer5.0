package Model;

import java.util.ArrayList;

/**
 * Comments
 * 
 * @author Carlo Nguyen
 * @author Tommy Tran
 * @author Marius Haugen
 */

public class CollisionHandler {
    ArrayList<Sprite> spriteList;

    public CollisionHandler(ArrayList<Sprite> spriteList) {
        this.spriteList = spriteList;
    }

    public boolean collCheck(Sprite player, Sprite entity) {
        if (entity.getID() != ID.Player) {
            if (player.getSprite().getBoundsInParent().intersects(entity.getSprite().getBoundsInParent())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

