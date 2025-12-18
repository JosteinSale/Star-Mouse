package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.PlayerFly;

public class KamikazeDrone extends BaseEnemy {
   private PlayerFly player;
   private int playerCollisions = 0; // When the drone has collided 3 times, it explodes
   private float xSpeed = 3;

   public KamikazeDrone(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox, info);
      maxHP = 60;
      HP = maxHP;
   }

   /**
    * The kamikazedrone follows the player's position, so it needs access to the
    * player
    */
   public void setPlayer(PlayerFly player) {
      this.player = player;
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      // Double the downward movement speed
      this.hitbox.y += levelYSpeed;

      // Follow the player's X position
      if (Math.abs(player.getHitbox().x - hitbox.x) < 5) { // To avoid jittering
         return;
      }
      if (player.getHitbox().x > hitbox.x) { // Player is to the right of the drone
         this.hitbox.x += xSpeed;
      } else {
         this.hitbox.x -= xSpeed; // Player is to the left of the drone
      }
   }

   @Override
   public boolean canShoot() {
      return false;
   }

   @Override
   public void onCollision(int damage) {
      this.playerCollisions++;
      this.HP -= damage;
      if (playerCollisions == 3) {
         // The drone explodes after 3 collisions
         this.HP = 0;
         dead = true;
         playerCollisions = 0;
      }
   }

   @Override
   public void resetShootTick() {
      // Do nothing
   }

   @Override
   protected int getSpriteAmount() {
      return 2;
   }
}
