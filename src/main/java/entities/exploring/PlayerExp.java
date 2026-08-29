package entities.exploring;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import entities.*;
import inputs.Inputs;
import main_classes.Game;
import utils.HelpMethods;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;
import static entities.CollisionPixels.CollisionAt;

public class PlayerExp extends MyRectangle {
   private final MyCollisionImage collisionImg;
   private final CollisionPixels collisionPixels;
   private final float playerSpeed = 5f;
   private final Vector2 speedVector;
   public boolean visible = true;
   public static int CURRENT_SPRITE_SHEET = 0;

   private final AnimationFrame animation;
   public CharacterAction action;
   public Direction direction;

   public PlayerExp(Game game, Float hitbox, Direction initialDirection, Integer level, Integer area) {
      super(hitbox);
      collisionPixels = new CollisionPixels(this, CollisionAt.ALL_FOUR_CORNERS);
      action = CharacterAction.STANDING;
      direction = initialDirection;
      speedVector = new Vector2(0, 0);
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
         speedVector.set(0, 0);
         handleKeyboardInputs();
         movePlayer(npcHitboxes);
         collisionPixels.update();
      }
      if (action != CharacterAction.POSING) {
         updateAnimationRow();
         animation.update();
      }
   }

   private void handleKeyboardInputs() {
      // 1. Determine the character's action
      int amountOfDirectionsPressed = (Inputs.upIsPressed ? 1 : 0)
            + (Inputs.downIsPressed ? 1 : 0)
            + (Inputs.leftIsPressed ? 1 : 0)
            + (Inputs.rightIsPressed ? 1 : 0);

      if (amountOfDirectionsPressed == 0 || amountOfDirectionsPressed >= 3) {
         action = CharacterAction.STANDING;
         return;
      } else if ((Inputs.upIsPressed && Inputs.downIsPressed) ||
            (Inputs.leftIsPressed && Inputs.rightIsPressed)) {
         action = CharacterAction.STANDING;
         return;
      } else {
         action = CharacterAction.WALKING;
      }

      // 2. Determining the player's intended speed vector and animation direction
      if (Inputs.leftIsPressed) {
         direction = Direction.LEFT;
         speedVector.x -= playerSpeed;
      }
      if (Inputs.upIsPressed) {
         direction = Direction.UP;
         speedVector.y -= playerSpeed;
      }
      if (Inputs.rightIsPressed) {
         direction = Direction.RIGHT;
         speedVector.x += playerSpeed;
      }
      if (Inputs.downIsPressed) {
         direction = Direction.DOWN;
         speedVector.y += playerSpeed;
      }
   }

   /**
    * Move the player in the requested direction,
    * and if it crashes with the map / NPCs,
    * try to slide the player into an available position.
    */
   private void movePlayer(ArrayList<Rectangle2D.Float> npcHitboxes) {
      if (speedVector.x == 0 && speedVector.y == 0) {
         return;
      }

      float currentXPos = x();
      float currentYPos = y();
      // 1. Try the requested move first
      if (tryMove(currentXPos + speedVector.x, currentYPos + speedVector.y, npcHitboxes)) {
         return;
      }

      // 2. Requested move blocked — try reasonable alternatives
      // depending on movement type
      if (speedVector.x == 0) { // Vertical movement: try sliding right / left
         if (tryMove(currentXPos + playerSpeed, currentYPos + speedVector.y, npcHitboxes)) {
            return;
         }
         if (tryMove(currentXPos - playerSpeed, currentYPos + speedVector.y, npcHitboxes)) {
            return;
         }
      } else if (speedVector.y == 0) { // Horizontal movement: try sliding up / down
         if (tryMove(currentXPos + speedVector.x, currentYPos + playerSpeed, npcHitboxes)) {
            return;
         }
         if (tryMove(currentXPos + speedVector.x, currentYPos - playerSpeed, npcHitboxes)) {
            return;
         }
      } else { // Diagonal movement: try each axis separately
         if (tryMove(currentXPos + speedVector.x, currentYPos, npcHitboxes)) {
            return;
         }
         if (tryMove(currentXPos, currentYPos + speedVector.y, npcHitboxes)) {
            return;
         }
      }

      // 3. No available placements -> move back to startPosition
      setPosition(currentXPos, currentYPos);
   }

   /**
    * Moves the hitbox into the given position,
    * and returns true if it is available, false otherwise
    */
   private boolean tryMove(float x, float y, ArrayList<Rectangle2D.Float> npcHitboxes) {
      setPosition(x, y);
      collisionPixels.update();
      return (!HelpMethods.CollidesWithMap(collisionPixels, collisionImg, 0, 0) &&
              !HelpMethods.CollidesWithNpc(getHitboxAsFloat(), npcHitboxes));
   }

   public void resetAll() {
      Inputs.downIsPressed = false;
      Inputs.rightIsPressed = false;
      Inputs.leftIsPressed = false;
      Inputs.upIsPressed = false;
      speedVector.set(0, 0);
      this.action = CharacterAction.STANDING;
      updateAnimationRow();
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
      move(deltaX, deltaY);
   }

   public void setAction(CharacterAction action) {
      this.action = action;
   }

   public void setPose(boolean posing, int colIndex, int rowIndex) {
      if (posing) {
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
      int distance = 20;
      switch (reenterDir) {
         case RIGHT:
            move(distance, 0);
            break;
         case LEFT:
            move(-distance, 0);
            break;
         case DOWN:
            move(0, distance);
            break;
         case UP:
            move(0, -distance);
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
