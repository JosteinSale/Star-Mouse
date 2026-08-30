package entities.flying.enemies;

import entities.Dimensions;
import entities.flying.EntityInfo;

public class Target extends BaseEnemy {
   public Target(Dimensions hitbox, EntityInfo info) {
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
