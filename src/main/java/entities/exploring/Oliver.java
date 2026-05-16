package entities.exploring;

import static utils.Constants.Exploring.Cutscenes.OLIVER;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.AnimationFrame;
import entities.Entity;
import utils.Constants.Direction;
import utils.Constants.Exploring.CharacterAction;

public class Oliver extends Entity implements NPC {
   private String name = OLIVER;
   private Rectangle2D.Float triggerBox;
   private int xDrawOffset = 80;
   private int yDrawOffset = 30;
   private int startCutscene = 0;
   private boolean inForeground;

   private AnimationFrame animation;
   public CharacterAction action;
   public Direction direction;

   public Oliver(Float hitbox, Direction direction, boolean inForeground) {
      super(hitbox);
      this.direction = direction;
      this.action = CharacterAction.STANDING;
      this.animation = new AnimationFrame(
            getAnimationRow(), 0,
            8, 4);
      this.inForeground = inForeground;
      makeTriggerBox();
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

   private void makeTriggerBox() {
      this.triggerBox = new Rectangle2D.Float(
            hitbox.x - 8, hitbox.y, hitbox.width + 16, hitbox.height + 8);
   }

   @Override
   public void update() {
      if (action != CharacterAction.POSING) {
         animation.update();
      }
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
   public boolean inForeground() {
      return this.inForeground;
   }

   public void setAction(CharacterAction action) {
      this.action = action;
      this.animation.setRow(getAnimationRow());
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
