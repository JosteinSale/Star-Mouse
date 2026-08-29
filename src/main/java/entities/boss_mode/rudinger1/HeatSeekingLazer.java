package entities.boss_mode.rudinger1;

import java.awt.Point;
import java.awt.geom.Rectangle2D.Float;

import entities.boss_mode.AnimatedComponentFactory;
import entities.boss_mode.DefaultBossPart;
import entities.boss_mode.PlayerBoss;

/**
 * Follows the player and shoots at regular intervals.
 * Currently each shoot-cycle takes 180 frames
 */
public class HeatSeekingLazer extends DefaultBossPart {
   private PlayerBoss player;
   private Point gunCenter;
   private double imgDistanceFromCenter; // The image is always drawn from the hitbox center, thus we need this.

   // Animation states
   public static final String CHARGING = "CHARGING";
   public static final String VISUAL_WARNING = "VISUAL_WARNING";
   public static final String SHOOTING = "SHOOTING";

   private boolean isCharging = false;
   private int behaviorTick = 0;
   private int chargeDuration = 120;
   private int visualWarningPoint = 100;
   private int shootDuration = 60;

   // String spriteSheet, int aniRows, int aniCols, int spriteW, int spriteH

   public HeatSeekingLazer(
         Float hitbox, AnimatedComponentFactory animationFactory,
         PlayerBoss player, Point gunCenter) {
      super(hitbox, animationFactory.getHeatSeekingLazerAnimation((int) hitbox.x, (int) hitbox.y));
      this.player = player;
      this.imgDistanceFromCenter = hitbox.height / 2;
      this.gunCenter = gunCenter;
   }

   @Override
   public void startAttack() {
      this.isCharging = true;
      this.isVisible = true;
   }

   @Override
   public void updateBehavior() {
      if (isCharging) {
         updateChargingFase();
      } else {
         updateShootingFase();
      }
      this.updateAnimations();
   }

   private void updateChargingFase() {
      behaviorTick++;
      if (behaviorTick < visualWarningPoint) {
         pointLazerAtPlayer();
         animation.setAnimation(CHARGING);
      } else if (behaviorTick >= visualWarningPoint && behaviorTick < chargeDuration) {
         animation.setAnimation(VISUAL_WARNING);
      } else { // Shoot starts
         this.isCharging = false;
         animation.setAnimation(SHOOTING);
         this.collisionEnabled = true;
         this.behaviorTick = 0;
      }
   }

   private void updateShootingFase() {
      // When this method starts, tick is 0
      behaviorTick++;
      if (behaviorTick >= shootDuration) {
         // Restarts from charging fase
         this.behaviorTick = 0;
         this.isCharging = true;
         this.collisionEnabled = false;
      }
   }

   private void pointLazerAtPlayer() {
      // Calculate the direction vector of the line
      double dx = player.getHitbox().centerX() - gunCenter.getX();
      double dy = player.getHitbox().centerY() - gunCenter.getY();

      // Calculate the length of the line
      double lineLength = Math.sqrt(dx * dx + dy * dy);

      // Normalize the direction vector
      dx /= lineLength;
      dy /= lineLength;

      // Calculate the new center for the lazer hitbox
      double lazerCenterX = gunCenter.getX() + dx * imgDistanceFromCenter;
      double lazerCenterY = gunCenter.getY() + dy * imgDistanceFromCenter;

      // Offset away from the center to find the actual hitbox x and y
      float newX = (float) lazerCenterX - hitboxWidth / 2;
      float newY = (float) lazerCenterY - hitboxHeight / 2;

      // Extract the rotation of the vector
      double newRotation = Math.atan2(dy, dx) - Math.PI / 2;

      this.setPosition(newX, newY, newRotation);
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
      this.behaviorTick = 0;
   }
}
