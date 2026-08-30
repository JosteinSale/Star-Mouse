package entities.flying.enemies;

import java.awt.geom.Point2D;

import entities.Dimensions;
import entities.flying.EntityInfo;
import entities.flying.PlayerFly;

public class Lurker extends BaseEnemy {
   private PlayerFly player;
   private boolean attackPhase = false;
   private Point2D.Float playerPos;
   private Point2D.Float lurkerPos;
   private double angle;

   public Lurker(Dimensions hitbox, EntityInfo info, int chargeDone, PlayerFly player) {
      super(hitbox, info, chargeDone, null);
      this.player = player;
      maxHP = 20;
      HP = maxHP;
      animation.setAniTickPerFrame(6);
      animation.setAmountOfFrames(3);
      lurkerPos = new Point2D.Float(hitbox.x(), hitbox.y());
      playerPos = new Point2D.Float(player.x(), player.y());
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
      float jitterAmount = (float) (Math.random() - 0.5) * 3f;
      if (animation.getTick() == 0) {
         move(jitterAmount, jitterAmount);
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
      double rotationSpeed = 0.025; // Adjust this value for faster/slower turning, 0.02
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
      move(dx, dy);
   }

   private void updatePoint2Ds() {
      playerPos.setLocation(player.x(), player.y());
      lurkerPos.setLocation(this.x(), this.y());
   }

   private void turnFastTowardsPlayer() {
      angle = Math.atan2(playerPos.y - lurkerPos.y, playerPos.x - lurkerPos.x) + Math.PI / 2;
   }

   @Override
   public boolean canShoot() {
      return false;
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
