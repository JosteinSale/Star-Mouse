package entities.bossmode.rudinger1;

import java.awt.Point;
import java.awt.geom.Rectangle2D.Float;
import java.awt.geom.Line2D;

import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.DefaultBossPart;
import entities.bossmode.PlayerBoss;

/**
 * Follows the player and shoots at regular intervals.
 * Currently each shoot-cycle takes 180 frames
 */
public class HeatSeekingLazer extends DefaultBossPart {
   private PlayerBoss player;
   private Line2D lazerLine; // For debugging
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
      this.lazerLine = new Line2D.Double();
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
      double dx = player.getHitbox().getCenterX() - gunCenter.getX();
      double dy = player.getHitbox().getCenterY() - gunCenter.getY();

      // Calculate the length of the line
      double lineLength = Math.sqrt(dx * dx + dy * dy);

      // Normalize the direction vector
      dx /= lineLength;
      dy /= lineLength;

      // Calculate the new point coordinates
      double newX = gunCenter.getX() + dx * imgDistanceFromCenter;
      double newY = gunCenter.getY() + dy * imgDistanceFromCenter;

      // Extract the rotation of the vector
      double newRotation = Math.atan2(dy, dx) - Math.PI / 2;

      this.setPosition((int) newX, (int) newY, newRotation);
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

   // Can be used for debugging
   // private void drawLazerLine(Graphics g) {
   // Graphics2D g2 = (Graphics2D) g;
   // g2.drawLine(
   // (int) (lazerLine.getX1() * Game.SCALE),
   // (int) (lazerLine.getY1() * Game.SCALE),
   // (int) (lazerLine.getX2() * Game.SCALE),
   // (int) (lazerLine.getY2() * Game.SCALE));
   // }

   // Include in this update method to update debugLazer
   private void updateLazerLine() {
      // We start by making a line representing the lazer angle.
      this.lazerLine = new Line2D.Double(
            // From:
            gunCenter.getX(), gunCenter.getY(),
            // To:
            player.getHitbox().getCenterX(), player.getHitbox().getCenterY());
   }
}
