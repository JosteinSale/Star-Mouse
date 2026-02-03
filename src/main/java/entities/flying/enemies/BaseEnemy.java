package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import entities.flying.AnimatedGlow;
import main_classes.Game;

/**
 * Base class for most enemy types in the game.
 * This class serves as a common ancestor for various enemy implementations.
 * It defaults to:
 * - being small
 * - moving downwards only
 * - having only one direction
 * - shooting at set intervals
 * - having 2 states: idle and taking damage
 * All of these can be overridden by subclasses if needed.
 */
public abstract class BaseEnemy extends Entity implements Enemy {
   protected EntityInfo info;
   public AnimatedGlow glow; // Can be null
   protected float startY;
   protected float startX;
   protected int maxHP = 60;
   protected int HP = maxHP;
   protected boolean onScreen = false;
   protected boolean dead = false;

   protected int TAKING_DAMAGE = 0;
   protected int IDLE = 1;

   protected int action = IDLE;
   protected int aniIndex = 0;
   protected int aniTick;
   protected int aniTickPerFrame = 4;
   protected int damageFrames = 10;
   protected int damageTick = 0;

   protected int shootTick = 0;
   protected int shootInterval;

   public BaseEnemy(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval, AnimatedGlow glow) {
      super(hitbox);
      this.info = info;
      startY = hitbox.y;
      startX = hitbox.x;
      this.shootInterval = shootInterval;
      this.glow = glow;
   }

   public BaseEnemy(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox);
      this.info = info;
      startY = hitbox.y;
      startX = hitbox.x;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;

      onScreen = (((hitbox.y + hitbox.height * 1.2) > 0) &&
            (hitbox.y - hitbox.height * 0.2 < Game.GAME_DEFAULT_HEIGHT));

      if (onScreen) {
         updateAniTick();
         updateShootTick();
         updateCustomBehavior(levelYSpeed);
      }
   }

   /**
    * Will be called only when the enemy is on screen.
    * Override with custom behavior if needed
    */
   protected void updateCustomBehavior(float levelYSpeed) {
      // Default behavior: Do nothing
   }

   protected void updateAniTick() {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex >= getSpriteAmount()) {
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

   protected void updateShootTick() {
      shootTick++;
   }

   public boolean canShoot() {
      return shootTick == shootInterval;
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
   public void takeDamage(int damage) {
      this.HP -= damage;
      this.action = TAKING_DAMAGE;
      this.damageTick = damageFrames;
      if (HP <= 0) {
         dead = true;
      }
   }

   @Override
   public boolean isDead() {
      return dead;
   }

   @Override
   public int getDir() {
      return 1; // Only one dir
   }

   @Override
   public boolean isOnScreen() {
      return onScreen;
   }

   @Override
   public boolean isSmall() {
      return true;
   }

   protected int getSpriteAmount() {
      if (action == TAKING_DAMAGE) {
         return 2;
      } else {
         return 1;
      }
   }

   @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      hitbox.x = startX;
      if (glow != null)
         glow.reset();
      action = IDLE;
      HP = maxHP;
      onScreen = false;
      dead = false;
      aniTick = 0;
      aniIndex = 0;
      damageTick = 0;
      shootTick = 0;
   }

   @Override
   public void onCollision(int damage) {
      this.takeDamage(damage);
   }

   @Override
   public EntityInfo getInfo() {
      return this.info;
   }

   @Override
   public int getAction() {
      return this.action;
   }

   @Override
   public int getAniIndex() {
      return this.aniIndex;
   }

   @Override
   public void onShoot() {
      if (glow != null)
         glow.start();
      this.shootTick = 0;
   }

   @Override
   public boolean hasGlow() {
      return glow != null;
   }

   @Override
   public AnimatedGlow getGlow() {
      return this.glow;
   }

}
