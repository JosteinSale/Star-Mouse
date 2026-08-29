package utils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;

/**
 * Shared math for entities that support rotation.
 * For now, most utils are designed for square polygons.
 */
public final class PolygonUtils {
   public enum SquarePosition {
      UPPER_LEFT,
      UPPER_RIGHT,
      BOTTOM_RIGHT,
      BOTTOM_LEFT,
      MIDDLE_TOP,
      MIDDLE_RIGHT,
      MIDDLE_BOTTOM,
      MIDDLE_LEFT,
      CENTER
   }

   private PolygonUtils() {
   }

   /** Builds an unrotated, positioned polygon shaped like the given rectangle. */
   public static Polygon newSquareHitboxPolygon(Rectangle2D.Float hitbox) {
      float[] verts = {
            0, 0,                          // x, y upper left corner
            hitbox.width, 0,               // x, y upper right corner
            hitbox.width, hitbox.height,   // x, y lower right corner
            0, hitbox.height               // x, y lower left corner
      };
      Polygon polygon = new Polygon(verts);
      polygon.translate(hitbox.x, hitbox.y);
      return polygon;
   }

   /** Builds an unrotated, positioned polygon shaped like the given rectangle. */
   public static Polygon newSquareHitboxPolygon(float x, float y, float width, float height) {
      float[] verts = {
              0, 0,                        // x, y upper left corner
              width, 0,                    // x, y upper right corner
              width, height,               // x, y lower right corner
              0, height                    // x, y lower left corner
      };
      Polygon polygon = new Polygon(verts);
      polygon.translate(x, y);
      return polygon;
   }

   /** Positions and rotates polygon to match hitbox's current location and the given rotation, in radians. */
   public static void syncPolygonToRectPosition(Polygon polygon, Rectangle2D.Float hitbox, double rotationRadians) {
      polygon.setPosition(hitbox.x, hitbox.y);
      polygon.setRotation((float) (rotationRadians * MathUtils.radiansToDegrees));
   }

   /** Tests a (possibly rotated) polygon against an unrotated rectangle. */
   public static boolean polygonIntersectsRect(Polygon polygon, Rectangle2D.Float rect) {
      Polygon rectAsPolygon = newSquareHitboxPolygon(rect);
      syncPolygonToRectPosition(rectAsPolygon, rect, 0.0);
      return Intersector.overlapConvexPolygons(polygon, rectAsPolygon);
   }

   public static boolean polygonsIntersect(Polygon a, Polygon b) {
      return Intersector.overlapConvexPolygons(a, b);
   }

   /**
    * Updates the given pixel to the current position of the given corner of the square polygon.
    */
   public static void UpdatePixelForSquarePosition(SquarePosition position, Polygon polygon, Point2D.Float px) {
      float[] verts = polygon.getTransformedVertices();
      switch (position) {
         case UPPER_LEFT:
            px.setLocation(verts[0], verts[1]);
            break;
         case UPPER_RIGHT:
            px.setLocation(verts[2], verts[3]);
            break;
         case BOTTOM_RIGHT:
            px.setLocation(verts[4], verts[5]);
            break;
         case BOTTOM_LEFT:
            px.setLocation(verts[6], verts[7]);
            break;
         case MIDDLE_TOP:
            updatePixelToMiddlePointBetween(px, verts[0], verts[1], verts[2], verts[3]);
            break;
         case MIDDLE_RIGHT:
            updatePixelToMiddlePointBetween(px, verts[2], verts[3], verts[4], verts[5]);
            break;
         case MIDDLE_BOTTOM:
            updatePixelToMiddlePointBetween(px, verts[4], verts[5], verts[6], verts[7]);
            break;
         case MIDDLE_LEFT:
            updatePixelToMiddlePointBetween(px, verts[6], verts[7], verts[0], verts[1]);
            break;
         case CENTER:
            updatePixelToMiddlePointBetween(px, verts[0], verts[1], verts[4], verts[5]);
            break;
         default:
            throw new IllegalArgumentException("Invalid SquarePosition: " + position);
      }
   }

   private static void updatePixelToMiddlePointBetween(Point2D.Float px, float x1, float y1, float x2, float y2) {
      float midX = (x1 + x2) / 2f;
      float midY = (y1 + y2) / 2f;
      px.setLocation(midX, midY);
   }
}
