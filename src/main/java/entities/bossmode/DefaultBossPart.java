package entities.bossmode;

import java.awt.Point;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

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
abstract public class DefaultBossPart implements IBossPart {
   public Rectangle2D.Float nonRotatedHitbox;
   protected Polygon rotatedHitbox; // Is used to check collision
   public Double rotation = 0.0;
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
      this.nonRotatedHitbox = hitbox;
      this.animation = animation;
      constructCollisionPolygon(hitbox);
      updateCollisionArea();
   }

   private void constructCollisionPolygon(Float hitbox) {
      float[] verts = {
            0, 0,
            hitbox.width, 0,
            hitbox.width, hitbox.height,
            0, hitbox.height
      };
      this.rotatedHitbox = new Polygon(verts);
   }

   @Override
   public void updatePosition(int deltaX, int deltaY, Double deltaR) {
      this.rotation = (this.rotation + deltaR) % (Math.PI * 2);
      this.nonRotatedHitbox.x += deltaX;
      this.nonRotatedHitbox.y += deltaY;
      updateCollisionArea();
   }

   @Override
   public void setPosition(int centerX, int centerY, Double rotation) {
      this.rotation = rotation;
      this.nonRotatedHitbox.x = centerX - nonRotatedHitbox.width / 2;
      this.nonRotatedHitbox.y = centerY - nonRotatedHitbox.height / 2;
      updateCollisionArea();
   }

   private void updateCollisionArea() {
      rotatedHitbox.setOrigin(nonRotatedHitbox.width / 2f, nonRotatedHitbox.height / 2f);
      rotatedHitbox.setPosition(nonRotatedHitbox.x, nonRotatedHitbox.y);
      rotatedHitbox.setRotation((float) (rotation * MathUtils.radiansToDegrees));
   }

   @Override
   public boolean containsPoint(Point p) {
      return rotatedHitbox.contains(p.x, p.y);
   }

   @Override
   public boolean intersectsRect(Rectangle2D.Float hb) {
      Polygon otherHitbox = new Polygon(new float[] {
            0, 0,
            hb.width, 0,
            hb.width, hb.height,
            0, hb.height
      });
      otherHitbox.setOrigin(hb.width / 2f, hb.height / 2f);
      otherHitbox.setPosition(hb.x, hb.y);
      return Intersector.overlapConvexPolygons(rotatedHitbox, otherHitbox);
   }

   @Override
   public Rectangle2D.Float getNonRotatedHitbox() {
      return this.nonRotatedHitbox;
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
   public Polygon getRotatedHitbox() {
      return this.rotatedHitbox;
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
