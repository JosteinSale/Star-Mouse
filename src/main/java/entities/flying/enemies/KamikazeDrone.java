package entities.flying.enemies;

import entities.Dimensions;
import entities.flying.EntityInfo;
import entities.flying.PlayerFly;

public class KamikazeDrone extends BaseEnemy {
   private PlayerFly player;
   private int playerCollisions = 0; // When the drone has collided 3 times, it explodes
   private float xSpeed = 3;

   public KamikazeDrone(Dimensions hitbox, EntityInfo info, PlayerFly player) {
      super(hitbox, info);
      this.player = player;
      maxHP = 60;
      HP = maxHP;
      animation.setAmountOfFrames(2);
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      // Double the downward movement speed
      move(0, levelYSpeed);

      // Follow the player's X position
      if (Math.abs(player.x() - this.x()) < 5) { // To avoid jittering
         return;
      }
      if (player.x() > this.x()) { // Player is to the right of the drone
         move(xSpeed, 0);
      } else {
         move(-xSpeed, 0); // Player is to the left of the drone
      }
   }

   @Override
   public boolean canShoot() {
      return false;
   }

   @Override
   public void onCollision(int damage) {
      this.takeDamage(damage);
      this.playerCollisions++;
      if (playerCollisions == 3) {
         // The drone explodes after 3 collisions
         this.HP = 0;
         dead = true;
         playerCollisions = 0;
      }
   }
}
