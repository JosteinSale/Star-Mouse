package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.Glow;

public class BlasterDrone extends BaseEnemy {

   public BlasterDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info, 60, new Glow(Glow.ORANGE_GLOW_BIG, 1f));
      startY = hitbox.y;
      this.info = info;
      maxHP = 80;
      HP = maxHP;
      setGlowPosition();
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      glow.update();
      setGlowPosition();
   }

   private void setGlowPosition() {
      glow.setPos(hitbox.x - 18, hitbox.y + hitbox.height / 2 - 5);
   }
}
