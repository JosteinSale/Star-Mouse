package projectiles;

import entities.Dimensions;
import entities.animation.Animation;
import entities.flying.enemies.Enemy;
import utils.Images;
import static entities.animation.Animation.Type.LOOP_FORWARDS;

import static projectiles.ProjectileFactory.TypeConstants.LAZER_PROJECTILE;;

public class LazerProjectile extends BaseProjectile {
   public Animation animation;
   private Enemy enemy;

   protected LazerProjectile(Dimensions dimensions, Enemy enemy) {
      super(dimensions, LAZER_PROJECTILE, 15, 0, 0);
      this.enemy = enemy;
      hasMapCollission = false;
      animation = new Animation(Images.LAZER_PROJECTILE_SPRITE, 0, 0, 3);
   }

   @Override
   public int getAniIndex() {
      return animation.getFrame();
   }

   @Override
   protected void updateCustomBehavior() {
      animation.play(LOOP_FORWARDS, 1);
      if (enemy.isDead()) {
         this.active = false;
      }
   }

}
