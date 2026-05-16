package entities.exploring;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import static utils.Constants.Exploring.Cutscenes.GARD;

import entities.AnimationFrame;
import entities.Entity;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;

public class Gard extends Entity implements NPC {
   private String name = GARD;
   private Rectangle2D.Float triggerBox;
   private int xDrawOffset = 80;
   private int yDrawOffset = 30;
   private int startCutscene = 0;
   private boolean inForeground;

   private CharacterAction action;
   private Direction direction;
   private AnimationFrame animation;

   public Gard(Float hitbox, Direction direction, boolean inForeground) {
      super(hitbox);
      this.direction = direction;
      this.inForeground = inForeground;
      makeTriggerBox();
      this.animation = new AnimationFrame(
            0, 0,
            8, 4);
   }

   private void makeTriggerBox() {
      this.triggerBox = new Rectangle2D.Float(
            hitbox.x - 8, hitbox.y, hitbox.width + 16, hitbox.height + 8);
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

   public void adjustPos(float deltaX, float deltaY) {
      this.hitbox.x += deltaX;
      this.hitbox.y += deltaY;
      this.triggerBox.x += deltaX;
      this.triggerBox.y += deltaY;
   }

   @Override
   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }

   @Override
   public Rectangle2D.Float getTriggerBox() {
      return this.triggerBox;
   }

   public int getStartCutscene() {
      return this.startCutscene;
   }

   @Override
   public void setStartCutscene(int startCutscene) {
      this.startCutscene = startCutscene;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public void setDir(Direction dir) {
      this.direction = dir;
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
   public boolean inForeground() {
      return this.inForeground;
   }

   public void setAction(CharacterAction action) {
      this.action = action;
      animation.setRow(getAnimationRow());
   }

   private int getAnimationRow() {
      switch (action) {
         case STANDING:
            int standingRow = switch (direction) {
               case LEFT -> 0;
               case RIGHT -> 1;
               default -> throw new IllegalArgumentException("Not implemented yet " + action.toString());
            };
            return standingRow;
         case WALKING:
            int walkingRow = switch (direction) {
               case LEFT -> 2;
               case RIGHT -> 3;
               default -> throw new IllegalArgumentException("Not implemented yet " + action.toString());
            };
            return walkingRow;
         default:
            throw new IllegalArgumentException("No animation row for action " + action.toString());
      }
   }

   @Override
   public float getXDrawOffset() {
      return this.xDrawOffset;
   }

   @Override
   public float getYDrawOffset() {
      return this.yDrawOffset;
   }

   @Override
   public AnimationFrame getAnimation() {
      return this.animation;
   }

}
