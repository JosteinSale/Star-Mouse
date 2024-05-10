package entities.bossmode.shootPatterns;

import java.awt.Graphics;

/** A class representing a specific shootpattern.
 * It governs the creation of new projectiles in regards to their type,
 * speed and direction.
 */
public interface ShootPattern {

   /** First it ticks down the chargeTime (if any).
    * Then it ticks down the delay time (if any).
    * Then it calls shoot() at the specified shootInterval.
    * It updates the isCharging- and shootPhase-booleans accordingly.
    */
   public void update();

   /** Should be called by the update method when it's time to shoot.
    * Should be overrided with custom implementation.
    * The default implementation does nothing.
    */
   public void shoot();

   /** Call this method to reset the shootpattern.
    * The default implementation resets tick, isCharging- and shootPhase-boolean.
    */
   public void finishAttack();

   /** Returns true if the pattern is charging. 
    * Can be used to syncronize with boss animations.
    */
   public boolean isCharging();

   /** If there are any spefific animations associated with charging / shooting, 
    * they can be implemented here. 
    * You can utilize the the isCharging- and shootPhase-booleans
    * inherited from the supertype. The default implementation does nothing.
   */
   public void drawShootAnimations(Graphics g);
}
