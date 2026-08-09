package projectiles;

import java.awt.geom.Rectangle2D;

import entities.Entity;

/**
 * Base class for most projectile types in the game.
 * This class serves as a common ancestor for various projectile implementations.
 * It defaults to:
 * - a "center-bottom point, /3 scale" collisionPixels shape
 * - being active as soon as it's constructed
 * - returning damage as a flat, constant value
 * All of these can be overridden by subclasses if needed.
 * collisionPixels may be left null by a subclass (see BossProjectile1) if
 * updateCollisionPixels() is overridden to do nothing.
 */
public abstract class BaseProjectile extends Entity implements Projectile {
   protected int damage;
   protected float xSpeed;
   protected float ySpeed;
   protected int[][] collisionPixels; // Can be null
   protected boolean active = true;
   protected int type;

   protected BaseProjectile(Rectangle2D.Float hitbox, int type, int damage, float xSpeed, float ySpeed) {
      super(hitbox);
      this.type = type;
      this.damage = damage;
      this.xSpeed = xSpeed;
      this.ySpeed = ySpeed;
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

   /**
    * Default: center-bottom point, /3 scale.
    * Override with a different collisionPixels shape if needed.
    */
   @Override
   public void updateCollisionPixels() {
      collisionPixels[0][0] = (int) (hitbox.x + hitbox.width / 2) / 3;
      collisionPixels[0][1] = (int) (hitbox.y + hitbox.height) / 3;
   }

   @Override
   /**
    * Returns a 2D-array of collisionPixels. In the inner layer: 0 = x, and 1 = y.
    * The collisionPixels are already adjusted to 1/3 size. Can be null.
    */
   public int[][] getCollisionPixels() {
      return this.collisionPixels;
   }

   @Override
   public int getType() {
      return this.type;
   }

   @Override
   public void setActive(boolean active) {
      this.active = active;
   }

   @Override
   public boolean isActive() {
      return this.active;
   }
}
