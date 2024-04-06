package entities.bossmode;

import static utils.Constants.Flying.ActionConstants.*;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.flying.PlayerFly;
import main_classes.Game;
import utils.Constants.Audio;

public class PlayerBoss extends PlayerFly {
   private int noFlyZone = 350;
 
   public PlayerBoss(Game game, Float hitbox) {
      super(game, hitbox);
   }


   @Override
   public void update(float yLevelOffset, float xLevelOffset) {
      int prevAction = planeAction;
      handleKeyboardInputs();
      movePlayer();
      checkBossInteraction();
      if (planeAction != prevAction) {
         aniIndex = 0;
      }
      updateAniTick();
      flame.update();
      statusDisplay.update();
   }



   /** Moves the player hitbox, and prevents it from going off screen */
   @Override
   protected void adjustPos(float deltaX, float deltaY) {
      hitbox.x += deltaX;
      hitbox.y += deltaY;
      if (hitbox.x < edgeDist) {
         hitbox.x = edgeDist;
         xSpeed = 0;
      }
      if ((hitbox.x + hitbox.width + edgeDist) > Game.GAME_DEFAULT_WIDTH) {
         hitbox.x = Game.GAME_DEFAULT_WIDTH - hitbox.width - edgeDist;
         xSpeed = 0;
      }
      if (hitbox.y < noFlyZone) {
         hitbox.y = noFlyZone;
         ySpeed = 0;
      }
      if ((hitbox.y + hitbox.height + edgeDist) > Game.GAME_DEFAULT_HEIGHT) {
         hitbox.y = Game.GAME_DEFAULT_HEIGHT - hitbox.height - edgeDist;
         ySpeed = 0;
      }
   }


   private void checkBossInteraction() {
      // checkBossCollision(boss.getParts());
      // checkBossTeleportHit(boss.getParts());
   }

   private void checkBossTeleportHit() {

   }


   /**
    * Checks if any of the boss-rectangles overlaps the player.
    * If so, it checks whether player should be pushed in the opposite direction.
    * If a collision has occured, player takes damage and SFX are played.
    * 
    * (This method could be improved: instead of making 9 new points for each
    * enemy,
    * make 9 points once, and then check each enemyhitbox that is close enough.
    * Make this method in enemyManager, call getPlayerPixels(),
    * check those pixels for each enemy).
    */
   private void checkBossCollision(Rectangle2D.Float bossHitbox) {
      // TODO - for (box2d b : bossParts)
      if (hitbox.intersects(bossHitbox)) {
         // TODO - if (bossRect.shouldPushPlayer) {
         for (int i = 0; i < 9; i++) {
            Point point = new Point((int) collisionXs[i], (int) collisionYs[i]);
            if (bossHitbox.contains(point)) {
               pushInOppositeDirectionOf(i, pushDistance);
               this.updateCollisionPixels();
               this.resetSpeed();
            }
         }
         takeCollisionDmg();
         audioPlayer.playSFX(Audio.SFX_COLLISION);
      }
   }

   public boolean teleportHitsBossPart(Rectangle2D.Float bossHitbox) {
      if (planeAction == TELEPORTING_RIGHT || planeAction == TELEPORTING_LEFT) {
         if (teleportHitbox.intersects(bossHitbox)) {
            return true;
         }
      }
      return false;
   }


   @Override
   public void takeShootDamage(int damage) {
      this.HP -= damage;
      this.aniTick = 0;
      this.aniIndex = 0;
      this.planeAction = TAKING_SHOOT_DAMAGE;
      this.statusDisplay.setHP(this.HP);
      this.statusDisplay.setBlinking(true);
      if (HP <= 0) {
         // TODO - game.getBossMode.killPlayer();
      }
   }

   @Override
   protected void takeCollisionDmg() {
      HP -= collisionDmg;
      this.aniTick = 0;
      this.aniIndex = 0;
      this.resetSpeed();
      this.planeAction = TAKING_COLLISION_DAMAGE;
      this.statusDisplay.setHP(HP);
      this.statusDisplay.setBlinking(true);
      if (HP <= 0) {
         // TODO - game.getBossMode.killPlayer();
      }
   }

   @Override
   public void reset() {
      this.visible = true;
      this.aniIndex = 0;
      HP = maxHP;
      statusDisplay.setHP(this.HP);
      statusDisplay.setBlinking(false);
      //statusDisplay.setKilledEnemies(0);   Don't alter this.
      hitbox.x = 500f;
      hitbox.y = 400f;
      updateCollisionPixels();
      planeAction = IDLE;
  }
}
