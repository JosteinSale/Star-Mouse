package cutscenes.effects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import cutscenes.SimpleAnimationFactory;
import game_events.AddObjectEvent;
import game_events.GeneralEvent;
import game_events.ObjectMoveEvent;
import gamestates.Gamestate;

/**
 * Keeps a list of SimpleAnimation-objects. These can be added with the
 * activate-method. Once added they will be drawn and updated.
 * They can be moved with the moveObject-method.
 * The effect can only be inactivated with the reset-method.
 * 
 * Objects will move independently of any other cutscene effects.
 * NOTE: objects added first will be drawn at the bottom layer.
 */
public class ObjectMoveEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   private SimpleAnimationFactory animationFactory;
   private ArrayList<SimpleAnimation> objects;  // Need arraylist because of sorting, to ensure correct layering
   private HashMap<String, Integer> nameToIndexMap; // Needed to associate the identifier with the correct index
   private HashMap<String, Boolean> moveStatuses;
   private HashMap<String, Integer> moveTicks;
   private HashMap<String, Integer> moveDurations;
   private HashMap<String, Float> xSpeeds;
   private HashMap<String, Float> ySpeeds;

   public ObjectMoveEffect() {
      this.animationFactory = new SimpleAnimationFactory();
      this.objects = new ArrayList<>();
      this.nameToIndexMap = new HashMap<>();
      this.moveStatuses = new HashMap<>();
      this.moveTicks = new HashMap<>();
      this.moveDurations = new HashMap<>();
      this.xSpeeds = new HashMap<>();
      this.ySpeeds = new HashMap<>();
      // TODO - Later we might also include the AnimatedComponentFactory.
      // TODO - Then, include methods setAnimation(String name, int row) which throws exception if not AnimatedComponent.
      //  and setPose(int row, int col, boolean active).
   }

   /*
    * Can be used to add new objects.
    * Once an object is added, the effect is set to active.
    * If the initial position is outside of the screen,
    * it throws an IllegalArgumentException
    */
   @Override
   public void activate(GeneralEvent evt) {
      this.active = true;
      AddObjectEvent addEvt = (AddObjectEvent) evt;
      if (this.nameToIndexMap.containsKey(addEvt.identifier())) {
         throw new IllegalArgumentException("Identifier already registered: " + addEvt.identifier());
      }
      SimpleAnimation animation = animationFactory.getAnimation(
            addEvt.objectName(), addEvt.xPos(), addEvt.yPos(), addEvt.scaleW(), addEvt.scaleH(), addEvt.aniSpeed());
      this.addNewEmptyEntry(addEvt.identifier(), animation);
   }

   private void addNewEmptyEntry(String identifier, SimpleAnimation animation) {
      this.nameToIndexMap.put(identifier, objects.size());
      this.objects.add(animation);
      this.moveStatuses.put(identifier, false);
      this.moveTicks.put(identifier, 0);
      this.moveDurations.put(identifier, 0);
      this.xSpeeds.put(identifier, 0f);
      this.ySpeeds.put(identifier, 0f);
   }

   /**
    * Can be called to set target position for an object. The update-method will
    * move the object. If the identifier name is not registered, it throws an
    * exception.
    */
   public void moveObject(ObjectMoveEvent evt) {
      String id = evt.identifier();
      if (!this.nameToIndexMap.containsKey(id)) {
         throw new IllegalArgumentException("Cannot find identifier: " + id);
      }
      int index = nameToIndexMap.get(id);
      this.moveStatuses.put(id, true);
      this.moveDurations.put(id, evt.duration());
      float xSpeed = (evt.targetX() - objects.get(index).xPos) / evt.duration();
      float ySpeed = (evt.targetY() - objects.get(index).yPos) / evt.duration();
      this.xSpeeds.put(id, xSpeed);
      this.ySpeeds.put(id, ySpeed);
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new AddObjectEvent(null, null, 0, 0, 0f, 0f, 0);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // Supports all gamestates
   }

   // For each object:
   // 1. It updates animations
   // 2. If it should move, it does so.
   @Override
   public void update() {
      for (String id : nameToIndexMap.keySet()) {
         int index = nameToIndexMap.get(id);
         objects.get(index).updateAnimation();
         if (shouldObjectMove(id)) {
            this.updatePositionOf(id);
         }
      }
   }

   private void updatePositionOf(String id) {
      SimpleAnimation object = objects.get(nameToIndexMap.get(id));
      object.xPos += xSpeeds.get(id);
      object.yPos += ySpeeds.get(id);
      int updatedTick = moveTicks.get(id) + 1;
      moveTicks.put(id, updatedTick);
      if (updatedTick > moveDurations.get(id)) {
         moveTicks.put(id, 0);
         moveStatuses.put(id, false);
      }
   }

   private boolean shouldObjectMove(String id) {
      return this.moveStatuses.get(id);
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
      this.nameToIndexMap.clear();
      this.moveDurations.clear();
      this.moveTicks.clear();
      this.moveStatuses.clear();
      this.xSpeeds.clear();
      this.ySpeeds.clear();
   }

}
