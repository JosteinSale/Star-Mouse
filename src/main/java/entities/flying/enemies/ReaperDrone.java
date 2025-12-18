package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;

/**
 * The ReaperDrone shoots 3 wide, fast projectiles in fast succession.
 * They are not hard to dodge or kill, but has an imposing effect.
 */
public class ReaperDrone extends BaseEnemy {
   public ReaperDrone(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval) {
      super(hitbox, info, shootInterval);
      maxHP = 150;
      HP = maxHP;
   }

   @Override
   public boolean canShoot() {
      // Shoots 3 times in quick succession
      return (shootTick == shootInterval ||
            shootTick == (shootInterval + 20) ||
            shootTick == (shootInterval + 40));
   }

   @Override
   public boolean isSmall() {
      return false;
   }

   @Override
   public void resetShootTick() {
      // Do nothing
   }
}
