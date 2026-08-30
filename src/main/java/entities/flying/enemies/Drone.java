package entities.flying.enemies;

import entities.flying.EntityInfo;
import entities.Dimensions;
import entities.flying.AnimatedGlow;

public class Drone extends BaseEnemy {

   public Drone(Dimensions hitbox, EntityInfo info, int shootInterval) {
      super(hitbox, info, shootInterval, new AnimatedGlow(AnimatedGlow.ORANGE_GLOW_BIG, 1f));
      setGlowPosition();
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
   }

   private void setGlowPosition() {
      glow.setPos(
            x() - 7,
            y() + height() / 2 - 5);
   }
}
