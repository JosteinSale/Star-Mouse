package entities.flying.pickupItems;

import entities.Dimensions;
import entities.flying.FlyEntityInfo;
import entities.flying.StaticGlow;

public class Bomb extends DefaultPickupitem {
   public Bomb(Dimensions hitbox, FlyEntityInfo info) {
      super(hitbox, info, 3, 2,
            new StaticGlow(StaticGlow.WHITE_GLOW_DYNAMIC, 0.95f, 0.3f));
   }

   @Override
   protected void setGlowPos() {
      glow.setPos(x() - 23, y() - 26);
   }
}
