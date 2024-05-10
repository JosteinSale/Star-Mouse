package projectiles;

import static utils.Constants.Flying.SpriteSizes.REAPERDRONE_PRJT_SPRITE_H;
import static utils.Constants.Flying.SpriteSizes.REAPERDRONE_PRJT_SPRITE_W;
import static utils.Constants.Flying.TypeConstants.REAPER_PROJECTILE;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;

public class ReaperProjectile extends Entity implements Projectile {
   BufferedImage img;
   private int damage = 10;
   private int xSpeed = 0;
   private int ySpeed = 8;
   private int[][] collisionPixels = new int[1][2];

   private boolean active = true;

   public ReaperProjectile(Rectangle2D.Float hitbox, BufferedImage img) {
      super(hitbox);
      this.img = img;
   }

   @Override
   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   @Override
   public int getDamage() {
      return this.damage;
   }

   @Override
   public float getXSpeed() {
      return this.xSpeed;
   }

   @Override
   public float getYSpeed() {
      return this.ySpeed;
   }

   @Override
   public void updateCollisionPixels() {
      collisionPixels[0][0] = (int) (hitbox.x + hitbox.width / 2) / 3;   // x - the middle
      collisionPixels[0][1] = (int) (hitbox.y + hitbox.height) / 3;     // y - the bottom
   }

   @Override
   /**
    * Returns a 2D-array of collisionPixels. In the inner layer: 0 = x, and 1 = y.
    * The collisionPixels are already adjusted to 1/3 size.
    */
   public int[][] getCollisionPixels() {
      return this.collisionPixels;
   }

   @Override
   public void drawHitbox(Graphics g) {
      this.drawHitbox(g, 0, 0);
   }

   @Override
   public int getType() {
      return REAPER_PROJECTILE;
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
   public void draw(Graphics g) {
      g.drawImage(
         img,
         (int) (hitbox.x * Game.SCALE),
         (int) (hitbox.y * Game.SCALE),
         (int) (REAPERDRONE_PRJT_SPRITE_W * 3 * Game.SCALE),
         (int) (REAPERDRONE_PRJT_SPRITE_H * 3 * Game.SCALE), null);
   }
}
