package entities.flying.enemies;

import entities.Dimensions;
import entities.flying.EntityInfo;

public class SmallShip extends BaseEnemy {
   private int direction; // 1 = right, -1 = left
   private float xSpeed = 2;

   public SmallShip(Dimensions hitbox, EntityInfo info, int direction) {
      super(hitbox, info);
      startX = hitbox.x();
      maxHP = 20;
      HP = maxHP;
      this.direction = direction;
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      move(xSpeed * direction, 0);
   }

   @Override
   public boolean canShoot() {
      return false;
   }

   @Override
   public int getDir() {
      return this.direction;
   }
}
