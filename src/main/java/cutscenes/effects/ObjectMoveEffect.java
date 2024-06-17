package cutscenes.effects;

import java.awt.Graphics;
import java.util.ArrayList;

import cutscenes.SimpleAnimationFactory;
import game_events.AddObjectEvent;
import game_events.GeneralEvent;
import game_events.ObjectMoveEvent;
import gamestates.Gamestate;
import main_classes.Game;

/** Keeps a list of SimpleAnimation-objects. These can be added with the 
 * activate-method. Once added they will be drawn and updated. 
 * They can be moved with the moveObject-method.
 * The effect can only be inactivated with the reset-method.
 * 
 * OBS: when used in combination with an advancable effect,
 * make sure that movements get time to finish. Or else they will be interrupted.
 */
public class ObjectMoveEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   private SimpleAnimationFactory animationFactory;
   private ArrayList<SimpleAnimation> objects;
   private ArrayList<Boolean> moveStatuses;
   private ArrayList<Integer> moveTicks;
   private ArrayList<Integer> moveDurations;
   private ArrayList<Float> xSpeeds;
   private ArrayList<Float> ySpeeds;
   

   public ObjectMoveEffect() {
      this.animationFactory = new SimpleAnimationFactory();
      this.objects = new ArrayList<>();
      this.moveStatuses = new ArrayList<>();
      this.moveTicks = new ArrayList<>();
      this.moveDurations = new ArrayList<>();
      this.xSpeeds = new ArrayList<>();
      this.ySpeeds = new ArrayList<>();
      // TODO - Later we might also include the AnimatedComponentFactory.
   }

   /* Can be used to add new objects. 
   Once an object is added, the effect is set to active.
   If the initial position is outside of the screen, 
   it throws an IllegalArgumentException */ 
   @Override
   public void activate(GeneralEvent evt) {
      this.active = true;
      AddObjectEvent addEvt = (AddObjectEvent) evt;
      SimpleAnimation animation = animationFactory.getAnimation(
         addEvt.objectName(), addEvt.xPos(), addEvt.yPos());
      if (isOutOfScreen(animation)) {
         throw new IllegalArgumentException("Start position cannot be outside the screen");
      }
      this.addNewEmptyEntry(animation);
   }

   private void addNewEmptyEntry(SimpleAnimation animation) {
      this.objects.add(animation);
      this.moveStatuses.add(false);
      this.moveTicks.add(0);
      this.moveDurations.add(0);
      this.xSpeeds.add(0f);
      this.ySpeeds.add(0f);
   }

   /** Can be called to set target position for an object. The update-method will
    * move the object. */
    public void moveObject(ObjectMoveEvent evt) {
      int i = evt.objectIndex();
      this.moveStatuses.set(i, true);
      this.moveDurations.set(i, evt.duration());
      float xSpeed = (evt.targetX() - objects.get(i).xPos) / evt.duration();
      float ySpeed = (evt.targetY() - objects.get(i).yPos) / evt.duration();
      this.xSpeeds.set(i, xSpeed);
      this.ySpeeds.set(i, ySpeed);
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new AddObjectEvent(null, 0, 0);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true;  // Supports all gamestates
   }

   // For each object: 
   //    1. It updates animations
   //    2. If it should move, it does so. 
   @Override
   public void update() {
      for (int i = 0; i < objects.size(); i++) {
         objects.get(i).updateAnimation();
         if (shouldObjectMove(i)) {
            this.updatePositionOf(i);
         }
      }
   }

   private void updatePositionOf(int i) {
      SimpleAnimation object = objects.get(i);
      object.xPos += xSpeeds.get(i);
      object.yPos += ySpeeds.get(i);
      int updatedTick = moveTicks.get(i) + 1;
      moveTicks.set(i, updatedTick);
      if (updatedTick > moveDurations.get(i)) {
         moveTicks.set(i, 0);
         moveStatuses.set(i, false);
      }
   }

   private boolean shouldObjectMove(int i) {
      return this.moveStatuses.get(i);
   }

   private boolean isOutOfScreen(SimpleAnimation object) {
      // 1. Checks x-position
      if (((object.xPos + object.width) < 0) || (object.xPos > Game.GAME_WIDTH)) { 
         return true;
      }
      // 2. Checks y-position
      if (((object.yPos + object.height) < 0) || (object.yPos > Game.GAME_HEIGHT)) { 
         return true;
      }
      return false;
   }

   @Override
   public void draw(Graphics g) {
      for (SimpleAnimation object : objects) {
         object.draw(g);
      }
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void reset() {
      this.active = false;
      this.objects.clear();
      this.moveDurations.clear();
      this.moveTicks.clear();
      this.moveStatuses.clear();
      this.xSpeeds.clear();
      this.ySpeeds.clear();
   }
   
}
