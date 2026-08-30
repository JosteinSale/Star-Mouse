package entities.boss_mode;

import static utils.Constants.Flying.PlaneAction.*;

import java.util.ArrayList;

import entities.Dimensions;
import entities.MyCollisionImage;
import entities.MyRectangle;
import entities.flying.AnimatedGlow;
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
   private final int noFlyZone = 350; // Player cannot fly above this point
   private ArrayList<IBossPart> bossParts;

   private final int customIframes = 60; // The playerBoss should stay invincible for longer
   private int customIframeTick = 0;

   public PlayerBoss(Game game, Dimensions hitbox) {
      super(game, hitbox);
   }

   public void setBoss(ArrayList<IBossPart> bossParts) {
      this.bossParts = bossParts;
   }

   @Override
   public void update(float yLevelOffset, float xLevelOffset) {
      int prevAction = planeAction;
      handleKeyboardInputs();
      handleKeyboardNotPressed();
      movePlayer();
      checkBossInteraction();
      if (planeAction != prevAction) {
         aniIndex = 0;
      }
      updateCustomIframes();
      updateAniTick();
      flame.update();
      leftLazerGlow.update();
      rightLazerGlow.update();
      shipSmoke.update();
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
      move(deltaX, deltaY);
      if (x() < edgeDist) {
         setX(edgeDist);
         xSpeed = 0;
      }
      if ((x() + width() + edgeDist) > Game.GAME_DEFAULT_WIDTH) {
         setX(Game.GAME_DEFAULT_WIDTH - width() - edgeDist);
         xSpeed = 0;
      }
      if (y() < noFlyZone) {
         setY(noFlyZone);
         ySpeed = 0;
      }
      if ((y() + height() + edgeDist) > Game.GAME_DEFAULT_HEIGHT) {
         setY(Game.GAME_DEFAULT_HEIGHT - height() - edgeDist);
         ySpeed = 0;
      }
      collisionPixels.update();
      setGlowPositions();
   }

   private void checkBossInteraction() {
      checkBossCollision();
      checkBossTeleportHit();
   }

   private void checkBossTeleportHit() {
      if (planeAction == TELEPORTING_LEFT || planeAction == TELEPORTING_RIGHT) {
         for (IBossPart bp : bossParts) {
            if (bp.getHitbox().intersects(teleportHitbox)) {
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
            if (bp.containsPoint(this.collisionPixels.get()[i])) {
               this.takeCollisionDmg();
               audioPlayer.playSFX(Audio.SFX_COLLISION);
               pushInOppositeDirectionOf(i, pushDistance);
               collisionPixels.update();
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
      leftLazerGlow.reset();
      rightLazerGlow.reset();
      setGlowType(AnimatedGlow.BLUE_GLOW_SMALL);
      HP = maxHP;
      statusDisplay.setHP(this.HP);
      statusDisplay.setBlinking(false);
      setX(500f);
      setY(400f);
      collisionPixels.update();
      planeAction = IDLE;
   }

   // ----- Methods we don't use will do nothing / return false. -----
   @Override
   public boolean checkAndHandleCollisionWithEnemy(ArrayList<MyRectangle> hitboxesForEnemy) {
      return false;
   }

   @Override
   public boolean teleportHitsEnemy(ArrayList<MyRectangle> hitboxesForEnemy) {
      return false;
   }

   @Override
   public void setClImg(MyCollisionImage clImg) {
      /* Do nothing */}
}
