package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;

/** The drone is basically a BaseEnemy */
public class Drone extends BaseEnemy {
   public Drone(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval) {
      super(hitbox, info, shootInterval);
   }
}
