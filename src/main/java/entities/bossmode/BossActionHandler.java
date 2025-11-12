package entities.bossmode;

import java.util.ArrayList;
import java.util.HashMap;

import projectiles.shootPatterns.ShootPattern;

/**
 * A class which keeps track of which actions a boss can have.
 * Register new actions with the registerAction()-method.
 * The actions will then be added in that order.
 * Each action will have:
 * - A name
 * - An order in the sequence of actions
 * - A duration
 * - A list of bossParts (can be empty)
 * - A list of shootPatterns (can be empty)
 * 
 * The BossActionHandler will loop through the actions in the given order,
 * and update the bossParts and shootPatterns accordingly.
 */
public class BossActionHandler {
   private int tick = 0; // Global timer
   private String currentAction;
   private ArrayList<String> orderOfActions;

   // Maps between the action name and their data
   HashMap<String, Integer> durations;
   HashMap<String, ArrayList<DefaultBossPart>> bossParts;
   HashMap<String, ArrayList<ShootPattern>> shootPatterns;

   public BossActionHandler() {
      this.orderOfActions = new ArrayList<>();
      this.durations = new HashMap<>();
      this.bossParts = new HashMap<>();
      this.shootPatterns = new HashMap<>();
   }

   /** Use this constructor if the action has a globally controlled duration */
   public void registerAction(String name, int duration, ArrayList<DefaultBossPart> bossParts,
         ArrayList<ShootPattern> shootPatterns) {
      if (durations.containsKey(name)) {
         throw new IllegalArgumentException("Action with name " + name + " is already registered.");
      }
      this.orderOfActions.add(name);
      this.durations.put(name, duration);
      this.bossParts.put(name, bossParts);
      this.shootPatterns.put(name, shootPatterns);
   }

   /**
    * Use this constructor if the action doesn't have a globally controled duration
    */
   public void registerAction(
         String name, ArrayList<DefaultBossPart> bossParts,
         ArrayList<ShootPattern> shootPatterns) {
      if (durations.containsKey(name)) {
         throw new IllegalArgumentException("Action with name " + name + " is already registered.");
      }
      this.orderOfActions.add(name);
      this.durations.put(name, 0);
      this.bossParts.put(name, bossParts);
      this.shootPatterns.put(name, shootPatterns);
   }

   /**
    * Loops through the bossParts and shootPatters for the specific action,
    * and finishes them
    */
   public void finishAction(String name) {
      for (IBossPart part : bossParts.get(name)) {
         part.finishAttack();
      }
      for (ShootPattern pattern : shootPatterns.get(name)) {
         pattern.finishAttack();
      }
   }

   /**
    * Loops through the bossParts for the specific action
    * and starts them
    */
   public void startAction(String name) {
      currentAction = name;
      for (IBossPart part : bossParts.get(name)) {
         part.startAttack();
      }
   }

   public void update() {
      checkIfAbortAction();
      updateGlobalCycle();
      updateAction(currentAction);
   }

   private void updateGlobalCycle() {
      if (!currentActionHasDuration()) {
         return;
      }
      tick++;
      if (tick >= getDuration(currentAction)) {
         tick = 0;
         goToNextAction();
      }
   }

   // An active action can choose to abort its attack for whatever reason.
   // In such case, we go to the next action.
   private void checkIfAbortAction() {
      if (shouldAbort()) {
         goToNextAction();
      }
   }

   private void goToNextAction() {
      finishAction(currentAction);
      currentAction = orderOfActions.get(getNextInSequence());
      startAction(currentAction);
   }

   private int getNextInSequence() {
      int currentIndex = orderOfActions.indexOf(currentAction);
      return (currentIndex + 1) % amountOfActions();
   }

   /**
    * Loops through the bossParts and shootPatters for the specific action,
    * and updates them
    */
   private void updateAction(String name) {
      for (IBossPart part : bossParts.get(name)) {
         part.updateBehavior();
      }
      for (ShootPattern pattern : shootPatterns.get(name)) {
         pattern.update();
      }
   }

   private int getDuration(String name) {
      if (durations.get(name) == 0) {
         throw new IllegalArgumentException("This action shouldn't have a duration : " + name);
      }
      return this.durations.get(name);
   }

   public int amountOfActions() {
      return orderOfActions.size();
   }

   /**
    * Checks all bossParts pertaining to the currentAction, and checks
    * if any of them is currently in a charging fase.
    */
   public boolean isActionCharging() {
      for (IBossPart part : bossParts.get(currentAction)) {
         if (part.isCharging()) {
            return true;
         }
      }
      for (ShootPattern pattern : shootPatterns.get(currentAction)) {
         if (pattern.isCharging()) {
            return true;
         }
      }
      return false;
   }

   /**
    * Checks all bossParts pertaining to the currentAction, and checks
    * if any of them is currently in a cooldown fase.
    */
   public boolean isActionCoolingDown() {
      for (IBossPart part : bossParts.get(currentAction)) {
         if (part.isCoolingDown()) {
            return true;
         }
      }
      return false;
   }

   public boolean shouldAbort() {
      for (IBossPart part : bossParts.get(currentAction)) {
         if (part.shouldAbort()) {
            return true;
         }
      }
      return false;
   }

   public boolean currentActionHasDuration() {
      return (durations.get(currentAction) != 0);
   }

   public HashMap<String, ArrayList<DefaultBossPart>> getAllBossParts() {
      return bossParts;
   }

   public HashMap<String, ArrayList<ShootPattern>> getAllShootPatterns() {
      return shootPatterns;
   }

   public String getNameOfCurrentAction() {
      return currentAction;
   }

   public void reset() {
      tick = 0;
      finishAction(currentAction);
      currentAction = orderOfActions.get(0);
   }
}
