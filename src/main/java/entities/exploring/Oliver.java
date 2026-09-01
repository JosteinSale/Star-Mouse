package entities.exploring;

import static utils.Constants.Exploring.Cutscenes.OLIVER;

import entities.AnimationFrame;
import entities.Dimensions;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;

public class Oliver extends BaseNpc {
   public CharacterAction action;
   public Direction direction;

   public Oliver(Dimensions hitbox, Direction direction, boolean inForeground) {
      super(OLIVER, hitbox, 80, 30, inForeground);
      this.direction = direction;
      this.action = CharacterAction.STANDING;
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
            throw new IllegalArgumentException("No animation row for action " + action.toString());
      }
   }

   @Override
   public void update() {
      if (action != CharacterAction.POSING) {
         animation.update();
      }
   }

   @Override
   public void adjustPos(float deltaX, float deltaY) {
      move(deltaX, deltaY);
      triggerBox.move(deltaX, deltaY);
   }

   @Override
   public void setDir(Direction dir) {
      this.direction = dir;
      animation.setRow(getAnimationRow());
   }

   @Override
   public void setPose(boolean poseActive, int colIndex, int rowIndex) {
      if (poseActive == true) {
         this.action = CharacterAction.POSING;
         animation.setRow(rowIndex);
         animation.setCol(colIndex);
      } else {
         this.action = CharacterAction.STANDING;
         this.animation.reset();
      }
   }

   @Override
   public void setAction(CharacterAction action) {
      this.action = action;
      this.animation.setRow(getAnimationRow());
   }

}
