package entities.bossmode;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/** An interface that represents a default BossPart. It has methods for updating the 
 * x- y- and rotation, checking collision with Rectangle2D's, and drawing.
 * Specific implementations of bossParts can extend this object, and should
 * override the `onPlayerCollision`-method and 'onTeleportHit'-method
 */
public interface IBossPart {
   
   /** Moves the bossPart x- and y-position, as well as its rotation 
    * around the hitbox center. Collision-rectangles are updated.
    */
   public void updatePosition(int deltaX, int deltaY, Double deltaR);

   /** Sets new positions for the bossPart x and y, as well as its rotation 
    * around the hitbox center. Collision-rectangles are updated.
    */
   public void setPosition(int centerX, int centerY, Double rotation);

   /** Returns true if the point is inside the bossPart */
   public boolean containsPoint(Point p);

   /** Returns true if the rectangle intersects the bossPart */
   public boolean intersectsRect(Rectangle2D.Float hb);

   /** OBS: for drawing, you should always call the 
    * super-implementation in DefaultBossPart */
   public void draw(Graphics g);

   /** Use for testing */
   public Rectangle2D.Float getNonRotatedHitbox();

   /** The player needs access to the active-status of the bossPart.
    * If the bossPart isn't currently in an active fase, it should be set to false.
    * @param active
    */
   public void setActive(boolean active);

   /** A method invoked by the player. Returns true if the bossPart is active */
   public boolean isActive();

   /** A method invoked by the player if a collision is detected.
    * It should implement custom behavior for the bossPart upon player collision.
    * This method is made to be overwritten, the default implementation does nothing.
    */
   public void onPlayerCollision();

   /** A method invoked by the player if a teleport hit is detected.
    * It should implement custom behavior for the bossPart upon being hit 
    * with teleportation.
    * This method is made to be overwritten, the default implementation does nothing.
    */
   public void onTeleportHit();

   /** A method invoked by the Boss at 60 FPS, if the attack is active.
    * This method is made to be overwritten, the default implementation does nothing.
    */
   public void updateBehavior();

   /** A method invoked by the Boss at 60 FPS, if the attack is active.
    * This method is made to be overwritten with custom behavior for the bossPart.
    * Alter the aniIndex- and action-variables which was inherited from the
    * DefaultBossPart.
    */
   public void updateAnimations();
}
