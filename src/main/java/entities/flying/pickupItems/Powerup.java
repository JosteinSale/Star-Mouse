package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;

public class Powerup extends DefaultPickupitem {

    public Powerup(Rectangle2D.Float hitbox, EntityInfo info) {
        super(hitbox, info, 3, 7);
    }
}
