package entities.bossmode.rudinger1;

import java.awt.geom.Rectangle2D.Float;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.DefaultBossPart;

public class RotatingLazer extends DefaultBossPart {
   private Double rotationSpeed = 0.013;
   private Double initialRotation;
   private boolean isCharging = false;
   private int chargeTick = 0;
   private int chargeDuration = 140;
   private int visualWarningPoint = 100;

   private final AnimatedComponent lazerAnimation; // alias for the inherited animation field
   public AnimatedComponent chargeAnimation; // Can be null

   // Animation states
   public static final String SHOOTING = "SHOOTING";
   public static final String VISUAL_WARNING = "VISUAL_WARNING";

   public RotatingLazer(
         Float hitbox, AnimatedComponentFactory animationFactory,
         Double startRotation, AnimatedComponent chargeAnimation) {
      super(hitbox, animationFactory.getRotatingLazerAnimation((int) hitbox.x, (int) hitbox.y));
      this.lazerAnimation = this.animation;
      this.initialRotation = startRotation;
      this.updatePosition(0, 0, initialRotation);

      if (chargeAnimation != null) {
         this.chargeAnimation = chargeAnimation;
      }
   }

   @Override
   public void startAttack() {
      this.isCharging = true;
   }

   @Override
   public void updateBehavior() {
      if (isCharging) {
         updateChargingFase();
      } else { // The active fase
         this.updatePosition(0, 0, rotationSpeed);
      }
      this.updateAnimations();
   }

   private void updateChargingFase() {
      // The last 20 ticks of the charging, a visualWarning is given.
      chargeTick++;
      if (chargeTick >= visualWarningPoint && chargeTick < chargeDuration) {
         this.isVisible = true;
         lazerAnimation.setAnimation(VISUAL_WARNING);
      } else if (chargeTick >= chargeDuration) {
         lazerAnimation.setAnimation(SHOOTING);
         this.isCharging = false;
         this.collisionEnabled = true;
      }
   }

   @Override
   public void updateAnimations() {
      lazerAnimation.updateAnimations();
      if (chargeAnimation != null) {
         chargeAnimation.updateAnimations();
      }
   }

   @Override
   public void onPlayerCollision() {
      // No behavior
   }

   @Override
   public void onTeleportHit() {
      // No behavior
   }

   @Override
   public boolean isCharging() {
      return isCharging;
   }

   @Override
   public void finishAttack() {
      this.collisionEnabled = false;
      this.isVisible = false;
      this.chargeTick = 0;
      this.setPosition( // Resets to start position
            (int) nonRotatedHitbox.getCenterX(),
            (int) nonRotatedHitbox.getCenterY(),
            initialRotation);
   }
}
