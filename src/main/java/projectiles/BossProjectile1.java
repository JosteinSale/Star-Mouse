package projectiles;

import entities.Dimensions;

import static projectiles.ProjectileFactory.TypeConstants.BOSS_PROJECTILE1;

/**
 * Note: does not have map collission.
 */
public class BossProjectile1 extends BaseProjectile {

   public BossProjectile1(Dimensions dimensions, float xSpeed, float ySpeed) {
      super(dimensions, BOSS_PROJECTILE1, 10, xSpeed, ySpeed);
      // collisionPixels intentionally left null - see class javadoc.
   }

   @Override
   public void updateCollisionPixels() {
      /* Do nothing */
   }
}
