package entities.flying.pickupItems;

import entities.Dimensions;
import entities.flying.FlyEntityInfo;
import entities.flying.StaticGlow;

public class Repair extends DefaultPickupitem {
   public Repair(Dimensions hitbox, FlyEntityInfo info) {
      super(hitbox, info, 7, 4,
            new StaticGlow(StaticGlow.WHITE_GLOW_DYNAMIC, 1.0f, 0.3f));
   }

   @Override
   protected void setGlowPos() {
      glow.setPos(x() - 18, y() - 18);
   }

}
