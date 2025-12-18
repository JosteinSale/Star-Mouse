package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;

public class OctaDrone extends BaseEnemy {
   public OctaDrone(Rectangle2D.Float hitbox, EntityInfo info, int shootTimer) {
      super(hitbox, info, shootTimer);
      maxHP = 85;
      HP = maxHP;
   }

   /**
    * This enemy can only shoot once in its lifetime
    */
   @Override
   protected void updateShootTick() {
      shootTick++;
   }

   @Override
   public void resetShootTick() {
      // Do nothing
   }
}
