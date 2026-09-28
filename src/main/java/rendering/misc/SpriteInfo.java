package rendering.misc;

/**
 * Simple container class for containing the info of a sprite array.
 */
public class SpriteInfo {
   public final String spriteName;
   public final int spriteW;
   public final int spriteH;
   public final int rows;
   public final int cols;

   public SpriteInfo(String spriteName, int spriteW, int spriteH, int rows, int cols) {
      this.spriteName = spriteName;
      this.spriteW = spriteW;
      this.spriteH = spriteH;
      this.rows = rows;
      this.cols = cols;
   }
}
