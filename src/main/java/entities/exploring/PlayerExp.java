package entities.exploring;

import static utils.Constants.Exploring.DirectionConstants.*;
import static utils.HelpMethods.CollidesWithMap;
import static utils.HelpMethods.CollidesWithNpc;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import main_classes.Game;
import utils.Images;
import utils.ResourceLoader;

public class PlayerExp extends Entity {
   private Game game;
   private BufferedImage collisionImg;

   private boolean poseActive = false;
   public int playerAction;
   private int playerDirection = LEFT;
   private float playerSpeed = 5f;
   public boolean visible = true;

   public static int CURRENT_SPRITE_SHEET = 0;
   private int aniTick = 0;
   private int aniTickPerFrame = 8; // Antall ticks per gang animasjonen oppdateres
   public int aniIndex = 0;

   public PlayerExp(Game game, Float hitbox, int direction, Integer level, Integer area) {
      super(hitbox);
      this.game = game;
      this.playerDirection = direction;
      playerAction = STANDING;
      String imgName = "level" + level.toString() + "_area" + area.toString();
      this.collisionImg = game.getImages().getExpImageCollision(imgName + "_cl.png").getImage();

   }

   public void keyPressed(KeyEvent e) {
   }

   public void KeyReleased(KeyEvent e) {
   }

   public void update(ArrayList<Rectangle2D.Float> npcHitboxes, boolean cutsceneActive, boolean standardFadeActive) {
      if (!cutsceneActive && !standardFadeActive) {
         handleKeyboardInputs(npcHitboxes);
         updatePlayerAction();
      }
      updateAniTick();
   }

   private void handleKeyboardInputs(ArrayList<Rectangle2D.Float> npcHitboxes) {
      if (game.leftIsPressed) {
         playerAction = WALKING_LEFT;
         playerDirection = LEFT;
         hitbox.x -= playerSpeed;
         if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
            hitbox.x += playerSpeed;
         }
      }
      if (game.upIsPressed && !(game.leftIsPressed && game.rightIsPressed)) {
         playerAction = WALKING_UP;
         playerDirection = UP;
         hitbox.y -= playerSpeed;
         if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
            hitbox.y += playerSpeed;
         }
      }
      if (game.rightIsPressed) {
         playerAction = WALKING_RIGHT;
         playerDirection = RIGHT;
         hitbox.x += playerSpeed;
         if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
            hitbox.x -= playerSpeed;
         }
      }
      if (game.downIsPressed && !(game.leftIsPressed && game.rightIsPressed)) {
         playerAction = WALKING_DOWN;
         playerDirection = DOWN;
         hitbox.y += playerSpeed;
         if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
            hitbox.y -= playerSpeed;
         }
      }
   }

   private void updatePlayerAction() {
      if (!game.upIsPressed && !game.downIsPressed && !game.leftIsPressed && !game.rightIsPressed) {
         playerAction = STANDING;
         return;
      }
      if ((game.upIsPressed && game.downIsPressed) || (game.leftIsPressed && game.rightIsPressed)) {
         playerAction = STANDING;
      }
   }

   private void updateAniTick() {
      if (poseActive) {
         return;
      }
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniIndex++;
         aniTick = 0;
      }
      if (aniIndex >= GetSpriteAmount(playerAction)) {
         aniIndex = 0;
      }
      if (playerAction == STANDING) {
         aniIndex = playerDirection;
      }
   }

   public void resetAll() {
      game.downIsPressed = false;
      game.rightIsPressed = false;
      game.leftIsPressed = false;
      game.upIsPressed = false;
      this.playerAction = STANDING;
      this.aniTick = 0;
      this.aniIndex = 0;
   }

   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   public void setDirection(int dir) {
      this.playerDirection = dir;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public void setCURRENT_SPRITE_SHEET(int sheetIndex) {
      PlayerExp.CURRENT_SPRITE_SHEET = sheetIndex;
   }

   public void adjustPos(float deltaX, float deltaY) {
      hitbox.x += deltaX;
      hitbox.y += deltaY;
   }

   public void setAction(int action) {
      this.playerAction = action;
   }

   public void setSprite(boolean pose, int colIndex, int rowIndex) {
      if (pose == true) {
         this.poseActive = true;
         this.playerAction = rowIndex;
         this.aniIndex = colIndex;
      } else { // Stop posing
         this.poseActive = false;
         this.playerAction = STANDING;
         this.aniIndex = 0;
      }
   }

   /** Moves the player 20 px in the reenter-direction specified for this portal */
   public void adjustReenterPos(int reenterDir) {
      int adjustDistance = 20;
      switch (reenterDir) {
         // right = 0, left = 1, down = 2, up = 3
         case 0:
            hitbox.x += adjustDistance;
            break;
         case 1:
            hitbox.x -= adjustDistance;
            break;
         case 2:
            hitbox.y += adjustDistance;
            break;
         case 3:
            hitbox.y -= adjustDistance;
            break;
      }
   }

   public void flush() {
      this.collisionImg.flush();
      this.collisionImg = null;
   }
}
