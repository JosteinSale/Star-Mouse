package entities.bossmode;

import java.util.ArrayList;

import projectiles.shootPatterns.ShootPattern;

/**
 * A class which keeps track of which actions a boss can have.
 * Register new actions with the registerAction()-method.
 * The actions will then be added in that order.
 * Each action will have:
 * - A name
 * - A duration
 * - A list of bossParts (can be empty)
 * - A list of shootPatterns (can be empty)
 * 
 * Use the getName() and getDuration() to access info about the action
 * Call startAction(), updateAction() and finishAction() to coordinate the
 * behavior.
 * 
 */
public class BossActionHandler {
   // Each property of the attack will be linked to one common index for all lists.
   ArrayList<Integer> actionNames;
   ArrayList<Integer> durations;
   ArrayList<ArrayList<DefaultBossPart>> bossParts;
   ArrayList<ArrayList<ShootPattern>> shootPatterns;

   public BossActionHandler() {
      this.actionNames = new ArrayList<>();
      this.durations = new ArrayList<>();
      this.bossParts = new ArrayList<>();
      this.shootPatterns = new ArrayList<>();
   }

   /** Use this constructor if the action has a globally controlled duration */
   public void registerAction(int name, int duration, ArrayList<DefaultBossPart> bossParts,
         ArrayList<ShootPattern> shootPatterns) {
      this.actionNames.add(name);
      this.durations.add(duration);
      this.bossParts.add(bossParts);
      this.shootPatterns.add(shootPatterns);
   }

   /**
    * Use this constructor if the action doesn't have a globally controled duration
    */
   public void registerAction(
         int name, ArrayList<DefaultBossPart> actionParts,
         ArrayList<ShootPattern> shootPatterns) {
      this.actionNames.add(name);
      this.durations.add(0);
      this.bossParts.add(actionParts);
      this.shootPatterns.add(shootPatterns);
   }

   /**
    * Loops through the bossParts and shootPatters for the specific action,
    * and finishes them
    */
   public void finishAction(int index) {
      for (IBossPart part : bossParts.get(index)) {
         part.finishAttack();
      }
      for (ShootPattern pattern : shootPatterns.get(index)) {
         pattern.finishAttack();
      }
   }

   /**
    * Loops through the bossParts for the specific action
    * and starts them
    */
   public void startAction(int index) {
      for (IBossPart part : bossParts.get(index)) {
         part.startAttack();
      }
   }

   /**
    * Loops through the bossParts and shootPatters for the specific action,
    * and updates them
    */
   public void updateAction(int index) {
      for (IBossPart part : bossParts.get(index)) {
         part.updateBehavior();
      }
      for (ShootPattern pattern : shootPatterns.get(index)) {
         pattern.update();
      }
   }

   public int getName(int actionIndex) {
      if (actionIndex > (actionNames.size() - 1)) {
         throw new IllegalArgumentException("No name available for index : " + actionIndex);
      }
      return this.actionNames.get(actionIndex);
   }

   public int getDuration(int actionIndex) {
      if (durations.get(actionIndex) == 0) {
         throw new IllegalArgumentException("This action shouldn't have a duration : " + actionIndex);
      }
      return this.durations.get(actionIndex);
   }

   public int amountOfActions() {
      return this.actionNames.size();
   }

   /**
    * Checks all bossParts pertaining to the currentAction, and checks
    * if any of them is currently in a charging fase.
    */
   public boolean isActionCharging(int index) {
      for (IBossPart part : bossParts.get(index)) {
         if (part.isCharging()) {
            return true;
         }
      }
      for (ShootPattern pattern : shootPatterns.get(index)) {
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
   public boolean isActionCoolingDown(int index) {
      for (IBossPart part : bossParts.get(index)) {
         if (part.isCoolingDown()) {
            return true;
         }
      }
      return false;
   }

   public boolean shouldAbort(int index) {
      for (IBossPart part : bossParts.get(index)) {
         if (part.shouldAbort()) {
            return true;
         }
      }
      return false;
   }

   public boolean hasDuration(int index) {
      return (durations.get(index) != 0);
   }

   public ArrayList<ArrayList<DefaultBossPart>> getAllBossParts() {
      return this.bossParts;
   }

   public ArrayList<ArrayList<ShootPattern>> getAllShootPatterns() {
      return this.shootPatterns;
   }
}
