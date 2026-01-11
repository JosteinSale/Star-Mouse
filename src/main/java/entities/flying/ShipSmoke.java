package entities.flying;

import java.util.ArrayList;
import java.awt.Point;

/**
 * This class will represent a trail of smoke trailing behind the player ship.
 * Each wingtip has a trail of smoke behind it, making two trails in total.
 */
public class ShipSmoke {
   // We will represent each trail as a list of smoke points
   public ArrayList<Point.Float> leftTrailingSmokePoints;
   public ArrayList<Point.Float> rightTrailingSmokePoints;
   private static final int AMOUNT_OF_POINTS = 60;
   private PlayerFly player;
   private int LEFT = 1;
   private int RIGHT = -1;

   public ShipSmoke(PlayerFly player) {
      this.player = player;
      this.leftTrailingSmokePoints = new ArrayList<>();
      this.rightTrailingSmokePoints = new ArrayList<>();
   }

   public void update(float ySpeed) {
      if (leftTrailingSmokePoints.size() > AMOUNT_OF_POINTS) {
         leftTrailingSmokePoints.remove(0);
      }
      if (rightTrailingSmokePoints.size() > AMOUNT_OF_POINTS) {
         rightTrailingSmokePoints.remove(0);
      }

      // Make 2 new points and add them
      Point.Float leftPoint = getNewSmokePoint(LEFT);
      Point.Float rightPoint = getNewSmokePoint(RIGHT);
      leftTrailingSmokePoints.add(leftPoint);
      rightTrailingSmokePoints.add(rightPoint);

      // Move all points downwards
      leftTrailingSmokePoints.forEach(point -> point.y += 2 * ySpeed);
      rightTrailingSmokePoints.forEach(point -> point.y += 2 * ySpeed);
   }

   private Point.Float getNewSmokePoint(int direction) {
      Point.Float point = new Point.Float();
      point.y = player.hitbox.y + player.hitbox.height;
      point.x = player.hitbox.x + 20 + (25 * direction);
      return point;
   }

   /**
    * Returns the alpha value for a given smoke point, given its index
    * in the trailingSmokePoint-list.
    */
   public static float getAlphaForPoint(int index) {
      return (1f / AMOUNT_OF_POINTS) * index;
   }

   public void reset() {
      leftTrailingSmokePoints.clear();
      rightTrailingSmokePoints.clear();
   }
}
