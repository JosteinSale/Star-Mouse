package entities.bossmode;

import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * An interface that represents a default BossPart. It has methods for updating
 * the
 * x- y- and rotation, checking collision with Rectangle2D's, and drawing.
 * Specific implementations of bossParts can extend this object, and should
 * override the following methods:
 * - updateBehavior(),
 * - updateAnimations(),
 * - onPlayerCollision(),
 * - onTeleportHit(),
 * - isCharging(),
 * - shouldAbort(),
 * - startAttack(),
 * - finishAttack()
 */
public interface IBossPart {

   /**
    * Moves the bossPart x- and y-position, as well as its rotation
    * around the hitbox center. Collision-rectangles are updated.
    */
   public void updatePosition(int deltaX, int deltaY, Double deltaR);

   /**
    * Sets new positions for the bossPart centerX and centerY,
    * as well as its rotation around the hitbox center.
    * Collision-rectangles are updated.
    */
   public void setPosition(int centerX, int centerY, Double rotation);

   /** Returns true if the point is inside the bossPart */
   public boolean containsPoint(Point p);

   /** Returns true if the rectangle intersects the bossPart */
   public boolean intersectsRect(Rectangle2D.Float hb);

   /** OBS: Only use to get hitbox-dimensions, or centerX / centerY */
   public Rectangle2D.Float getNonRotatedHitbox();

   /**
    * If the bossPart can't currently be collided with, it should be set to false.
    * (The player needs access to the canCollide-status of the bossPart).
    * 
    * @param active
    */
   public void setCollisionActive(boolean active);

   /** If set to false, the bossPart is not drawn */
   public void setVisible(boolean visible);

   /**
    * A method invoked by the player.
    * Returns true if the player can collide with the bossPart
    */
   public boolean canCollide();

   /**
    * A method invoked by the projectileHandler.
    * Returns true if the projectile can collide with the bossPart
    */
   public boolean stopsProjectiles();

   /**
    * Returns true if the bossPart is currently in a charging fase.
    * The bossPart doesn't necessarily need a charging fase, in such case
    * the default implementation can be kept, which always returns false.
    * 
    * @return isCharging
    */
   public boolean isCharging();

   /**
    * Returns true if the bossPart is currently in a coolDown fase.
    * The bossPart doesn't necessarily need a coolDown fase, in such case
    * the default implementation can be kept, which always returns false.
    * 
    * @return isCoolingDown
    */
   public boolean isCoolingDown();

   /**
    * Returns true if the bossPart wants to abort the attack for whatever reason.
    * If so, the bossActionHandler immediately proceeds to the next action.
    * The default implementation always returns false.
    * 
    * @return shouldAbort
    */
   public boolean shouldAbort();

   /**
    * A method invoked by the player if a collision is detected.
    * It should implement custom behavior for the bossPart upon player collision.
    * This method is made to be overwritten, the default implementation does
    * nothing.
    */
   public void onPlayerCollision();

   /**
    * A method invoked by the player if a teleport hit is detected.
    * It should implement custom behavior for the bossPart upon being hit
    * with teleportation.
    * This method is made to be overwritten, the default implementation does
    * nothing.
    */
   public void onTeleportHit();

   /**
    * A method invoked by the projectileHandler if a projectile hit is detected.
    * It should implement custom behavior for the bossPart upon being hit
    * with a projectile.
    * This method is made to be overwritten, the default implementation does
    * nothing.
    */
   public void onProjectileHit();

   /**
    * A method invoked by the Boss at 60 FPS, if the attack is active.
    * This method is made to be overwritten, the default implementation does
    * nothing.
    */
   public void updateBehavior();

   /**
    * A method invoked by the BossPart update-method, if the attack is active.
    * The default implementation updates the animation component. This method
    * can be overwritten with custom behavior.
    */
   public void updateAnimations();

   /**
    * Should be called when the bossPart enters its attack fase.
    * This method is made to be overwritten with custom behavior for the bossPart.
    */
   public void startAttack();

   /**
    * Should be called when the bossPart exits its attack fase.
    * This method is made to be overwritten with custom behavior for the bossPart.
    */
   public void finishAttack();
}
