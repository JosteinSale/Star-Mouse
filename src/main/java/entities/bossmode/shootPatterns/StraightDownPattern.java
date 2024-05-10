package entities.bossmode.shootPatterns;

import java.awt.Point;

import projectiles.ProjectileHandler2;
import static utils.Constants.Flying.TypeConstants.BOSS_PROJECTILE1;

/**
 * Shoots a single projectile straight downwards at the specified interval.
 */
public class StraightDownPattern extends DefaultShootPattern {
   private float ySpeed = 4f;

   public StraightDownPattern(ProjectileHandler2 projectileHandler, Point gunPoint, int chargeTime, int startDelay,
         int shootInterval) {
      super(projectileHandler, gunPoint, chargeTime, startDelay, shootInterval);
   }

   @Override
   public void shoot() {
      this.projectileHandler.addBossProjectile(
         BOSS_PROJECTILE1, 
         (float) gunPoint.getX(), 
         (float) gunPoint.getY(), 
         0, ySpeed);
   }
   
}
