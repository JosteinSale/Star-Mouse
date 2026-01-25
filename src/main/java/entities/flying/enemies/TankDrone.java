package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;

public class TankDrone extends BaseEnemy {
   public TankDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info);
      maxHP = 300;
      HP = maxHP;
   }

   @Override
   public boolean canShoot() {
      return false;
   }

}