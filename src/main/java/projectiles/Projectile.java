package projectiles;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public interface Projectile {
    public Rectangle2D.Float getHitbox();

    public int getDamage();

    public int getXSpeed();

    public int getYSpeed();

    public void updateCollisionPixels();

    /** Returns the actual int values of the collisionPixels to be checked */
    public int[][] getCollisionPixels();

    public void drawHitbox(Graphics g);

    /** Each projectile will get their image from the projectileHandler */
    public void draw(Graphics g);

    public int getType();

    public void setActive(boolean active);

    public boolean isActive();

}
