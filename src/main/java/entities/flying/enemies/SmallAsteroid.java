package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import entities.Entity;
import entities.flying.EntityInfo;
import main_classes.Game;

/** The SmallAsteroid will use the shootInterval and direction 
 * from the levelData-sheet to extract x- and y-Speed.
 * The first digit will be y-speed. The second will be x-speed.
 * If the direction is negative, the x-speed will be negative.
 * (Sadly we can't have negative y-speed, because currently we can't 
 * spawn enemies at the bottom of the screen).
 * 
 * If it makes contact with player, it explodes.
 */
public class SmallAsteroid extends Entity implements Enemy {
   private EntityInfo info;

   private int VARIANT_INDEX;   // Each asteroid will be randomized to look like 1 of 4 variants.
   private static final int IDLE = 0;
   private static final int TAKING_DAMAGE = 1;

   private float startY;
   private float startX;
   private int maxHP = 30;
   private int HP = maxHP;
   private boolean onScreen = false;
   private boolean dead = false;

   private int action = IDLE;
   private int aniIndex = 0;
   private int aniTick;
   private int aniTickPerFrame = 3;
   private int damageFrames = 10;
   private int damageTick = 0;

   private int xSpeed;
   private int ySpeed;

   public SmallAsteroid(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval, int direction) {
      super(hitbox);
      startY = hitbox.y;
      startX = hitbox.x;
      this.info = info;

      // Randomize the look of the asteroid into 1 of 4 variants.
      Random rand = new Random();
      this.VARIANT_INDEX = rand.nextInt(4);

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
      onScreen = (((hitbox.y + hitbox.height) > 0) && (hitbox.y < Game.GAME_DEFAULT_HEIGHT));
      if (onScreen) {
         updateAniTick();
         updateBehavior();
      }
   }

   private void updateBehavior() {
      hitbox.y += ySpeed;
      hitbox.x += xSpeed;
   }

   private void updateAniTick() {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex >= 2) {
            aniIndex = 0;
         }
      }
      if (action == TAKING_DAMAGE) {
         damageTick--;
         if (damageTick <= 0) {
            action = IDLE;
         }
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
      this.HP -= damage;
      this.action = TAKING_DAMAGE;
      this.damageTick = damageFrames;
      if (HP <= 0) {
         dead = true;
      }
   }

   @Override
   public void takeCollisionDamage(int damage) {
      // The asteroid explodes immediately upon colliding with the player.
      dead = true; 
   }

   @Override
   public boolean isDead() {
      return dead;
   }

   @Override
   public int getDir() {
      return 0; // No dir
   }

   @Override
   public boolean isOnScreen() {
      return onScreen;
   }

   @Override
   public boolean isSmall() {
      return true;
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
            info.animation[action + (VARIANT_INDEX * 2)][aniIndex],
            (int) ((hitbox.x - info.drawOffsetX) * Game.SCALE),
            (int) ((hitbox.y - info.drawOffsetY) * Game.SCALE),
            (int) (info.spriteW * 3 * Game.SCALE),
            (int) (info.spriteH * 3 * Game.SCALE), null);
   }

   @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      hitbox.x = startX;
      action = IDLE;
      HP = maxHP;
      onScreen = false;
      dead = false;
      aniTick = 0;
      aniIndex = 0;
      damageTick = 0;
   }
}
