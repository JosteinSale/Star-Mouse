package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;
import entities.flying.AnimatedGlow;

/**
 * The ReaperDrone shoots 3 wide, fast projectiles in fast succession.
 * They are not hard to dodge or kill, but has an imposing effect.
 */
public class ReaperDrone extends BaseEnemy {
   public ReaperDrone(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval) {
      super(hitbox, info, shootInterval, new AnimatedGlow(AnimatedGlow.REAPER_GLOW, 1.5f));
      maxHP = 150;
      HP = maxHP;
      setGlowPosition();
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
   }

   private void setGlowPosition() {
      glow.setPos(hitbox.x + 75, hitbox.y + 100);
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
   public void onShoot() {
      if (glow != null) {
         glow.start();
      }
      // Don't reset shootTick, so it can shoot multiple times in succession
   }
}
