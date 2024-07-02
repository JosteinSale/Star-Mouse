package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import main_classes.Game;

public class TankDrone extends Entity implements Enemy {
   private EntityInfo info;

   private static final int IDLE = 1;
   private static final int TAKING_DAMAGE = 0;

   private float startY;
   private int maxHP = 300;
   private int HP = maxHP;
   private boolean onScreen = false;
   private boolean dead = false;

   private int action = IDLE;
   private int aniIndex = 0;
   private int aniTick;
   private int aniTickPerFrame = 3;
   private int damageFrames = 10;
   private int damageTick = 0;

   public TankDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox);
      startY = hitbox.y;
      this.info = info;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;
      onScreen = (((hitbox.y + hitbox.height) > 0) && (hitbox.y < Game.GAME_DEFAULT_HEIGHT));
      if (onScreen) {
         updateAniTick();
      }
   }

   private void updateAniTick() {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex >= getDroneSpriteAmount()) {
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
      return 0;  // No dir
   }

   public void resetShootTick() {}

   @Override
   public void drawHitbox(Graphics g) {
      this.drawHitbox(g, 0, 0);
   }

   @Override
   public void draw(Graphics g) {
      g.drawImage(
            info.animation[action][aniIndex],
            (int) ((hitbox.x - info.drawOffsetX) * Game.SCALE),
            (int) (hitbox.y * Game.SCALE),
            (int) (info.spriteW * 3 * Game.SCALE),
            (int) (info.spriteH * 3 * Game.SCALE), null);
   }

   private int getDroneSpriteAmount() {
      switch (action) {
         case TAKING_DAMAGE:
            return 3;
         case IDLE:
         default:
            return 1;
      }
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