package entities.flying.enemies;

import static utils.Constants.Flying.DrawOffsetConstants.FLAMEDRONE_OFFSET_X;
import static utils.Constants.Flying.DrawOffsetConstants.FLAMEDRONE_OFFSET_Y;
import static utils.Constants.Flying.SpriteSizes.FLAMEDRONE_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.FLAMEDRONE_SPRITE_WIDTH;
import static utils.Constants.Flying.TypeConstants.FLAMEDRONE;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;

public class FlameDrone extends Entity implements Enemy {
   // Actions
   private static final int PREPARING_TO_SHOOT = 2;
   private static final int IDLE = 1;
   private static final int TAKING_DAMAGE = 0;

   BufferedImage[][] animations;
   private float startY;
   private int maxHP = 120;
   private int HP = maxHP;
   private boolean onScreen = false;
   private boolean dead = false;

   private int action = IDLE;
   private int aniIndex = 0;
   private int aniTick;
   private int aniTickPerFrame = 3;   // This is set to 10 during PREPARE
   private int damageFrames = 10;
   private int damageTick = 0;

   private int shootTick = 0;  // See implementation of upodateShootTick and canShoot

   public FlameDrone(Rectangle2D.Float hitbox, BufferedImage[][] animations) {
      super(hitbox);
      startY = hitbox.y;
      this.animations = animations;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;
      onScreen = (((hitbox.y + hitbox.height + 15) > 0) && ((hitbox.y - 20) < Game.GAME_DEFAULT_HEIGHT));
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

   private void updateShootTick() {
      shootTick++;
      if (shootTick == 90) {
         action = PREPARING_TO_SHOOT;
         aniTickPerFrame = 5;
         aniTick = 0;
         aniIndex = 0;
      }
   }

   public boolean canShoot() {
      if (shootTick == 120) {
         aniTickPerFrame = 3;
         action = IDLE;
         return true;
      }
      return false;
   }

   @Override
   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   @Override
   public int getType() {
      return FLAMEDRONE;
   }

   @Override
   public void takeDamage(int damage) {
      this.HP -= damage;
      if (this.action != PREPARING_TO_SHOOT) {
         this.action = TAKING_DAMAGE;
      }
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
            animations[action][aniIndex],
            (int) ((hitbox.x - FLAMEDRONE_OFFSET_X) * Game.SCALE),
            (int) ((hitbox.y - FLAMEDRONE_OFFSET_Y) * Game.SCALE),
            (int) (FLAMEDRONE_SPRITE_WIDTH * 3 * Game.SCALE),
            (int) (FLAMEDRONE_SPRITE_HEIGHT * 3 * Game.SCALE), null);
   }

   private int getDroneSpriteAmount() {
      switch (action) {
         case TAKING_DAMAGE:
            return 3;
         case PREPARING_TO_SHOOT:
            return 6;
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
      shootTick = 0;
   }
}
