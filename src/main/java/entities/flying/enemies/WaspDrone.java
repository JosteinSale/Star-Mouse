package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;

public class WaspDrone extends BaseEnemy {
   private int direction; // 1 = right, -1 = left

   public WaspDrone(Rectangle2D.Float hitbox, EntityInfo info, int direction, int shootTimer) {
      super(hitbox, info, shootTimer);
      maxHP = 60;
      HP = maxHP;
      this.direction = direction;
   }

   @Override
   public int getDir() {
      return this.direction;
   }

}
