package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;
import entities.flying.AnimatedGlow;

public class WaspDrone extends BaseEnemy {
   private int direction; // 1 = right, -1 = left

   public WaspDrone(Rectangle2D.Float hitbox, EntityInfo info, int direction, int shootTimer) {
      super(hitbox, info, shootTimer, new AnimatedGlow(AnimatedGlow.ORANGE_GLOW_BIG, 1f));
      maxHP = 60;
      HP = maxHP;
      this.direction = direction;
   }

   @Override
   public int getDir() {
      return this.direction;
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
   }

   private void setGlowPosition() {
      glow.setPos(
            hitbox.x - 3 + direction * 35,
            hitbox.y + 50);
   };
}
