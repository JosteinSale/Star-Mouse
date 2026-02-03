package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.StaticGlow;

public class Powerup extends DefaultPickupitem {

   public Powerup(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info, 3, 7,
            new StaticGlow(StaticGlow.WHITE_GLOW_DYNAMIC, 1.0f, 0.3f));
   }

   @Override
   protected void setGlowPos() {
      glow.setPos(hitbox.x - 30, hitbox.y - 25);
   }
}
