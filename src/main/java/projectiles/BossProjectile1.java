package projectiles;

import java.awt.geom.Rectangle2D.Float;

import static projectiles.ProjectileFactory.TypeConstants.BOSS_PROJECTILE1;

/**
 * The bossProjectile differs from a regular projectile in that it
 * doesn't update collisionPixels, and instead returns null when
 * getCollisionPixels() is called. Also xSpeed and ySpeed is not
 * hard coded into the object, but rather taken as arguments.
 */
public class BossProjectile1 extends BaseProjectile {

   public BossProjectile1(Float hitbox, float xSpeed, float ySpeed) {
      super(hitbox, BOSS_PROJECTILE1, 10, xSpeed, ySpeed);
      // collisionPixels intentionally left null - see class javadoc.
   }

   @Override
   public void updateCollisionPixels() {
      /* Do nothing */
   }
}
