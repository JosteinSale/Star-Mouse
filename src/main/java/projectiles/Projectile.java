package projectiles;

import java.awt.geom.Point2D;

import entities.CollisionPixels;
import entities.MyRectangle;

public interface Projectile {

    public int getDamage();

    public float getXSpeed();

    public float getYSpeed();

    public void updateCollisionPixels();

    /** Returns the actual int values of the collisionPixels to be checked */
    public CollisionPixels getCollisionPixels();

    public int getType();

    public void setActive(boolean active);

    public boolean isActive();

    public double getRotation();

    /**
     * Sets rotation to an absolute angle, in radians.
     * The hitbox polygon is rotated around its center point.
     */
    public void setRotation(double radians);

   /**
    * Rotates the hitbox, in radians.
    * The hitbox polygon is rotated around its center point.
    */
    public void rotate(double deltaRadians);

    /**
     * Rotation-aware intersection test against an (unrotated) rectangle.
     */
    public boolean intersects(MyRectangle other);


    /** The rotated hitbox polygon used for collision */
    public MyRectangle getHitbox();

}
