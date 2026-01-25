package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import entities.flying.EntityInfo;

/**
 * The SmallAsteroid will use the shootInterval and direction
 * from the levelData-sheet to extract x- and y-Speed.
 * The first digit will be y-speed. The second will be x-speed.
 * If the direction is negative, the x-speed will be negative.
 * (Sadly we can't have negative y-speed, because currently we can't
 * spawn enemies at the bottom of the screen).
 * 
 * If it makes contact with player, it explodes.
 */
public class SmallAsteroid extends BaseEnemy {
   private int VARIANT_INDEX; // Each asteroid will be randomized to look like 1 of 4 variant
   private int xSpeed;
   private int ySpeed;

   public SmallAsteroid(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval, int direction) {
      super(hitbox, info);
      maxHP = 30;
      HP = maxHP;
      IDLE = 0;
      TAKING_DAMAGE = 1;

      // Randomize the look of the asteroid into 1 of 4 variants.
      Random rand = new Random();
      this.VARIANT_INDEX = rand.nextInt(4);

      // Extract x- and y-Speed.
      this.extractXandYSpeed(shootInterval, direction);
   }

   /**
    * Will use the shootInterval and direction to extract x- and y-Speed.
    * The first digit will be y-speed. The second will be x-speed.
    * If the direction is negative, the x-speed will be negative.
    * (Sadly we can't have negative y-speed, because currently we can't
    * spawn enemies at the bottom of the screen).
    * 
    * @param shootInterval
    * @param direction
    */
   private void extractXandYSpeed(int shootInterval, int direction) {
      if (shootInterval < 0) {
         throw new IllegalArgumentException("We can't have negative y-speed");
      }
      this.ySpeed = shootInterval / 100;
      this.xSpeed = Math.abs((shootInterval / 10) % 10) * direction;
   }

   @Override
   protected void updateCustomBehavior(float __) {
      hitbox.y += ySpeed;
      hitbox.x += xSpeed;
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

   @Override
   public int getAction() {
      return action + (VARIANT_INDEX * 2);
   }
}
