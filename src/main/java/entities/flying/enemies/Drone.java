package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;
import entities.flying.AnimatedGlow;

public class Drone extends BaseEnemy {
   public Drone(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval) {
      super(hitbox, info, shootInterval, new AnimatedGlow(AnimatedGlow.ORANGE_GLOW_BIG, 1f));
      setGlowPosition();
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
   }

   private void setGlowPosition() {
      glow.setPos(hitbox.x - 7, hitbox.y + hitbox.height / 2 - 5);
   }
}
