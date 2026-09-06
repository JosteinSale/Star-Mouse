package entities;

/**
 * Simple container class for dimensions, holding a position (x, y) and size
 * (width, height).
 */
public class Dimensions {
   public float x;
   public float y;
   public int width;
   public int height;

   public Dimensions(float x, float y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public Dimensions(Dimensions dimensions) {
      this.x = dimensions.x;
      this.y = dimensions.y;
      this.width = dimensions.width;
      this.height = dimensions.height;
   }
}
