package projectiles;

import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;

import static utils.Constants.Flying.TypeConstants.BOSS_PROJECTILE1;

/**
 * The bossProjectile differs from a regular projectile in that it
 * doesn't update collisionPixels, and instead returns null when
 * getCollisionPixels() is called. Also xSpeed and ySpeed is not
 * hard coded into the object, but rather taken as arguments.
 */
public class BossProjectile1 extends Entity implements Projectile {
   private BufferedImage img;
   private int damage = 10;
   private float xSpeed;
   private float ySpeed;
   private boolean isActive = true;

   public BossProjectile1(BufferedImage img, Float hitbox, float xSpeed, float ySpeed) {
      super(hitbox);
      this.img = img;
      this.xSpeed = xSpeed;
      this.ySpeed = ySpeed;
   }

   @Override
   public Float getHitbox() {
      return this.hitbox;
   }

   @Override
   public int getDamage() {
      return this.damage;
   }

   @Override
   public float getXSpeed() {
      return xSpeed;
   }

   @Override
   public float getYSpeed() {
      return ySpeed;
   }

   @Override
   public void updateCollisionPixels() {
      /* Do nothing */
   }

   @Override
   public int[][] getCollisionPixels() {
      return null;
   }

   @Override
   public int getType() {
      return BOSS_PROJECTILE1;
   }

   @Override
   public void setActive(boolean active) {
      this.isActive = active;
   }

   @Override
   public boolean isActive() {
      return isActive;
   }

}
