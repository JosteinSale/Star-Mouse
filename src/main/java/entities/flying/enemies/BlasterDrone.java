package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;

public class BlasterDrone extends BaseEnemy {

   public BlasterDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info, 60);
      startY = hitbox.y;
      this.info = info;
      maxHP = 80;
      HP = maxHP;
   }
}
