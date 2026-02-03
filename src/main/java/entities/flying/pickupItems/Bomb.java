package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.StaticGlow;

public class Bomb extends DefaultPickupitem {
   public Bomb(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info, 3, 2,
            new StaticGlow(StaticGlow.WHITE_GLOW_DYNAMIC, 0.95f, 0.3f));
   }

   @Override
   protected void setGlowPos() {
      glow.setPos(hitbox.x - 23, hitbox.y - 26);
   }
}
