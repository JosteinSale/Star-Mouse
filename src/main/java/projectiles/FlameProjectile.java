package projectiles;

import static utils.Constants.Flying.SpriteSizes.FLAME_PRJT_SPRITE_H;
import static utils.Constants.Flying.SpriteSizes.FLAME_PRJT_SPRITE_W;
import static utils.Constants.Flying.TypeConstants.FLAME_PROJECTILE;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;

public class FlameProjectile extends Entity implements Projectile {
   BufferedImage img;
   private int damage = 30;
   private int xSpeed = 0;
   private int ySpeed = 4;
   private int[][] collisionPixels = new int[1][2];
   private boolean justShot = true;

   private boolean active = true;

   public FlameProjectile(Rectangle2D.Float hitbox, BufferedImage img) {
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
      collisionPixels[0][0] = (int) (hitbox.x + hitbox.width / 2) / 3; // x - ned i sentrum
      collisionPixels[0][1] = (int) (hitbox.y + hitbox.height) / 3; // y - ned i sentrum

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
      return FLAME_PROJECTILE;
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
         (int) ((hitbox.x - 36) * Game.SCALE),
         (int) ((hitbox.y - 35) * Game.SCALE),
         (int) (FLAME_PRJT_SPRITE_W * 3 * Game.SCALE),
         (int) (FLAME_PRJT_SPRITE_H * 3 * Game.SCALE), null);
   }
}
