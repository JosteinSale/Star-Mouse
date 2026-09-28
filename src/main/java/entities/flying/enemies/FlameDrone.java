package entities.flying.enemies;

import static entities.animation.Animation.Type.ONCE_FORWARDS;

import entities.Dimensions;
import entities.animation.PositionedAnimation;
import entities.flying.FlyEntityInfo;

public class FlameDrone extends BaseEnemy {
   public PositionedAnimation flameAnimation;
   private int charginStarts = 90;
   private int shootStarts = 120;

   public FlameDrone(Dimensions hitbox, FlyEntityInfo info) {
      super(hitbox, info);
      startY = hitbox.y;
      this.info = info;
      maxHP = 120;
      HP = maxHP;
      this.flameAnimation = new PositionedAnimation(
            0, 0, 5,
            getFlameAnimationX(), getFlameAnimationY(),
            3f, 3f);
   }

   private int getFlameAnimationX() {
      return (int) (x() - 130);
   }

   private int getFlameAnimationY() {
      return (int) (y() + 86);
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      if (isPreparingToShoot()) {
         flameAnimation.xPos = getFlameAnimationX();
         flameAnimation.yPos = getFlameAnimationY();
         flameAnimation.play(ONCE_FORWARDS, 6);
      }
   }

   @Override
   public boolean canShoot() {
      if (chargeTick == shootStarts) {
         animation.setAction(IDLE);
         return true;
      }
      return false;
   }

   @Override
   public void onShoot() {
      // Don't reset shootTick - The flamedrone will only shoot once.
   }

   public boolean isPreparingToShoot() {
      return (chargeTick >= charginStarts && chargeTick < shootStarts);
   }
}
