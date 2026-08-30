package entities.flying.enemies;

import entities.Dimensions;
import entities.flying.EntityInfo;
import main_classes.Game;

/**
 * The BurningFragment has a lot of the same behavior as the SmallAsteroid,
 * except that it only has an ySpeed and not an xSpeed. I.e the first digit in
 * the shootInterval will be the ySpeed.
 */
public class BurningFragment extends BaseEnemy {
   private int ySpeed;

   public BurningFragment(Dimensions hitbox, EntityInfo info, int shootInterval) {
      super(hitbox, info);
      startY = hitbox.y();
      maxHP = 30;
      HP = maxHP;
      animation.setAmountOfFrames(8);
      animation.setAniTickPerFrame(3);
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
   protected void checkOnScreen(float levelYSpeed) {
      onScreen = (((y() + height() * 1.2) > 0) &&
            (y() - 150 < Game.GAME_DEFAULT_HEIGHT));
   }

   @Override
   protected void updateCustomBehavior(float __) {
      move(0, ySpeed);
   }

   @Override
   public boolean canShoot() {
      return false;
   }

   @Override
   public void onCollision(int damage) {
      // The asteroid explodes immediately upon colliding with the player.
      dead = true;
   }
}