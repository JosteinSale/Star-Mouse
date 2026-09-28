package entities.animation;

/**
 * An animation with an xPos and yPos, as well as a scale for width and height.
 * To move it, alter the public xPos and yPos-variables.
 */
public class PositionedAnimation extends Animation {
   public float xPos;
   public float yPos;
   public float scaleW;
   public float scaleH;

   public PositionedAnimation(int startRow, int startCol, int aniSpeed,
         float xPos, float yPos, float scaleW, float scaleH) {
      super(startRow, startCol, aniSpeed);
      this.xPos = xPos;
      this.yPos = yPos;
      this.scaleW = scaleW;
      this.scaleH = scaleH;
   }
}
