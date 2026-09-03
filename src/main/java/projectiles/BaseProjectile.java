package projectiles;

import entities.CollisionPixels;
import entities.MyRectangle;
import entities.Dimensions;

/**
 * Base class for most projectile types in the game.
 * This class serves as a common ancestor for various projectile
 * implementations.
 * It defaults to:
 * - being active as soon as it's constructed
 * - returning damage as a flat, constant value
 * All of these can be overridden by subclasses if needed.
 * Collision with the map is optional, and is applied by leaving the
 * 'collisionPixels' field empty.
 */
public abstract class BaseProjectile extends MyRectangle implements Projectile {
   protected CollisionPixels collisionPixels;
   protected int damage;
   protected float xSpeed;
   protected float ySpeed;
   protected boolean active = true;
   protected int type;

   protected BaseProjectile(Dimensions dimensions, int type, int damage, float xSpeed, float ySpeed) {
      super(dimensions);
      this.type = type;
      this.damage = damage;
      this.xSpeed = xSpeed;
      this.ySpeed = ySpeed;
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
   public MyRectangle getHitbox() {
      return this;
   }

   /**
    * Updates the collisionPixels array to the current center of the projectile's
    * hitbox.
    */
   @Override
   public void updateCollisionPixels() {
      collisionPixels.update();
   }

   @Override
   /**
    * Returns a 2D-array of collisionPixels. In the inner layer: 0 = x, and 1 = y.
    * The collisionPixels are already adjusted to 1/3 size.
    */
   public CollisionPixels getCollisionPixels() {
      return collisionPixels;
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

   @Override
   public double getRotation() {
      return this.rotationRadians;
   }

   @Override
   public void setRotation(double newRotationRadians) {
      super.setRotation(newRotationRadians);
   }

   @Override
   public void rotate(double deltaRadians) {
      super.rotate(deltaRadians);
   }

   @Override
   public boolean intersects(MyRectangle other) {
      return super.intersects(other);
   }
}
