package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import main_classes.Game;

public class WaspDrone extends Entity implements Enemy {
   private EntityInfo info;
   
   private static final int IDLE = 1;
   private static final int TAKING_DAMAGE = 0;

   private int direction; // 1 = right, -1 = left

   private float startY;
   private float startX;
   private int maxHP = 60;
   private int HP = maxHP;
   private boolean onScreen = false;
   private boolean dead = false;

   private int action = IDLE;
   private int aniIndex = 0;
   private int aniTick;
   private int aniTickPerFrame = 3;
   private int damageFrames = 10;
   private int damageTick = 0;
   private int shootTick = 0;
   private int shootTimer;

   public WaspDrone(Rectangle2D.Float hitbox, EntityInfo info, int direction, int shootTimer) {
      super(hitbox);
      startX = hitbox.x;
      startY = hitbox.y;
      this.info = info;
      this.direction = direction;
      this.shootTimer = shootTimer;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;
      onScreen = (((hitbox.y + hitbox.height) > 0) && (hitbox.y < Game.GAME_DEFAULT_HEIGHT));
      if (onScreen) {
         updateAniTick();
         updateShootTick();
      }
   }

   private void updateAniTick() {
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

   private void updateShootTick() {
      this.shootTick++;
   }

   public boolean canShoot() {
      return this.shootTick == shootTimer;
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
        this.takeShootDamage(damage);
    }

   @Override
   public boolean isDead() {
      return dead;
   }

   @Override
   public boolean isOnScreen() {
      return onScreen;
   }

   @Override
   public boolean isSmall() {
      return true;
   }

   @Override
   public int getDir() {
      return this.direction;
   }

   @Override
   public void drawHitbox(Graphics g) {
      this.drawHitbox(g, 0, 0);
   }

   @Override
   public void draw(Graphics g) {
      //drawHitbox(g);
      g.drawImage(
            info.animation[action][aniIndex],
            (int) ((hitbox.x - info.drawOffsetX + getFlipX()) * Game.SCALE),
            (int) ((hitbox.y - info.drawOffsetY) * Game.SCALE),
            (int) (info.spriteW * 3 * direction * Game.SCALE),
            (int) (info.spriteH * 3 * Game.SCALE), null);
   }

   private float getFlipX() {
      if (direction == 1) {
         return 0;
      } else {
         return (hitbox.width + 33);
      }
   }

   private int getSpriteAmount() {
      switch (action) {
         case TAKING_DAMAGE:
            return 4;
         case IDLE:
         default:
            return 1;
      }
   }

   @Override
   public void resetShootTick() {
      this.shootTick = 0;
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
      shootTick = 0;
   }

}
