package entities.exploring;

import entities.Dimensions;
import entities.MyRectangle;
import utils.Constants.Direction;

/**
 * When player touches the Portal-hitbox, he is automatically transported to
 * the next area. The Player-object in the current area is set to a position
 * outside the portal hitbox, so that it doesn't trigger immediately upon the
 * player returning to the current area.
 */
public class Portal extends MyRectangle {
   private int areaItLeadsTo;
   private Direction reenterDir;

   public Portal(Dimensions hitbox, int areaItLeadsTo, Direction reenterDir) {
      super(hitbox);
      this.areaItLeadsTo = areaItLeadsTo;
      this.reenterDir = reenterDir;
   }

   public int getAreaItLeadsTo() {
      return this.areaItLeadsTo;
   }

   public Direction getReenterDir() {
      return this.reenterDir;
   }

   public MyRectangle getHitbox() {
      return this;
   }
}
