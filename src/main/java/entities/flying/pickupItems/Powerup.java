package entities.flying.pickupItems;

import entities.Dimensions;
import entities.flying.EntityInfo;
import entities.flying.StaticGlow;

public class Powerup extends DefaultPickupitem {

   public Powerup(Dimensions hitbox, EntityInfo info) {
      super(hitbox, info, 3, 7,
            new StaticGlow(StaticGlow.WHITE_GLOW_DYNAMIC, 1.0f, 0.3f));
   }

   @Override
   protected void setGlowPos() {
      glow.setPos(x() - 30, y() - 25);
   }
}
