package entities.bossmode;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * A BossPart represents an animated part of a boss that can be rotated, moved
 * around, perform actions, and collide with player.
 * 
 * The defaultBossPart is a default implementation of the IBossPart-interface.
 * See the interface for explanation of each provided method.
 * 
 * For drawing, the BossActionHandler will call the static method in this
 * object. If the bossPart needs additional animations (like charging),
 * you should implement it the subclass and call the method in the
 * boss-specific render (e.g RenderRudinger1).
 * 
 * OBS: The given hitbox will be used to represent the x- and y-coordinate, as
 * well as the dimensions of the hitbox, but it's NOT used for collision
 * detection.
 */
abstract public class DefaultBossPart implements IBossPart {
   public Rectangle2D.Float nonRotatedHitbox;
   protected Area rotatedArea; // Is used to check collision
   public Double rotation = 0.0;
   public Image[][] imgs;
   protected AffineTransform af; // Is used in rotation operations
   public int animAction = 0;
   public int aniIndex = 0;
   protected Boolean collisionEnabled = false;
   public boolean rotatedImgVisible = false;

   // Sprite info
   public String spriteName;
   public int aniRows;
   public int aniCols;
   public int spriteW;
   public int spriteH;

   /**
    * Constructs a new BossPart with the given hitbox and spriteSheet.
    * NOTE: the width/height of individual sprites will be scaled up x3.
    * So the hitbox should be about 3x the size of each individual sprite.
    * The sprites will always be drawn in the dead center of the hitbox.
    * 
    * @param hitbox
    * @param img
    * @param aniRows
    * @param aniCols
    * @param spriteW
    * @param spriteH
    */
   public DefaultBossPart(Rectangle2D.Float hitbox, String spriteName,
         int aniRows, int aniCols, int spriteW, int spriteH) {
      this.nonRotatedHitbox = hitbox;
      this.spriteName = spriteName;
      this.aniRows = aniRows;
      this.aniCols = aniCols;
      this.spriteW = spriteW;
      this.spriteH = spriteH;
      this.updateCollisionArea();
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
      // Make an Area-object using the non rotated hitbox
      Area area = new Area(nonRotatedHitbox);

      // Create an AffineTransform-object
      this.af = new AffineTransform();

      // Rotate this object according to current rotation, around the hitbox center.
      af.rotate(this.rotation, nonRotatedHitbox.getCenterX(), nonRotatedHitbox.getCenterY());

      // Create a new rotated area, by rotating the original area using the
      // AffineTransform.
      rotatedArea = area.createTransformedArea(af);
   }

   @Override
   public boolean containsPoint(Point p) {
      return rotatedArea.contains(p);
   }

   @Override
   public boolean intersectsRect(Rectangle2D.Float hb) {
      return rotatedArea.intersects(hb);
   }

   /**
    * Draw rotated hitbox. OBS: NOT SCALED TO Game.SCALE!
    * (We would need to create a new Area-object, which is expensive)
    */
   // public void drawRotatedHitbox(Graphics2D g2) {
   // g2.setColor(Color.BLACK);
   // g2.draw(rotatedArea);
   // }

   @Override
   public Rectangle2D.Float getNonRotatedHitbox() {
      return this.nonRotatedHitbox;
   }

   @Override
   public void setCollisionActive(boolean active) {
      this.collisionEnabled = active;
   }

   @Override
   public void setRotatedImgVisible(boolean visible) {
      this.rotatedImgVisible = visible;
   }

   @Override
   public boolean canCollide() {
      return this.collisionEnabled;
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
   public void updateAnimations() {
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
