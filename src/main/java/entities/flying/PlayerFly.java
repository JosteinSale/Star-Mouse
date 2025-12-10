package entities.flying;

import static utils.Constants.Flying.ActionConstants.*;
import static utils.HelpMethods.IsSolid;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.Entity;
import entities.MyCollisionImage;
import entities.flying.enemies.Enemy;
import main_classes.Game;
import ui.StatusDisplay;
import utils.Constants.Audio;

public class PlayerFly extends Entity implements ShootingPlayer {
   protected Game game;
   protected AudioPlayer audioPlayer;
   protected MyCollisionImage clImg;
   public ShipFlame flame;
   public StatusDisplay statusDisplay;
   protected Rectangle2D.Float teleportHitbox; // When the player teleports, a 'kill hitbox' materializes
   protected int teleportKillWidth = 100; // Width of said hitbox
   protected int teleportKillOffset; // The distance between the players hitbox and the teleport kill hitbox

   public int planeAction;
   protected float xSpeed = 0;
   protected float ySpeed = 0;
   protected float acceleration = 1.0f;
   protected float playerMaxSpeed = 8f;
   public boolean visible = true;
   protected float[] collisionXs = new float[9];
   protected float[] collisionYs = new float[9];
   protected int edgeDist = 20; // The minimum distance from player to edge of screen
   protected int pushDistance = 40; // How far the player is pushed back when taking damage
   public int teleportDistance = 250;
   protected int maxHP;
   protected int HP;
   protected int collisionDmg = 10;
   protected int flipX = 1; // 1 = right, -1 = left. Is used in checkTeleportCollision

   protected int aniTick = 0;
   protected int aniTickPerFrame = 3;
   public int aniIndex = 0;

   protected int iFrames = 20; // When player takes damage, they get 20 frames of invinsibilty
   protected int iFrameCount = 0;
   public int teleportBuffer = 0;
   protected int teleportCoolDown = 10; // The amount of frames between each time the player can teleport

   public PlayerFly(Game game, Float hitbox) {
      super(hitbox);
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      updateCollisionPixels();
      this.flame = new ShipFlame();
      this.teleportHitbox = new Rectangle2D.Float(
            hitbox.x, hitbox.y, teleportKillWidth, hitbox.height);
      this.teleportKillOffset = (int) (teleportDistance - hitbox.width - teleportKillWidth) / 2;
      this.statusDisplay = new StatusDisplay();
   }

   /** Updates maxHP and such from the progressValues */
   public void onLevelStart() {
      this.setKilledEnemies(0);
      int maxHp = game.getProgressValues().getMaxHP();
      this.maxHP = maxHp;
      this.HP = maxHp;
      statusDisplay.setMaxHP(maxHp);
      statusDisplay.setHP(maxHp);
   }

   /*
    * 9 pixels in the player hitbox are used for colission detection with enemies
    * and map. These are enumerated according to the order they should be checked
    * by the collission algorithms. To understand the logic, see the planning
    * notes.
    *
    * 4 - 0 - 5
    * 1 - 6 - 2
    * 7 - 3 - 8
    *
    */
   protected void updateCollisionPixels() {
      collisionXs[0] = hitbox.x + hitbox.width / 2;
      collisionXs[1] = hitbox.x;
      collisionXs[2] = hitbox.x + hitbox.width;
      collisionXs[3] = hitbox.x + hitbox.width / 2;
      collisionXs[4] = hitbox.x;
      collisionXs[5] = hitbox.x + hitbox.width;
      collisionXs[6] = hitbox.x + hitbox.width / 2;
      collisionXs[7] = hitbox.x;
      collisionXs[8] = hitbox.x + hitbox.width;

      collisionYs[0] = hitbox.y;
      collisionYs[1] = hitbox.y + hitbox.height / 2;
      collisionYs[2] = hitbox.y + hitbox.height / 2;
      collisionYs[3] = hitbox.y + hitbox.height;
      collisionYs[4] = hitbox.y;
      collisionYs[5] = hitbox.y;
      collisionYs[6] = hitbox.y + hitbox.height / 2;
      collisionYs[7] = hitbox.y + hitbox.height;
      collisionYs[8] = hitbox.y + hitbox.height;
   }

   public void update(float yLevelOffset, float xLevelOffset) {
      int prevAction = planeAction;
      handleKeyboardInputs();
      movePlayer();
      checkAndHandleCollision(yLevelOffset, xLevelOffset);
      if (planeAction != prevAction) {
         aniIndex = 0;
      }
      updateAniTick();
      flame.update();
      statusDisplay.update();
   }

   protected void handleKeyboardInputs() {

      // First: handles behaviour based on whether keys are pressed
      if ((planeAction == TAKING_COLLISION_DAMAGE)) { // Player can't control the plane while
         iFrameCount += 1; // taking collision damage.
         if (iFrameCount > iFrames) {
            planeAction = IDLE;
            iFrameCount = 0;
         }
      } else {
         if (game.rightIsPressed && game.teleportIsPressed && (teleportBuffer == 0)) {
            planeAction = TELEPORTING_RIGHT;
            teleportBuffer = teleportCoolDown;
            flipX = 1;
            return;
         } else if (game.leftIsPressed && game.teleportIsPressed && (teleportBuffer == 0)) {
            planeAction = TELEPORTING_LEFT;
            teleportBuffer = teleportCoolDown;
            flipX = -1;
            return;
         }
         if (game.upIsPressed) {
            planeAction = IDLE;
            ySpeed -= acceleration;
            if (ySpeed < -playerMaxSpeed) {
               ySpeed = -playerMaxSpeed;
            }
         } else if (game.downIsPressed) {
            planeAction = IDLE;
            ySpeed += acceleration;
            if (ySpeed > playerMaxSpeed) {
               ySpeed = playerMaxSpeed;
            }
         }
         if (game.leftIsPressed) {
            planeAction = FLYING_LEFT;
            xSpeed -= acceleration;
            if (xSpeed < -playerMaxSpeed) {
               xSpeed = -playerMaxSpeed;
            }
         }
         if (game.rightIsPressed) {
            planeAction = FLYING_RIGHT;
            xSpeed += acceleration;
            if (xSpeed > playerMaxSpeed) {
               xSpeed = playerMaxSpeed;
            }
         }
      }

      // Second: handles behaviour based on whether keys are NOT pressed
      if (!(game.leftIsPressed || game.rightIsPressed)) {
         if (xSpeed < 0) {
            xSpeed += acceleration / 2;
         } else if (xSpeed > 0) {
            xSpeed -= acceleration / 2;
         }
         if ((Math.abs(xSpeed)) < 0.6) { // OBS
            xSpeed = 0;
         }
         if ((planeAction != TAKING_COLLISION_DAMAGE) && (planeAction != TAKING_SHOOT_DAMAGE)) {
            planeAction = IDLE;
         }
      }
      if (!(game.upIsPressed || game.downIsPressed)) {
         if (ySpeed < 0) {
            ySpeed += acceleration / 2;
         } else if (ySpeed > 0) {
            ySpeed -= acceleration / 2;
         }
         if ((Math.abs(ySpeed)) < 0.6) {
            ySpeed = 0;
         }
      }

      if (teleportBuffer > 3) {
         xSpeed = 0;
      }
   }

   protected void movePlayer() {
      if ((planeAction == TELEPORTING_RIGHT) || (planeAction == TELEPORTING_LEFT)) {
         adjustPos(teleportDistance * flipX, 0);
         adjustTeleportHitbox();
         audioPlayer.playSFX(Audio.SFX_TELEPORT);
      } else {
         adjustPos(xSpeed, ySpeed);
      }
   }

   /**
    * Moves the player hitbox, and prevents it from going off screen.
    * Then it updates the collision pixels
    * 
    * @param deltaX
    * @param deltaY
    */
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
      if (hitbox.y < edgeDist) {
         hitbox.y = edgeDist;
         ySpeed = 0;
      }
      if ((hitbox.y + hitbox.height + edgeDist) > Game.GAME_DEFAULT_HEIGHT) {
         hitbox.y = Game.GAME_DEFAULT_HEIGHT - hitbox.height - edgeDist;
         ySpeed = 0;
      }
      updateCollisionPixels();
   }

   /**
    * Remember, this is called AFTER player has teleported. Thus the hitbox should
    * be positioned in relation to where the player WAS, just before they
    * teleported.
    */
   protected void adjustTeleportHitbox() {
      teleportHitbox.y = hitbox.y;
      if (flipX == 1) {
         teleportHitbox.x = hitbox.x - teleportKillOffset - teleportKillWidth;
      } else if (flipX == -1) {
         teleportHitbox.x = hitbox.x + hitbox.width + teleportKillOffset;
      }
   }

   /**
    * Handles teleport/normal collision with the map, and teleport collision with
    * big enemies.
    */
   private void checkAndHandleCollision(float yLevelOffset, float xLevelOffset) {
      if ((planeAction == TELEPORTING_RIGHT) || (planeAction == TELEPORTING_LEFT)) {
         checkAndHandleTeleportCollision(yLevelOffset, xLevelOffset);
      } else {
         int nrOfCollisions = 0;
         while (collidesWithMap(yLevelOffset, xLevelOffset)) {
            int collidingPixel = getPixelThatCollides(yLevelOffset, xLevelOffset);
            pushInOppositeDirectionOf(collidingPixel, pushDistance);
            nrOfCollisions += 1;
            if (nrOfCollisions > 2) { // failsafe, if the player gets stuck
               this.HP = 0; // kill player
               takeCollisionDmg();
               return;
            }
         }
         if (nrOfCollisions > 0) {
            takeCollisionDmg();
            audioPlayer.playSFX(Audio.SFX_COLLISION);
         }
      }
   }

   private void checkAndHandleTeleportCollision(float yLevelOffset, float xLevelOffset) {
      // Checks map
      if (collidesWithMap(yLevelOffset, xLevelOffset)) {
         handleTeleportCollisionWithWall(yLevelOffset, xLevelOffset);
      }
      // Checks big enemies
      else {
         ArrayList<Enemy> bigEnemies = game.getFlying().getBigEnemies();
         for (Enemy e : bigEnemies) {
            if (hitbox.intersects(e.getHitbox())) {
               handleTeleportCollisionWithBigEnemy(e, yLevelOffset, xLevelOffset);
            }
         }
      }
   }

   /**
    * Should be called if the player tried to teleport into a big enemy.
    * It moves the player back outside the wall, takes damage, and plays SFX.
    */
   private void handleTeleportCollisionWithBigEnemy(Enemy e, float yLevelOffset, float xLevelOffset) {
      this.planeAction = TAKING_COLLISION_DAMAGE;
      while (e.getHitbox().intersects(hitbox)) {
         int xMove = -(teleportDistance / 10 * flipX);
         adjustPos(xMove, 0);
      }
      takeCollisionDmg();
      audioPlayer.playSFX(Audio.SFX_COLLISION);
      e.onCollision(10);
      game.getFlying().checkIfDeadAndHandleDeath(e);
   }

   /**
    * Should be called if the player tried to teleport into a wall.
    * It moves the player back outside the wall, takes damage, and plays SFX.
    */
   protected void handleTeleportCollisionWithWall(float yLevelOffset, float xLevelOffset) {
      this.planeAction = TAKING_COLLISION_DAMAGE;
      while (collidesWithMap(yLevelOffset, xLevelOffset)) {
         int xMove = -(teleportDistance / 10 * flipX);
         adjustPos(xMove, 0);
      }
      takeCollisionDmg();
      audioPlayer.playSFX(Audio.SFX_COLLISION);
   }

   private boolean collidesWithMap(float yLevelOffset, float xLevelOffset) {
      for (int i = 0; i < 9; i++) {
         if (IsSolid(
               (int) (collisionXs[i] + xLevelOffset) / 3,
               (int) (collisionYs[i] - yLevelOffset) / 3,
               clImg)) {
            return true;
         }
      }
      return false;
   }

   private int getPixelThatCollides(float yLevelOffset, float xLevelOffset) {
      for (int i = 0; i < 9; i++) {
         if (IsSolid(
               (int) (collisionXs[i] + xLevelOffset) / 3,
               (int) (collisionYs[i] - yLevelOffset) / 3,
               clImg)) {
            return i;
         }
      }
      return -1;
   }

   /**
    * Is called from EnemyManager
    * Checks if the enemy overlaps the player.
    * If so, it checks collisionpixels (an extensive operation).
    * If a collision has occured, player takes damage and is pushed in opposite
    * direction.
    * 
    * (This method could be improved: instead of making 9 new points for each
    * enemy,
    * make 9 points once, and then check each enemyhitbox that is close enough.
    * Make this method in enemyManager, call getPlayerPixels(),
    * check those pixels for each enemy).
    */
   public boolean checkAndHandleCollisionWithEnemy(Rectangle2D.Float enemyHitbox) {
      if (hitbox.intersects(enemyHitbox)) {
         for (int i = 0; i < 9; i++) {
            Point point = new Point((int) collisionXs[i], (int) collisionYs[i]);
            if (enemyHitbox.contains(point)) {
               takeCollisionDmg();
               audioPlayer.playSFX(Audio.SFX_COLLISION);
               pushInOppositeDirectionOf(i, pushDistance);
               this.resetSpeed();
               return true;
            }
         }
      }
      return false;
   }

   public boolean teleportDamagesEnemy(Rectangle2D.Float enemyHitbox) {
      if (planeAction == TELEPORTING_RIGHT || planeAction == TELEPORTING_LEFT) {
         if (teleportHitbox.intersects(enemyHitbox)) {
            return true;
         }
      }
      return false;
   }

   /*
    * Remember, the pixels of the player hitbox are enumerated as such:
    * 4 - 0 - 5
    * 1 - 6 - 2
    * 7 - 3 - 8
    */
   protected void pushInOppositeDirectionOf(int i, float pushDistance) {
      switch (i) {
         case 0 -> adjustPos(0, pushDistance * 1.5f);
         case 1 -> adjustPos(pushDistance, 0);
         case 2 -> adjustPos(-pushDistance, 0);
         case 3 -> adjustPos(0, -pushDistance);
         case 4 -> adjustPos(pushDistance, pushDistance);
         case 5 -> adjustPos(-pushDistance, pushDistance);
         // case 6 -> do nothing
         case 7 -> adjustPos(pushDistance, -pushDistance);
         case 8 -> adjustPos(-pushDistance, -pushDistance);
      }
   }

   protected void updateAniTick() {
      this.teleportBuffer -= 1;
      if (teleportBuffer < 0) {
         teleportBuffer = 0;
      }
      this.aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
         if (aniIndex == GetPlayerSpriteAmount(planeAction)) {
            aniIndex = GetPlayerSpriteAmount(planeAction) - 1;
         }
      }
   }

   public void resetSpeed() {
      this.xSpeed = 0;
      this.ySpeed = 0;
   }

   public boolean takesCollisionDmg() {
      return (planeAction == TAKING_COLLISION_DAMAGE);
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
         game.getFlying().killPlayer();
      }
   }

   protected void takeCollisionDmg() {
      HP -= collisionDmg;
      this.aniTick = 0;
      this.aniIndex = 0;
      this.resetSpeed();
      this.planeAction = TAKING_COLLISION_DAMAGE;
      this.statusDisplay.setHP(HP);
      this.statusDisplay.setBlinking(true);
      if (HP <= 0) {
         game.getFlying().killPlayer();
      }
   }

   @Override
   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public void increaseHealth(int health) {
      this.HP += health;
      if (HP > maxHP) {
         HP = maxHP;
      }
      this.statusDisplay.setHP(this.HP);
   }

   @Override
   public void setMaxHp(int hp) {
      this.statusDisplay.setMaxHP(hp);
   }

   @Override
   public void setBombs(int nr) {
      this.statusDisplay.setBombs(nr);
   }

   public void setKilledEnemies(int nr) {
      this.statusDisplay.setKilledEnemies(nr);
   }

   public void setClImg(MyCollisionImage clImg) {
      this.clImg = clImg;
   }

   public void reset() {
      this.visible = true;
      this.aniIndex = 0;
      HP = maxHP;
      statusDisplay.setHP(this.HP);
      statusDisplay.setBlinking(false);
      statusDisplay.setKilledEnemies(0);
      hitbox.x = 500f;
      hitbox.y = 400f;
      updateCollisionPixels();
      resetSpeed();
      planeAction = IDLE;
   }
}
