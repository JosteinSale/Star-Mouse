package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import cutscenes.effects.SimpleAnimation;
import entities.flying.EntityInfo;

public class FlameDrone extends BaseEnemy {
   public SimpleAnimation flameAnimation;
   private int charginStarts = 90;
   private int shootStarts = 120;

   public FlameDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info);
      startY = hitbox.y;
      this.info = info;
      maxHP = 120;
      HP = maxHP;
      this.flameAnimation = new SimpleAnimation(
            getFlameAnimationX(), getFlameAnimationY(),
            1f, 1f, 5, 6);
   }

   private int getFlameAnimationX() {
      return (int) (hitbox.x - 130);
   }

   private int getFlameAnimationY() {
      return (int) (hitbox.y + 86);
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      if (isPreparingToShoot()) {
         flameAnimation.xPos = getFlameAnimationX();
         flameAnimation.yPos = getFlameAnimationY();
         flameAnimation.updateAnimation();
      }
   }

   @Override
   public boolean canShoot() {
      if (shootTick == shootStarts) {
         action = IDLE;
         return true;
      }
      return false;
   }

   @Override
   public void onShoot() {
      // Don't reset shootTick - The flamedrone will only shoot once.
   }

   public boolean isPreparingToShoot() {
      return (shootTick >= charginStarts && shootTick < shootStarts);
   }
}
