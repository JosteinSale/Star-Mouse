package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import main_classes.Game;

/**
 * The big asteroid uses the same logic as SmallAsteroid to extract
 * xSpeed and ySpeed, see the documentation for SmallAsteroid for explanation.
 * This is a big enemy, which cannot be teleport-killed.
 * Also it cannot take damage, and thus has no damage animation.
 * If the player collides with it, the asteroid's speed is reduced by 1 unit.
 */
public class BigAsteroid extends BaseEnemy {
   private float startXSpeed;
   private float startYSpeed;
   private float curXSpeed;
   private float curYSpeed;

   public BigAsteroid(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval, int direction) {
      super(hitbox, info);
      startY = hitbox.y;
      startX = hitbox.x;
      this.info = info;
      // Extract x- and y-Speed.
      this.extractXandYSpeed(shootInterval, direction);
      this.curXSpeed = startXSpeed;
      this.curYSpeed = startYSpeed;
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
      this.startYSpeed = shootInterval / 100;
      this.startXSpeed = Math.abs((shootInterval / 10) % 10) * direction;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;
      onScreen = (((hitbox.y + hitbox.height + 50) > 0) && ((hitbox.y - 50) < Game.GAME_DEFAULT_HEIGHT));
      if (onScreen) {
         hitbox.y += curYSpeed;
         hitbox.x += curXSpeed;
      }
   }

   @Override
   public boolean canShoot() {
      return false;
   }

   @Override
   public void takeDamage(int damage) {
      // Do nothing
   }

   @Override
   public void onCollision(int damage) {
      this.reduceAsteroidSpeed();
   }

   private void reduceAsteroidSpeed() {
      if (Math.abs(curXSpeed) > 0) {
         int decrement = (int) (curXSpeed / Math.abs(curXSpeed)); // will be +1 or -1
         curXSpeed -= decrement;
      }
      if (Math.abs(curYSpeed) > 0) {
         int decrement = (int) (curYSpeed / Math.abs(curYSpeed)); // will be +1 or -1
         curYSpeed -= decrement;
      }
   }

   @Override
   public boolean isDead() {
      return false;
   }

   @Override
   public boolean isSmall() {
      return false;
   }

   @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      hitbox.x = startX;
      curXSpeed = startXSpeed;
      curYSpeed = startYSpeed;
      onScreen = false;
   }

   @Override
   public int getAction() {
      return 0; // Only one action
   }

   @Override
   public int getAniIndex() {
      return 0; // Only one image in animation
   }

}
