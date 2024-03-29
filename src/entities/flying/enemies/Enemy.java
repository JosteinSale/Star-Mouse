package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public interface Enemy {

    /** If the enemy is small, the player can teleport through it and kill it.
     * Big enemies can not be teleported through, and if the player attempts it, 
     * he will collide just like he would with the map.
     * @return
     */
    public boolean isSmall();

   /**
     * Updates y-pos with levelYSpeed.
     * Also if the enemy is onScreen and !isDead: updates individual movement, aniTick and shootTick.
    */
    public void update(float levelYSpeed);

    public Rectangle2D.Float getHitbox();

    public int getType();

    /** Reduces the enemy's HP by the specified amount. Also sets the enemies
     * status to TAKING_DAMAGE. If HP falls below 0, it sets 'dead' to true.
     */
    public void takeDamage(int damage);

    public boolean canShoot();

    public void resetShootTick();

    public boolean isDead();

    /**Is used for drawing and to update individual behavior */
    public boolean isOnScreen();

    public void drawHitbox(Graphics g);

    /** If the enemy is onscreen and !dead, it's drawn */
    public void draw(Graphics g);

    /** Resets the enemy position, active-status, aniIndex, HP and shootTimer */
    public void resetTo(float y);

    /** Needed for projectileHandler. 1 = facing right, -1 = facing left. */
    public int getDir();
}
