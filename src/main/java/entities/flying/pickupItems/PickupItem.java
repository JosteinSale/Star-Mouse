package entities.flying.pickupItems;

import entities.MyRectangle;
import entities.flying.FlyEntityInfo;
import entities.flying.StaticGlow;

/**
 * An item that player can pick up in flying mode.
 * Includes powerups, repairs and bombs.
 */
public interface PickupItem {

   public void update(float yLevelSpeed);

   public boolean isActive();

   public int getAniIndex();

   public void setActive(boolean active);

   public MyRectangle getHitbox();

   public FlyEntityInfo getDrawInfo();

   public int getType();

   public void resetTo(float startY);

   public StaticGlow getGlow();
}
