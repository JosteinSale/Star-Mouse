package entities.flying.enemies;

import entities.flying.EntityInfo;
import entities.Dimensions;
import entities.flying.AnimatedGlow;

public class BlasterDrone extends BaseEnemy {

   public BlasterDrone(Dimensions hitbox, EntityInfo info) {
      super(hitbox, info, 60, new AnimatedGlow(AnimatedGlow.ORANGE_GLOW_BIG, 1f));
      startY = hitbox.y();
      this.info = info;
      maxHP = 80;
      HP = maxHP;
      setGlowPosition();
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
   }

   private void setGlowPosition() {
      glow.setPos(
            x() - 18,
            y() + height() / 2 - 5);
   }
}
