package entities.bossmode;

import static utils.Constants.Flying.ActionConstants.*;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import entities.MyCollisionImage;
import entities.flying.PlayerFly;
import main_classes.Game;
import utils.Constants.Audio;

/**
 * This class extends the PlayerFly object.
 * All logic pertaining to handling of keyboard-inputs and player
 * movement is kept identical to PlayerFly.
 * Drawing method and basic getters and setters are also the same.
 * 
 * There is a unique @Override-implementation of the following:
 * -update method
 * -adjustPos (player cannot move into the noFlyZone)
 * -takeDamage-methods
 * -reset-method
 * -unused methods (do nothing)
 * 
 * There are additional methods added for collision with- and teleport-hitting
 * the boss.
 */
public class PlayerBoss extends PlayerFly {
   private int noFlyZone = 350; // Player cannot fly above this point
   private ArrayList<IBossPart> bossParts;

   private int customIframes = 60; // The playerBoss should stay invincible for longer
   private int customIframeTick = 0;

   public PlayerBoss(Game game, Float hitbox) {
      super(game, hitbox);
   }

   public void setBoss(ArrayList<IBossPart> bossParts) {
      this.bossParts = bossParts;
   }

   @Override
   public void update(float yLevelOffset, float xLevelOffset) {
      int prevAction = planeAction;
      handleKeyboardInputs();
      movePlayer();
      updateCollisionPixels();
      checkBossInteraction();
      if (planeAction != prevAction) {
         aniIndex = 0;
      }
      updateCustomIframes();
      updateAniTick();
      flame.update();
      statusDisplay.update();
   }

   private void updateCustomIframes() {
      if (customIframeTick > 0) {
         customIframeTick--;
      }
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
      checkBossCollision();
      checkBossTeleportHit();
   }

   private void checkBossTeleportHit() {
      if (planeAction == TELEPORTING_LEFT || planeAction == TELEPORTING_RIGHT) {
         for (IBossPart bp : bossParts) {
            if (bp.intersectsRect(teleportHitbox)) {
               bp.onTeleportHit();
            }
         }
      }
   }

   /** Can be used in cutscenes */
   public void updateOnlyFlame() {
      this.flame.update();
   }

   /**
    * First checks if the player is currently invincible due to damage.
    * If yes, it returns.
    * Then it checks if any of the bossParts are active.
    * If so, it checks the collisionPixels against the bossPart.
    * If a collision has occured, it pushes the player in the opposite direction,
    * takes damage, plays SFX, and notifies the bossPart.
    * 
    * (This method could be improved: instead of making 9 new points for each
    * enemy, make 9 points once, and then check each enemyhitbox that is close
    * enough. Make this method in enemyManager, call getPlayerPixels(),
    * check those pixels for each enemy).
    */
   private void checkBossCollision() {
      if (isInvincible()) {
         return;
      }
      for (IBossPart bp : bossParts) {
         if (!bp.canCollide()) {
            continue;
         }
         for (int i = 0; i < 9; i++) {
            Point point = new Point((int) collisionXs[i], (int) collisionYs[i]);
            if (bp.containsPoint(point)) {
               this.takeCollisionDmg();
               audioPlayer.playSFX(Audio.SFX_COLLISION);
               pushInOppositeDirectionOf(i, pushDistance);
               this.updateCollisionPixels();
               this.resetSpeed();
               bp.onPlayerCollision();
               return;
            }
         }
      }
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
         game.getBossMode().killPlayer();
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
      this.customIframeTick = customIframes;
      if (HP <= 0) {
         game.getBossMode().killPlayer();
      }
   }

   private boolean isInvincible() {
      return (this.customIframeTick > 0 || this.iFrameCount > 0);
   }

   @Override
   public void reset() {
      this.visible = true;
      this.aniIndex = 0;
      this.resetSpeed();
      HP = maxHP;
      statusDisplay.setHP(this.HP);
      statusDisplay.setBlinking(false);
      hitbox.x = 500f;
      hitbox.y = 600f;
      updateCollisionPixels();
      planeAction = IDLE;
   }

   // Methods we don't use will do nothing / return false.
   @Override
   public boolean collidesWithEnemy(Rectangle2D.Float enemyHitbox) {
      return false;
   }

   @Override
   public boolean teleportDamagesEnemy(Rectangle2D.Float enemyHitbox) {
      return false;
   }

   @Override
   public void setClImg(MyCollisionImage clImg) {
      /* Do nothing */}
}
