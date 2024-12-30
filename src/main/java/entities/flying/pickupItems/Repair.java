package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;

public class Repair extends DefaultPickupitem {
    public Repair(Rectangle2D.Float hitbox, EntityInfo info) {
        super(hitbox, info, 7, 4);
    }

}
