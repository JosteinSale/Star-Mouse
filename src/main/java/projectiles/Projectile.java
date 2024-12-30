package projectiles;

import java.awt.geom.Rectangle2D;

public interface Projectile {
    public Rectangle2D.Float getHitbox();

    public int getDamage();

    public float getXSpeed();

    public float getYSpeed();

    public void updateCollisionPixels();

    /** Returns the actual int values of the collisionPixels to be checked */
    public int[][] getCollisionPixels();

    public int getType();

    public void setActive(boolean active);

    public boolean isActive();

}
