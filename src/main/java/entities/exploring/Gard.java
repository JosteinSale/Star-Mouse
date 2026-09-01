package entities.exploring;

import static utils.Constants.Exploring.Cutscenes.GARD;

import entities.AnimationFrame;
import entities.Dimensions;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;

public class Gard extends BaseNpc {
   private CharacterAction action;
   private Direction direction;

   public Gard(Dimensions hitbox, Direction direction, boolean inForeground) {
      super(GARD, hitbox, 80, 30, inForeground);
      this.direction = direction;
      this.action = CharacterAction.STANDING;
      this.animation = new AnimationFrame(
            0, 0,
            8, 4);
   }

   @Override
   public void update() {
      updateAniTick();
   }

   private void updateAniTick() {
      if (action == CharacterAction.POSING) {
         return;
      }
      animation.update();
   }

   @Override
   public void adjustPos(float deltaX, float deltaY) {
      this.move(deltaX, deltaY);
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
         animation.reset();
      }
   }

   @Override
   public void setAction(CharacterAction action) {
      this.action = action;
      animation.setRow(getAnimationRow());
   }

   private int getAnimationRow() {
      switch (action) {
         case STANDING:
            int standingRow = switch (direction) {
               case RIGHT -> 0;
               case LEFT -> 1;
               default -> throw new IllegalArgumentException("Not implemented yet " + action.toString());
            };
            return standingRow;
         case WALKING:
            int walkingRow = switch (direction) {
               case RIGHT -> 2;
               case LEFT -> 3;
               default -> throw new IllegalArgumentException("Not implemented yet " + action.toString());
            };
            return walkingRow;
         default:
            throw new IllegalArgumentException("No animation row for action " + action.toString());
      }
   }
}
