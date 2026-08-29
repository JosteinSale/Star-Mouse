package entities;

import com.badlogic.gdx.math.Polygon;
import static utils.PolygonUtils.UpdatePixelForSquarePosition;

import java.awt.geom.Point2D;

import static utils.PolygonUtils.SquarePosition.*;

/**
 * Keeps a list of pixels that can be used for collision detection.
 * It's useful for detecting collision with the map (because we need check specific pixels on the bitmap).
 * Also useful for knowing which specific part of the hitbox is colliding with an enemy.
 * This is used e.g. in the PlayerFly object, where a collision with an enemy pushes the
 * player in the opposite direction.
 *
 * NOTE: Only square hitboxes are supported.
 * The collision pixels are calculated based on the square's position and rotation.
 * This can be in the corners, the center, or any other relevant position depending on the configuration.
 */
public class CollisionPixels {
   private final Polygon square;
   private Point2D.Float[] collisionPixels;
   public enum CollisionAt {
      TOP_TWO_CORNERS,
      CENTER,
      BOTTOM_CENTER,
      ALL_FOUR_CORNERS,
      NINE_POINT_GRID
   }
   private final CollisionAt configuration;

   public CollisionPixels(MyRectangle rect, CollisionAt configuration) {
      this.square = rect.getPolygon();
      this.configuration = configuration;
      constructCollisionPixels();
   }

   private void constructCollisionPixels() {
      switch (configuration) {
         case TOP_TWO_CORNERS:
            collisionPixels = new Point2D.Float[2];
            break;
         case CENTER, BOTTOM_CENTER:
            collisionPixels = new Point2D.Float[1];
            break;
         case ALL_FOUR_CORNERS:
            collisionPixels = new Point2D.Float[4];
            break;
         case NINE_POINT_GRID:
            collisionPixels = new Point2D.Float[9];
            break;
      }
      populateCollisionPixels();
      updateCollisionPixels();
   }

   private void populateCollisionPixels() {
      for (int i = 0; i < collisionPixels.length; i++) {
         collisionPixels[i] = new Point2D.Float(0, 0);
      }
   }

   private void updateCollisionPixels() {
      switch (configuration) {
         case TOP_TWO_CORNERS:
            updateCollisionPixelsForTopTwoCorners();
            break;
         case CENTER:
            updateCollisionPixelsForCenter();
            break;
         case BOTTOM_CENTER:
            updateCollisionPixelsForBottomCenter();
            break;
         case ALL_FOUR_CORNERS:
            updateCollisionPixelsForAllFourCorners();
            break;
         case NINE_POINT_GRID:
            updateCollisionPixelsForNinePointGrid();
            break;
      }
   }

   /**
    * Call this method to update the collision pixels to match the current position of the polygon square.
    * This should be called whenever the square moves or rotates.
    */
   public void update() {
      updateCollisionPixels();
   }

   public Point2D[] get() {
      return collisionPixels;
   }

   private void updateCollisionPixelsForTopTwoCorners() {
      UpdatePixelForSquarePosition(UPPER_LEFT, square, collisionPixels[0]);
      UpdatePixelForSquarePosition(UPPER_RIGHT, square, collisionPixels[1]);
   }

   private void updateCollisionPixelsForCenter() {
      UpdatePixelForSquarePosition(CENTER, square, collisionPixels[0]);
   }

   private void updateCollisionPixelsForBottomCenter() {
      UpdatePixelForSquarePosition(MIDDLE_BOTTOM, square, collisionPixels[0]);
   }

   private void updateCollisionPixelsForAllFourCorners() {
      UpdatePixelForSquarePosition(UPPER_LEFT, square, collisionPixels[0]);
      UpdatePixelForSquarePosition(UPPER_RIGHT, square, collisionPixels[1]);
      UpdatePixelForSquarePosition(BOTTOM_RIGHT, square, collisionPixels[2]);
      UpdatePixelForSquarePosition(BOTTOM_LEFT, square, collisionPixels[3]);
   }

   /*
    * 9 pixels in the player hitbox are used for collision detection with enemies
    * and map. These are enumerated according to the order they should be checked
    * by the collision algorithms. To understand the logic, see the planning
    * notes.
    *
    * 4 - 0 - 5
    * 1 - 6 - 2
    * 7 - 3 - 8
    *
    */
   private void updateCollisionPixelsForNinePointGrid() {
      UpdatePixelForSquarePosition(MIDDLE_TOP, square, collisionPixels[0]);
      UpdatePixelForSquarePosition(MIDDLE_LEFT, square, collisionPixels[1]);
      UpdatePixelForSquarePosition(MIDDLE_RIGHT, square, collisionPixels[2]);
      UpdatePixelForSquarePosition(MIDDLE_BOTTOM, square, collisionPixels[3]);
      UpdatePixelForSquarePosition(UPPER_LEFT, square, collisionPixels[4]);
      UpdatePixelForSquarePosition(UPPER_RIGHT, square, collisionPixels[5]);
      UpdatePixelForSquarePosition(CENTER, square, collisionPixels[6]);
      UpdatePixelForSquarePosition(BOTTOM_LEFT, square, collisionPixels[7]);
      UpdatePixelForSquarePosition(BOTTOM_RIGHT, square, collisionPixels[8]);
   }
}
