package entities.bossmode;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main_classes.Game;

public class DefaultBossPart implements IBossPart {
   protected Rectangle2D.Float nonRotatedHitbox;
   protected Area rotatedArea; // Is used to check collision
   protected Double rotation = 0.0;
   protected BufferedImage[][] imgs;
   protected AffineTransform af; // Is used in rotation operations
   protected int action = 0;
   protected int aniIndex = 0;
   protected Boolean active = false;

   /**
    * Constructs a new BossPart with the given hitbox and spriteSheet.
    * NOTE: the width/height of individual sprites should be as
    * close as possible to the hitbox width/height. The sizes CAN differ,
    * but know that the image will always be drawn int the centered of the hitbox.
    * 
    * @param hitbox
    * @param img
    * @param aniRows
    * @param aniCols
    * @param spriteW
    * @param spriteH
    */
   public DefaultBossPart(Rectangle2D.Float hitbox, BufferedImage img,
         int aniRows, int aniCols, int spriteW, int spriteH) {
      this.nonRotatedHitbox = hitbox;
      updateCollisionArea();
      this.imgs = constructAnimationArray(img, aniRows, aniCols, spriteW, spriteH);
   }

   private BufferedImage[][] constructAnimationArray(BufferedImage img, int aniRows, int aniCols, int spriteW,
         int spriteH) {
      BufferedImage[][] animations = new BufferedImage[aniRows][aniCols];
      for (int r = 0; r < aniRows; r++) {
         for (int c = 0; c < aniCols; c++) {
            animations[r][c] = img.getSubimage(
                  c * spriteW,
                  r * spriteH, spriteW, spriteH);
         }
      }
      return animations;
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

   @Override
   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Draw hitbox. OBS: NOT SCALED TO Game.SCALE!
      // (We would need to create a new Area-object, which is expensive)
      //g2.setColor(Color.BLACK);
      //g2.draw(rotatedArea);

      // Draw nonRotatedHitbox. OBS: NOT SCALED TO Game.SCALE!
      // g2.draw(nonRotatedHitbox);

      // Draw the rotated image
      utils.Inf101Graphics.drawCenteredImage(
         g2, imgs[action][aniIndex],
         nonRotatedHitbox.getCenterX() * Game.SCALE,
         nonRotatedHitbox.getCenterY() * Game.SCALE, Game.SCALE, this.rotation);
   }

   @Override
   public Rectangle2D.Float getNonRotatedHitbox() {
      return this.nonRotatedHitbox;
   }

   @Override
   public void setActive(boolean active) {
      this.active = active;
   }

   @Override
   public boolean isActive() {
      return this.active;
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
   public void updateBehavior() {
      /* Override this method with custom behavior */
   }

   @Override
   public void updateAnimations() {
      /* Override this method with custom behavior */
   }
}
