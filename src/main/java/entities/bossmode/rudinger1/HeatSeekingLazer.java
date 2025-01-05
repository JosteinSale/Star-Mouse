package entities.bossmode.rudinger1;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;

import entities.bossmode.DefaultBossPart;
import entities.bossmode.IBossPart;
import entities.bossmode.PlayerBoss;
import main_classes.Game;

/**
 * Follows the player and shoots at regular intervals.
 * Currently each shoot-cycle takes 180 frames
 */
public class HeatSeekingLazer extends DefaultBossPart {
   private PlayerBoss player;
   private Line2D lazerLine; // For debugging
   private Point gunCenter;
   private double imgDistanceFromCenter; // The image is always drawn from the hitbox center, thus we need this.

   private final int CHARGING_ANIM = 0;
   private final int VISUALWARNING_ANIM = 2;
   private final int SHOOTING_ANIM = 1;

   private boolean isCharging = false;
   private int behaviorTick = 0;
   private int chargeDuration = 120;
   private int visualWarningPoint = 100;
   private int shootDuration = 60;

   private int aniTick = 0;
   private int aniSpeed = 3;

   public HeatSeekingLazer(
         Float hitbox, BufferedImage img, int aniRows, int aniCols, int spriteW, int spriteH,
         PlayerBoss player, Point gunCenter) {
      super(hitbox, img, aniRows, aniCols, spriteW, spriteH);
      this.player = player;
      this.imgDistanceFromCenter = hitbox.height / 2;
      this.gunCenter = gunCenter;
      this.lazerLine = new Line2D.Double();
   }

   @Override
   public void startAttack() {
      this.isCharging = true;
      this.rotatedImgVisible = true;
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
         this.animAction = CHARGING_ANIM;
      } else if (behaviorTick >= visualWarningPoint && behaviorTick < chargeDuration) {
         this.animAction = VISUALWARNING_ANIM;
      } else { // Shoot starts
         this.isCharging = false;
         this.animAction = SHOOTING_ANIM;
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
   public void updateAnimations() {
      aniTick++;
      if (aniTick > aniSpeed) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex > 3) {
            aniIndex = 0;
         }
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
      this.rotatedImgVisible = false;
      this.behaviorTick = 0;
   }

   public void draw(Graphics g) {
      IBossPart.draw(g, this);
   }

   // Can be used for debugging
   private void drawLazerLine(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.drawLine(
            (int) (lazerLine.getX1() * Game.SCALE),
            (int) (lazerLine.getY1() * Game.SCALE),
            (int) (lazerLine.getX2() * Game.SCALE),
            (int) (lazerLine.getY2() * Game.SCALE));
   }

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
