package entities.flying.enemies;

import entities.flying.AnimatedGlow;
import entities.flying.FlyEntityInfo;
import entities.Dimensions;

public class WaspDrone extends BaseEnemy {
   private int direction; // 1 = right, -1 = left

   public WaspDrone(Dimensions hitbox, FlyEntityInfo info, int direction, int shootTimer) {
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
            x() - 3 + direction * 35,
            y() + 50);
   };
}
