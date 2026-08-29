package entities.boss_mode;

import entities.MyRectangle;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A BossPart represents an animated part of a boss that can be rotated, moved
 * around, perform actions, and collide with player.
 * 
 * The defaultBossPart is a default implementation of the IBossPart-interface.
 * See the interface for explanation of each provided method.
 * 
 * The bossPart takes a single AnimatedComponent as an argument.
 * If the bossPart needs additional animations (like charging),
 * you should implement it the subclass and override the
 * updateAnimations-method.
 * 
 * OBS: The given hitbox will be used to represent the x- and y-coordinate, as
 * well as the dimensions of the hitbox, but it's NOT used for collision
 * detection.
 */
abstract public class DefaultBossPart extends MyRectangle implements IBossPart {
   protected Boolean collisionEnabled = false;
   public boolean isVisible = false;
   public AnimatedComponent animation;

   /**
    * Constructs a new BossPart with the given hitbox and spriteSheet.
    * NOTE: the width/height of individual sprites will be scaled up x3.
    * So the hitbox should be about 3x the size of each individual sprite.
    * The sprites will always be drawn in the dead center of the hitbox.
    * 
    * @param hitbox
    * @param animation
    */
   public DefaultBossPart(Rectangle2D.Float hitbox, AnimatedComponent animation) {
      super(hitbox);
      this.animation = animation;
   }

   @Override
   public void updatePosition(int deltaX, int deltaY, Double deltaR) {
      this.move(deltaX, deltaY);
      this.rotate(deltaR);
   }

   @Override
   public void setPosition(float x, float y, Double rotation) {
      this.setPosition(x, y);
      this.setRotation(rotation);
   }

   @Override
   public boolean containsPoint(Point2D p) {
      return this.contains((float) p.getX(), (float) p.getY());
   }

   @Override
   public void setCollisionActive(boolean active) {
      this.collisionEnabled = active;
   }

   @Override
   public void setVisible(boolean visible) {
      this.isVisible = visible;
   }

   @Override
   public boolean canCollide() {
      return this.collisionEnabled;
   }

   @Override
   public void updateAnimations() {
      animation.updateAnimations();
   }

   @Override
   public MyRectangle getHitbox() {
      return this;
   }

   @Override
   public void onPlayerCollision() {
      /* Override this method with custom behavior */
   }

   @Override
   public void onTeleportHit() {
      /* Override this method with custom behavior */
   }

   @Override
   public void onProjectileHit() {
      /* Override this method with custom behavior */
   }

   @Override
   public void updateBehavior() {
      /* Override this method with custom behavior */
   }

   @Override
   public void startAttack() {
      /* Override this method with custom behavior */
   }

   @Override
   public void finishAttack() {
      /* Override this method with custom behavior */
   }

   @Override
   public boolean isCharging() {
      return false;
      /* Override this method with custom behavior, if needed */
   }

   @Override
   public boolean shouldAbort() {
      return false;
      /* Override this method with custom behavior, if needed */
   }

   @Override
   public boolean stopsProjectiles() {
      return false;
      /* Override this method with custom behavior, if needed */
   }

   @Override
   public boolean isCoolingDown() {
      return false;
      /* Override this method with custom behavior, if needed */
   }
}
