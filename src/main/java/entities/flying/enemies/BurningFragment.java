package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import main_classes.Game;

/** The BurningFragment has a lot of the same behavior as the SmallAsteroid,
 * except that it only has an ySpeed and not an xSpeed. I.e the first digit in the 
 * shootInterval will be the ySpeed.
 */
public class BurningFragment extends Entity implements Enemy {
   private EntityInfo info;

   private static final int IDLE = 0;
   private static final int TAKING_DAMAGE = 1;

   private float startY;
   private int maxHP = 30;
   private int HP = maxHP;
   private boolean onScreen = false;
   private boolean dead = false;

   private int action = IDLE;
   private int aniIndex = 0;
   private int aniTick;
   private int aniTickPerFrame = 5;
   private int damageFrames = 10;
   private int damageTick = 0;

   private int ySpeed;

   public BurningFragment(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval) {
      super(hitbox);
      startY = hitbox.y;
      this.info = info;

      // Extract y-Speed.
      this.extractYSpeed(shootInterval);
   }

   /** 
    * The first digit in the shootinterval will be y-speed. 
    * @param shootInterval
    */
   private void extractYSpeed(int shootInterval) {
      if (shootInterval < 0) {
         throw new IllegalArgumentException("We can't have negative y-speed");
      }
      this.ySpeed = shootInterval / 100;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;
      onScreen = (((hitbox.y + hitbox.height) > 0) && ((hitbox.y - 200) < Game.GAME_DEFAULT_HEIGHT));
      if (onScreen) {
         updateAniTick();
         updateBehavior();
      }
   }

   private void updateBehavior() {
      hitbox.y += ySpeed;
   }

   private void updateAniTick() {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex >= 3) {
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
            info.animation[action][aniIndex],
            (int) ((hitbox.x - info.drawOffsetX) * Game.SCALE),
            (int) ((hitbox.y - info.drawOffsetY) * Game.SCALE),
            (int) (info.spriteW * 3 * Game.SCALE),
            (int) (info.spriteH * 3 * Game.SCALE), null);
   }

   @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      action = IDLE;
      HP = maxHP;
      onScreen = false;
      dead = false;
      aniTick = 0;
      aniIndex = 0;
      damageTick = 0;
   }
}