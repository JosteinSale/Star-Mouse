package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.AnimatedGlow;

/**
 * An item that player can pick up in flying mode.
 * Includes powerups, repairs and bombs.
 */
public interface PickupItem {

   public void update(float yLevelSpeed);

   public boolean isActive();

   public int getAniIndex();

   public void setActive(boolean active);

   public Rectangle2D.Float getHitbox();

   public EntityInfo getDrawInfo();

   public int getType();

   public void resetTo(float startY);

   public AnimatedGlow getGlow();
}
