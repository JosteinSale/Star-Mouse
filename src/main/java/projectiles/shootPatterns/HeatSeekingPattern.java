package projectiles.shootPatterns;

import java.awt.Graphics;
import java.awt.Point;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.PlayerBoss;
import projectiles.ProjectileHandler2;
import static utils.Constants.Flying.TypeConstants.BOSS_PROJECTILE1;

/**
 * Shoots a single projectiles directed at the player.
 * There are accompanying charge- and shoot animations at the gunPoint.
 */
public class HeatSeekingPattern extends DefaultShootPattern {
   private AnimatedComponent shootAnimation;
   private AnimatedComponent chargeAnimation;
   private float projectileSpeed = 4f;
   private PlayerBoss player;

   public HeatSeekingPattern(
         ProjectileHandler2 projectileHandler, Point gunPoint, AnimatedComponentFactory animationFactory,
         PlayerBoss player,
         int chargeTime, int startDelay, int shootInterval) {
      super(projectileHandler, gunPoint, chargeTime, startDelay, shootInterval);
      this.player = player;
      this.shootAnimation = animationFactory.getPinkShootAnimation(
            (int) gunPoint.getX() - 140, (int) gunPoint.getY() - 140);
      this.chargeAnimation = animationFactory.getPinkEnergyBall(
            (int) gunPoint.getX() - 90, (int) gunPoint.getY() - 90);
   }

   @Override
   public void shoot() {
      Point.Float shootVector = this.getShootVector();
      float xSpeed = (float) shootVector.getX();
      float ySpeed = (float) shootVector.getY();
      this.projectileHandler.addBossProjectile(
            BOSS_PROJECTILE1,
            (float) gunPoint.getX(),
            (float) gunPoint.getY(),
            xSpeed, ySpeed);
   }

   private Point.Float getShootVector() {
      // Calculate the direction vector of the line
      double dx = player.getHitbox().getCenterX() - gunPoint.getX();
      double dy = player.getHitbox().getCenterY() - gunPoint.getY();

      // Calculate the length of the line
      double lineLength = Math.sqrt(dx * dx + dy * dy);

      // Normalize the direction vector.
      dx /= lineLength;
      dy /= lineLength;

      // Make a Point representing the vector.
      // Multiply the normalized dx/dy with projectileSpeed.
      Point.Float shootVector = new Point.Float(
            (float) dx * projectileSpeed,
            (float) dy * projectileSpeed);

      return shootVector;
   }

   @Override
   public void update() {
      super.update();
      shootAnimation.updateAnimations();
      chargeAnimation.updateAnimations();
   }

   @Override
   public void drawShootAnimations(Graphics g) {
      if (isCharging) {
         AnimatedComponent.draw(g, chargeAnimation);
      } else if (shootPhase) {
         AnimatedComponent.draw(g, shootAnimation);
      }
   }

}
