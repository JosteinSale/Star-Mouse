package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import entities.flying.EntityInfo;

/**
 * The BurningFragment has a lot of the same behavior as the SmallAsteroid,
 * except that it only has an ySpeed and not an xSpeed. I.e the first digit in
 * the shootInterval will be the ySpeed.
 */
public class BurningFragment extends BaseEnemy {
   private int ySpeed;

   public BurningFragment(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval) {
      super(hitbox, info);
      startY = hitbox.y;
      maxHP = 30;
      HP = maxHP;
      aniTickPerFrame = 4;
      IDLE = 0;
      TAKING_DAMAGE = 1;
      this.extractYSpeed(shootInterval);
   }

   /**
    * The first digit in the shootinterval will be y-speed.
    * 
    * @param shootInterval
    */
   private void extractYSpeed(int shootInterval) {
      if (shootInterval < 0) {
         throw new IllegalArgumentException("We can't have negative y-speed");
      }
      this.ySpeed = shootInterval / 100;
   }

   @Override
   protected void updateCustomBehavior(float __) {
      hitbox.y += ySpeed;
   }

   @Override
   public boolean canShoot() {
      return false;
   }

   @Override
   protected int getSpriteAmount() {
      return 8;
   }

   @Override
   public void onCollision(int damage) {
      // The asteroid explodes immediately upon colliding with the player.
      dead = true;
   }

   @Override
   public void resetShootTick() {
      // Do nothing
   }
}