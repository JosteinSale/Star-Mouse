package projectiles.shootPatterns;

import java.awt.Point;

import projectiles.ProjectileHandler2;

/**
 * A class representing a specific shootpattern.
 * It governs the creation of new projectiles in regards to their type,
 * speed and direction.
 * 
 * If the pattern requires specific calculations
 * in the update-mehotd, then override the update-method. Remember to call
 * the updateTicks()-method here.
 * 
 * The updateTicks()-method calls the shoot()-method at intervals specified
 * by the shootInterval (int).
 * 
 * The shoot()-should always be overridden with custom behavior.
 */
abstract public class DefaultShootPattern implements ShootPattern {
   protected Point gunPoint;
   protected ProjectileHandler2 projectileHandler;
   protected boolean isCharging = true;
   protected boolean shootPhase = false;
   private int tick;
   private int chargeTime;
   private int startDelay; // If there should be a delay after charging has finished.
   private int shootInterval;

   public DefaultShootPattern(ProjectileHandler2 projectileHandler, Point gunPoint, int chargeTime, int startDelay,
         int shootInterval) {
      this.projectileHandler = projectileHandler;
      this.gunPoint = gunPoint;
      this.chargeTime = chargeTime;
      this.startDelay = startDelay;
      this.shootInterval = shootInterval;
   }

   @Override
   public void update() {
      updateTicks();
   }

   /**
    * The shootPattern always starts with a charging phase
    * (isCharging = true, shootPhase = false),
    * then a potential delay to allow syncronizing with other shootPatterns,
    * and then a shootPhase. This method coordinates this by increasing a tick,
    * and then comparing it to the shootInterval, chargeTime and startDelay.
    */
   protected void updateTicks() {
      tick++;
      if (shootPhase) {
         if (tick > shootInterval) {
            tick = 0;
            this.shoot();
         }
      } else if (isCharging) {
         isCharging = !(tick > chargeTime);
      }
      // Delay
      else if (tick > (chargeTime + startDelay)) {
         shootPhase = true;
         tick = 0;
         this.shoot();
      }
   }

   @Override
   public void shoot() {
      /* Override this method with custom behavior */
   }

   @Override
   public boolean isCharging() {
      return this.isCharging;
   }

   @Override
   public void finishAttack() {
      this.tick = 0;
      this.isCharging = true;
      this.shootPhase = false;

   }

}
