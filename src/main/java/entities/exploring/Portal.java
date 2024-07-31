package entities.exploring;

import java.awt.geom.Rectangle2D;

import entities.Entity;

/**
 * When player touched the Portal-hitbox, he is automatically transported to
 * the next area. The Player-object in the current area is set to a position
 * outside
 * the portal hitbox, so that it doesn't trigger immediately upon the player
 * returning to the current area.
 */
public class Portal extends Entity {
   private int areaItLeadsTo;
   private int reenterDir;

   public Portal(Rectangle2D.Float hitbox, int areaItLeadsTo, int reenterDir) {
      super(hitbox);
      this.areaItLeadsTo = areaItLeadsTo;
      this.reenterDir = reenterDir;
   }

   public int getAreaItLeadsTo() {
      return this.areaItLeadsTo;
   }

   public int getReenterDir() {
      return this.reenterDir;
   }

   public Rectangle2D.Float getHitbox() {
      return this.hitbox;
   }
}
