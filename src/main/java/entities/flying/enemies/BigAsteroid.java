package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import main_classes.Game;

/** The big asteroid uses the same logic as SmallAsteroid to extract 
 * xSpeed and ySpeed, see the documentation for SmallAsteroid for explanation.
 * This is a big enemy, which cannot be teleport-killed.
 * Also it cannot take damage, and thus has no damage animation.
 */
public class BigAsteroid extends Entity implements Enemy {
   private EntityInfo info;

   private float startY;
   private float startX;
   private float xSpeed;
   private float ySpeed;
   private boolean onScreen = false;


   public BigAsteroid(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval, int direction) {
      super(hitbox);
      startY = hitbox.y;
      startX = hitbox.x;
      this.info = info;
      // Extract x- and y-Speed.
      this.extractXandYSpeed(shootInterval, direction);
   }

   /** Will use the shootInterval and direction to extract x- and y-Speed.
    * The first digit will be y-speed. The second will be x-speed.
    * If the direction is negative, the x-speed will be negative.
    * (Sadly we can't have negative y-speed, because currently we can't 
    * spawn enemies at the bottom of the screen).
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
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;
      onScreen = (((hitbox.y + hitbox.height + 50) > 0) && ((hitbox.y - 50) < Game.GAME_DEFAULT_HEIGHT));
      if (onScreen) {
         hitbox.y += ySpeed;
         hitbox.x += xSpeed;
      }
   }

   public boolean canShoot() {
      return false;
   }

   @Override
   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   @Override
   public int getType() {
      return info.typeConstant;
   }

   @Override
   public void takeShootDamage(int damage) {
      // Do nothing
   }

   @Override
   public void takeCollisionDamage(int damage) {
      this.takeShootDamage(damage);
   }

   @Override
   public boolean isDead() {
      return false;
   }

   @Override
   public boolean isOnScreen() {
      return onScreen;
   }

   @Override
   public boolean isSmall() {
      return false;
   }

   @Override
   public int getDir() {
      return 0; // No dir
   }

   public void resetShootTick() {
      // Do nothing
   }

   @Override
   public void drawHitbox(Graphics g) {
      this.drawHitbox(g, 0, 0);
   }

   @Override
   public void draw(Graphics g) {
      g.drawImage(
         info.animation[0][0],
         (int) ((hitbox.x - info.drawOffsetX) * Game.SCALE),
         (int) ((hitbox.y - info.drawOffsetY) * Game.SCALE),
         (int) (info.spriteW * 3 * Game.SCALE),
         (int) (info.spriteH * 3 * Game.SCALE), null);
   }


   @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      hitbox.x = startX;
      onScreen = false;
   }

}
