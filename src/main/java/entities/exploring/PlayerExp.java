package entities.exploring;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import entities.AnimationFrame;
import entities.Entity;
import entities.MyCollisionImage;
import inputs.Inputs;
import main_classes.Game;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;
import utils.HelpMethods;

public class PlayerExp extends Entity {
   private MyCollisionImage collisionImg;
   private float playerSpeed = 5f;
   public boolean visible = true;
   public static int CURRENT_SPRITE_SHEET = 0;

   private AnimationFrame animation;
   public CharacterAction action;
   public Direction direction;

   public PlayerExp(Game game, Float hitbox, Direction initialDirection, Integer level, Integer area) {
      super(hitbox);
      action = CharacterAction.STANDING;
      direction = initialDirection;
      String imgName = "level" + level.toString() + "_area" + area.toString();
      this.collisionImg = game.getImages().getExpImageCollision(imgName + "_cl.png");
      this.animation = new AnimationFrame(
            getAnimationRow(), 0,
            8, 4);

   }

   private int getAnimationRow() {
      switch (action) {
         case STANDING:
            int standingRow = switch (direction) {
               case RIGHT -> 0;
               case LEFT -> 1;
               case DOWN -> 2;
               case UP -> 3;
            };
            return standingRow;
         case WALKING:
            int walkingRow = switch (direction) {
               case RIGHT -> 4;
               case LEFT -> 5;
               case DOWN -> 6;
               case UP -> 7;
            };
            return walkingRow;
         default:
            throw new IllegalArgumentException("Can't get animation row for action " + action.toString());
      }
   }

   public void keyPressed(KeyEvent e) {
   }

   public void KeyReleased(KeyEvent e) {
   }

   public void update(ArrayList<Rectangle2D.Float> npcHitboxes, boolean cutsceneActive, boolean fadeActive) {
      if (!cutsceneActive && !fadeActive) {
         handleKeyboardInputs(npcHitboxes);
         updateAnimationRow();
      }
      if (action != CharacterAction.POSING) {
         animation.update();
      }
   }

   private void handleKeyboardInputs(ArrayList<Rectangle2D.Float> npcHitboxes) {
      // Walking left
      if (Inputs.leftIsPressed) {
         action = CharacterAction.WALKING;
         direction = Direction.LEFT;
         hitbox.x -= playerSpeed;
         if (collidesWithMap(npcHitboxes)) {
            hitbox.x += playerSpeed;
         }
      }
      // Walking up
      if (Inputs.upIsPressed && !(Inputs.leftIsPressed && Inputs.rightIsPressed)) {
         action = CharacterAction.WALKING;
         direction = Direction.UP;
         hitbox.y -= playerSpeed;
         if (collidesWithMap(npcHitboxes)) {
            hitbox.y += playerSpeed;
         }
      }
      // Walking right
      if (Inputs.rightIsPressed) {
         action = CharacterAction.WALKING;
         direction = Direction.RIGHT;
         hitbox.x += playerSpeed;
         if (collidesWithMap(npcHitboxes)) {
            hitbox.x -= playerSpeed;
         }
      }
      // Walking down
      if (Inputs.downIsPressed && !(Inputs.leftIsPressed && Inputs.rightIsPressed)) {
         action = CharacterAction.WALKING;
         direction = Direction.DOWN;
         hitbox.y += playerSpeed;
         if (collidesWithMap(npcHitboxes)) {
            hitbox.y -= playerSpeed;
         }
      }
      // Standing still
      if (!Inputs.upIsPressed && !Inputs.downIsPressed
            && !Inputs.leftIsPressed && !Inputs.rightIsPressed) {
         action = CharacterAction.STANDING;
         return;
      }
      // Edge case: If opposite directions are pressed at the same time, the player
      // should stand still.
      if ((Inputs.upIsPressed && Inputs.downIsPressed)
            || (Inputs.leftIsPressed && Inputs.rightIsPressed)) {
         action = CharacterAction.STANDING;
      }
   }

   private boolean collidesWithMap(ArrayList<Rectangle2D.Float> npcHitboxes) {
      return HelpMethods.CollidesWithMap(hitbox, collisionImg)
            || HelpMethods.CollidesWithNpc(hitbox, npcHitboxes);
   }

   public void resetAll() {
      Inputs.downIsPressed = false;
      Inputs.rightIsPressed = false;
      Inputs.leftIsPressed = false;
      Inputs.upIsPressed = false;
      this.action = CharacterAction.STANDING;
      updateAnimationRow();
   }

   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   public void setDirection(Direction dir) {
      this.direction = dir;
   }

   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   public void setCurrentSpriteSheet(int sheetIndex) {
      PlayerExp.CURRENT_SPRITE_SHEET = sheetIndex;
   }

   public void adjustPos(float deltaX, float deltaY) {
      hitbox.x += deltaX;
      hitbox.y += deltaY;
   }

   public void setAction(CharacterAction action) {
      this.action = action;
   }

   public void setPose(boolean pose, int colIndex, int rowIndex) {
      if (pose == true) {
         this.action = CharacterAction.POSING;
         animation.setCol(colIndex);
         animation.setRow(rowIndex);
      } else {
         this.action = CharacterAction.STANDING;
         this.animation.reset();
      }
   }

   /** Moves the player 20 px in the reenter-direction specified for this portal */
   public void adjustReenterPos(Direction reenterDir) {
      int adjustDistance = 20;
      switch (reenterDir) {
         case RIGHT:
            hitbox.x += adjustDistance;
            break;
         case LEFT:
            hitbox.x -= adjustDistance;
            break;
         case DOWN:
            hitbox.y += adjustDistance;
            break;
         case UP:
            hitbox.y -= adjustDistance;
            break;
      }
   }

   public AnimationFrame getAnimation() {
      return animation;
   }

   public void updateAnimationRow() {
      animation.setRow(getAnimationRow());
   }
}
