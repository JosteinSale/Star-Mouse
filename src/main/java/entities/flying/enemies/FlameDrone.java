package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;

public class FlameDrone extends BaseEnemy {
   private static final int PREPARING_TO_SHOOT = 2;
   private static final int IDLE = 1;
   private static final int TAKING_DAMAGE = 0;

   public FlameDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info);
      startY = hitbox.y;
      this.info = info;
      maxHP = 120;
      HP = maxHP;
   }

   @Override
   protected void updateShootTick() {
      shootTick++;
      if (shootTick == 90) {
         action = PREPARING_TO_SHOOT;
         aniTickPerFrame = 5;
         aniTick = 0;
         aniIndex = 0;
      }
   }

   @Override
   public boolean canShoot() {
      if (shootTick == 120) {
         aniTickPerFrame = 3;
         action = IDLE;
         return true;
      }
      return false;
   }

   @Override
   public void takeDamage(int damage) {
      this.HP -= damage;
      if (this.action != PREPARING_TO_SHOOT) {
         this.action = TAKING_DAMAGE;
      }
      this.damageTick = damageFrames;
      if (HP <= 0) {
         dead = true;
      }
   }

   @Override
   public void resetShootTick() {
      // Do nothing
   }

   @Override
   protected int getSpriteAmount() {
      switch (action) {
         case TAKING_DAMAGE:
            return 2;
         case PREPARING_TO_SHOOT:
            return 6;
         case IDLE:
         default:
            return 1;
      }
   }
}
