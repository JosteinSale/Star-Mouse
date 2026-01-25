package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;

public class SmallShip extends BaseEnemy {
   private int direction; // 1 = right, -1 = left
   private float xSpeed = 2;

   public SmallShip(Rectangle2D.Float hitbox, EntityInfo info, int direction) {
      super(hitbox, info);
      startX = hitbox.x;
      maxHP = 20;
      HP = maxHP;
      this.direction = direction;
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      this.hitbox.x += xSpeed * direction;
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
