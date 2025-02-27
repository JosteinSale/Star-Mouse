package projectiles.shootPatterns;

import java.awt.Point;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimatedComponentFactory;
import projectiles.ProjectileHandler2;
import static utils.Constants.Flying.TypeConstants.BOSS_PROJECTILE1;

/**
 * Shoots 6 projectiles at once in a fan pattern, downwards.
 * There are accompanying charge- and shoot animations at the gunPoint.
 */
public class FanPattern extends DefaultShootPattern {
   private AnimatedComponent shootAnimation;
   private AnimatedComponent chargeAnimation;

   // Math stuff
   private float projectileSpeed = 4f;
   private int nrOfProjectiles = 6;
   private double fanWidth = 140; // The fan spans 140 degrees
   private double anglePerProjectile = ((double) 1 / (nrOfProjectiles - 1)) * fanWidth;
   private double startAngle = 180 + (180 - fanWidth) / 2;

   public FanPattern(
         ProjectileHandler2 projectileHandler, Point gunPoint, AnimatedComponentFactory animationFactory,
         int chargeTime, int startDelay, int shootInterval) {
      super(projectileHandler, gunPoint, chargeTime, startDelay, shootInterval);
      this.shootAnimation = animationFactory.getPinkShootAnimation(
            (int) gunPoint.getX() - 150, (int) gunPoint.getY() - 150);
      this.chargeAnimation = animationFactory.getPinkEnergyBall(
            (int) gunPoint.getX() - 90, (int) gunPoint.getY() - 90);
   }

   @Override
   public void shoot() {
      for (int i = 0; i < nrOfProjectiles; i++) {
         double angle = Math.toRadians(startAngle + i * anglePerProjectile);
         float xSpeed = (float) (Math.cos(angle) * projectileSpeed);
         float ySpeed = (float) (Math.sin(angle) * -projectileSpeed);

         this.projectileHandler.addBossProjectile(
               BOSS_PROJECTILE1,
               (float) gunPoint.getX() - 35, // 35 is the width of the projectile/2
               (float) gunPoint.getY(),
               xSpeed, ySpeed);
      }
   }

   @Override
   public void update() {
      super.update();
      shootAnimation.updateAnimations();
      chargeAnimation.updateAnimations();
   }

   @Override
   public AnimatedComponent getChargeAnimation() {
      return this.chargeAnimation;
   }

   @Override
   public AnimatedComponent getShootAnimation() {
      return this.shootAnimation;
   }

}
