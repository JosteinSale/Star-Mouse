package entities.flying.enemies;

import entities.Dimensions;
import entities.flying.FlyEntityInfo;

public class Target extends BaseEnemy {
   public Target(Dimensions hitbox, FlyEntityInfo info) {
      super(hitbox, info);
      maxHP = 20;
      HP = maxHP;
   }

   @Override
   protected void updateChargeTick() {
      // Do nothing
   }

   public boolean canShoot() {
      return false;
   }
}
