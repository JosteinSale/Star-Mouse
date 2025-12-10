package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.Entity;
import entities.flying.EntityInfo;
import entities.flying.PlayerFly;
import main_classes.Game;

public class KamikazeDrone extends Entity implements Enemy {
   private EntityInfo info;
   private static final int IDLE = 1;
   private static final int TAKING_DAMAGE = 0;

   private PlayerFly player;
   private float startY;
   private float startX;
   private float xSpeed = 3;
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

   private int playerCollisions = 0; // When the drone has collided 3 times, it explodes

   public KamikazeDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox);
      startY = hitbox.y;
      startX = hitbox.x;
      this.info = info;
   }

   /**
    * The kamikazedrone follows the player's position, so it needs access to the
    * player
    */
   public void setPlayer(PlayerFly player) {
      this.player = player;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;
      onScreen = (((hitbox.y + hitbox.height) > 0) && (hitbox.y < Game.GAME_DEFAULT_HEIGHT));
      if (onScreen) {
         hitbox.y += levelYSpeed;
         updateAniTick();
         moveTowardsPlayer();
      }
   }

   private void moveTowardsPlayer() {
      if (Math.abs(player.getHitbox().x - hitbox.x) < 5) { // To avoid jittering
         return;
      }
      if (player.getHitbox().x > hitbox.x) { // Player is to the right of the drone
         this.hitbox.x += xSpeed;
      } else {
         this.hitbox.x -= xSpeed; // Player is to the left of the drone
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
   public void takeDamage(int damage) {
      this.HP -= damage;
      this.action = TAKING_DAMAGE;
      this.damageTick = damageFrames;
      if (HP <= 0) {
         dead = true;
      }
   }

   @Override
   public void onCollision(int damage) {
      this.playerCollisions++;
      this.HP -= damage;
      if (playerCollisions == 3) {
         // The drone explodes after 3 collisions
         this.HP = 0;
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

   public void resetShootTick() {
      // Do nothing
   }

   @Override
   public int getDir() {
      return 1; // Only one dir
   }

   private int getSpriteAmount() {
      switch (action) {
         case TAKING_DAMAGE:
            return 3;
         case IDLE:
         default:
            return 2;
      }
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
      this.playerCollisions = 0;
   }

   @Override
   public EntityInfo getInfo() {
      return info;
   }

   @Override
   public int getAction() {
      return action;
   }

   @Override
   public int getAniIndex() {
      return aniIndex;
   }
}
