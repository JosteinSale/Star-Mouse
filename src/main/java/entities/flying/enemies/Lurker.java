package entities.flying.enemies;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.PlayerFly;

public class Lurker extends BaseEnemy {
   private PlayerFly player;
   private boolean attackPhase = false;
   private Point2D.Float playerPos;
   private Point2D.Float lurkerPos;
   private double angle;

   public Lurker(Rectangle2D.Float hitbox, EntityInfo info, int chargeDone) {
      super(hitbox, info, chargeDone, null);
      maxHP = 10;
      HP = maxHP;
      aniTickPerFrame = 6;
      lurkerPos = new Point2D.Float(hitbox.x, hitbox.y);
   }

   /**
    * The lurker follows the player's position, so it needs access to the
    * player
    */
   public void setPlayer(PlayerFly player) {
      this.player = player;
      playerPos = new Point2D.Float(player.hitbox.x, player.hitbox.y);
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      updatePoint2Ds();
      jitterMovement();
      if (attackPhase) {
         turnSlowTowardsPlayer();
         moveTowardsPlayer(levelYSpeed);
      } else {
         turnFastTowardsPlayer();
         if (chargeTick >= chargeDone) {
            attackPhase = true;
         }

      }
   }

   private void jitterMovement() {
      float jitterAmount = 3f;
      if (aniTick == 0) {
         hitbox.x += (Math.random() - 0.5) * jitterAmount;
         hitbox.y += (Math.random() - 0.5) * jitterAmount;
      }
   }

   private void turnSlowTowardsPlayer() {
      double targetAngle = Math.atan2(playerPos.y - lurkerPos.y, playerPos.x - lurkerPos.x) + Math.PI / 2;
      double angleDifference = targetAngle - angle;

      // Normalize the angle difference to the range [-PI, PI]
      while (angleDifference > Math.PI) {
         angleDifference -= 2 * Math.PI;
      }
      while (angleDifference < -Math.PI) {
         angleDifference += 2 * Math.PI;
      }

      // Limit the rotation speed
      double rotationSpeed = 0.02; // Adjust this value for faster/slower turning
      if (angleDifference > rotationSpeed) {
         angle += rotationSpeed;
      } else if (angleDifference < -rotationSpeed) {
         angle -= rotationSpeed;
      } else {
         angle = targetAngle; // Close enough to the target angle
      }
   }

   private void moveTowardsPlayer(float levelYSpeed) {
      float speed = 5.0f; // Speed at which the lurker moves towards the player
      float dx = (float) (speed * Math.cos(angle - Math.PI / 2));
      float dy = (float) (speed * Math.sin(angle - Math.PI / 2));
      hitbox.x += dx;
      hitbox.y += dy;
   }

   private void updatePoint2Ds() {
      playerPos.setLocation(player.hitbox.x, player.hitbox.y);
      lurkerPos.setLocation(hitbox.x, hitbox.y);
   }

   private void turnFastTowardsPlayer() {
      angle = Math.atan2(playerPos.y - lurkerPos.y, playerPos.x - lurkerPos.x) + Math.PI / 2;
   }

   @Override
   public boolean canShoot() {
      return false;
   }

   @Override
   protected int amountOfFrames() {
      return 3;
   }

   @Override
   public double getRotation() {
      return angle;
   }

   @Override
   protected void resetCustomVars() {
      attackPhase = false;
   }
}
