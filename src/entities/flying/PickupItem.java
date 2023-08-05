package entities.flying;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/** An item that player can pick up in flying mode.
 * Includes powerups, repairs and bombs.
 */
public interface PickupItem {

    public void update(float yLevelSpeed);

    public void draw(Graphics g);

    public boolean isActive();

    public void setActive(boolean active);

    public Rectangle2D.Float getHitbox();

    public int getType();
}
