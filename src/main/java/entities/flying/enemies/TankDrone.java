package entities.flying.enemies;

import entities.Dimensions;
import entities.flying.FlyEntityInfo;

public class TankDrone extends BaseEnemy {
   public TankDrone(Dimensions hitbox, FlyEntityInfo info) {
      super(hitbox, info);
      maxHP = 300;
      HP = maxHP;
   }

   @Override
   public boolean canShoot() {
      return false;
   }

}