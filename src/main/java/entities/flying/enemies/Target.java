package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;

public class Target extends BaseEnemy {
   public Target(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info);
      maxHP = 20;
      HP = maxHP;
   }

   @Override
   protected void updateShootTick() {
      // Do nothing
   }

   public boolean canShoot() {
      return false;
   }
}
