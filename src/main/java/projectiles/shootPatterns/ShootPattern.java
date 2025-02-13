package projectiles.shootPatterns;

import java.util.ArrayList;

import entities.bossmode.AnimatedComponent;

/**
 * A class representing a specific shootpattern.
 * It governs the creation of new projectiles in regards to their type,
 * speed and direction.
 */
public interface ShootPattern {

   /**
    * First it ticks down the chargeTime (if any).
    * Then it ticks down the delay time (if any).
    * Then it calls shoot() at the specified shootInterval.
    * It updates the isCharging- and shootPhase-booleans accordingly.
    */
   public void update();

   /**
    * Should be called by the update method when it's time to shoot.
    * Should be overrided with custom implementation.
    * The default implementation does nothing.
    */
   public void shoot();

   /**
    * Call this method to reset the shootpattern.
    * The default implementation resets tick, isCharging- and shootPhase-boolean.
    */
   public void finishAttack();

   /**
    * Returns true if the pattern is charging.
    */
   public boolean isCharging();

   /**
    * Returns true if the pattern is in it's shoot phase.
    */
   public boolean isInShootPhase();

   /**
    * Returns the chargeAnimation.
    * (Should we allow it be null?)
    */
   public AnimatedComponent getChargeAnimation();

   /**
    * Return the shootAnimation.
    * (Should we allow it be null?)
    */
   public AnimatedComponent getShootAnimation();
}
