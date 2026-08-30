package entities.flying.enemies;

import entities.Dimensions;
import entities.flying.EntityInfo;

public class TankDrone extends BaseEnemy {
   public TankDrone(Dimensions hitbox, EntityInfo info) {
      super(hitbox, info);
      maxHP = 300;
      HP = maxHP;
   }

   @Override
   public boolean canShoot() {
      return false;
   }

}