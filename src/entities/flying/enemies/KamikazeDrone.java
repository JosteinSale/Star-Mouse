package entities.flying.enemies;

import static utils.Constants.Flying.SpriteSizes.KAMIKAZEDRONE_SPRITE_SIZE;
import static utils.Constants.Flying.TypeConstants.KAMIKAZEDRONE;
import static utils.Constants.Flying.DrawOffsetConstants.KAMIKAZEDRONE_OFFSET;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.flying.PlayerFly;
import main.Game;

public class KamikazeDrone extends Entity implements Enemy {
   // Actions
   private static final int IDLE = 1;
   private static final int TAKING_DAMAGE = 0;

   PlayerFly player;
   BufferedImage[][] animations;
   private float startY;
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

   private int playerCollisions = 0;    // When the drone has collided 3 times, it explodes

   public KamikazeDrone(Rectangle2D.Float hitbox, BufferedImage[][] animations) {
      super(hitbox);
      startY = hitbox.y;
      this.animations = animations;
   }

   /** The kamikazedrone follows the player's position, so it needs access to the player */
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
         checkPlayerOverlap();
      }
   }

   private void checkPlayerOverlap() {
      if (playerCollisions == 2) {
         this.HP = 0;  // The drone will explode next time the takeDamage-method is called.
      }
      if (player.getHitbox().intersects(hitbox)) {
         this.playerCollisions++;
      }
   }

   private void moveTowardsPlayer() {
      if (Math.abs(player.getHitbox().x - hitbox.x) < 5) {  // To avoid jittering
         return;
      }
      if (player.getHitbox().x > hitbox.x) {  // Player is to the right of the drone
         this.hitbox.x += xSpeed;
      }
      else {
         this.hitbox.x -= xSpeed;   // Player is to the left of the drone
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
      return KAMIKAZEDRONE;
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
      return 0;  // No dir
   }

   @Override
   public void drawHitbox(Graphics g) {
      this.drawHitbox(g, 0, 0);
   }

   @Override
   public void draw(Graphics g) {
      g.drawImage(
            animations[action][aniIndex],
            (int) ((hitbox.x - KAMIKAZEDRONE_OFFSET) * Game.SCALE),
            (int) ((hitbox.y - KAMIKAZEDRONE_OFFSET) * Game.SCALE),
            (int) (KAMIKAZEDRONE_SPRITE_SIZE * 3 * Game.SCALE),
            (int) (KAMIKAZEDRONE_SPRITE_SIZE * 3 * Game.SCALE), null);
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
      action = IDLE;
      HP = maxHP;
      onScreen = false;
      dead = false;
      aniTick = 0;
      aniIndex = 0;
      damageTick = 0;
      this.playerCollisions = 0;
   }
}
