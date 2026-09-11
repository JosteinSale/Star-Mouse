package entities.flying.enemies;

import java.awt.geom.Point2D;
import entities.AnimationFrame;
import entities.Dimensions;
import entities.flying.AnimatedGlow;
import entities.flying.EntityInfo;
import main_classes.Game;

public class BatDrone extends BaseEnemy {
   private final int dir; // Right = starts left, and moves towards right. And vice versa if dir = left.
   private final Point2D.Float attackPos;
   private final float xMoveSpeed = 8f;
   private final float yMoveSpeed = 2f;

   public BatDrone(Dimensions hitbox, EntityInfo info, int chargeDone, int dir) {
      super(hitbox, info, chargeDone, new AnimatedGlow(AnimatedGlow.ORANGE_GLOW_BIG, 1f));
      maxHP = 20;
      HP = maxHP;
      setGlowPosition();
      animation = new AnimationFrame(IDLE, 0, 4, 7);
      allAnimations.clear();
      allAnimations.add(animation);
      this.dir = dir;
      this.attackPos = new Point2D.Float(hitbox.x, hitbox.y);
      setStartPosition();
   }

   private void setGlowPosition() {
      glow.setPos(
            x() - 18,
            y() + 20);
   }

   /**
    * Sets the start point of the drone to an off-screen position.
    * When it attacks, it will fly onto the screen and arrive at the
    * attackPos, before shooting its projectile.
    */
   private void setStartPosition() {
      if (dir == Enemy.RIGHT) {
         setPosition(-80, y());
      } else {
         setPosition(Game.GAME_DEFAULT_WIDTH + 30, y());
      }
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
      moveToAttackPos();
   }

   private void moveToAttackPos() {
      if (isInAttackPos()) {
         return;
      }
      int flyOntoScreenPoint = chargeDone - 60;
      if (chargeTick > flyOntoScreenPoint) {
         moveTowardsAttackPoint();
      }
   }

   private boolean isInAttackPos() {
      return Math.abs(x() - attackPos.x) < 10f;
   }

   private void moveTowardsAttackPoint() {
      if (dir == Enemy.RIGHT) {
         // move towards right
         move(xMoveSpeed, yMoveSpeed);
      } else {
         // move towards left
         move(-xMoveSpeed, yMoveSpeed);
      }
   }

   @Override
   public void onShoot() {
      glow.start();
      // Don't reset shootTick, so it can only shoot once
   }

   @Override
   public int getDir() {
      return this.dir;
   }

   @Override
   protected void resetCustomVars() {
      this.setStartPosition();
   }
}
