package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.AnimatedGlow;

public class OctaDrone extends BaseEnemy {
   public OctaDrone(Rectangle2D.Float hitbox, EntityInfo info, int shootTimer) {
      super(hitbox, info, shootTimer, new AnimatedGlow(AnimatedGlow.ORANGE_GLOW_BIG, 1f));
      maxHP = 85;
      HP = maxHP;
      setGlowPosition();
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
   }

   private void setGlowPosition() {
      glow.setPos(hitbox.x - 8, hitbox.y - 8);
   }

   @Override
   public void onShoot() {
      if (glow != null) {
         glow.start();
      }
      // Don't reset shootTick, so it can only shoot once
   }
}
