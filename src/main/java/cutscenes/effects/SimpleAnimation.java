package cutscenes.effects;

/**
 * Contains the numerical values needed to represent an animation.
 * It differs from AnimatedComponent in that it's simpler to construct,
 * and only has 1 animation, that always loops.
 * 
 * To move it, alter the public xPos and yPos-variables.
 */
public class SimpleAnimation {
   public int aniIndex = 0;
   private int aniTick = 0;
   private int aniSpeed;
   public int aniLength;
   public float xPos;
   public float yPos;
   public float scaleW;
   public float scaleH;

   public SimpleAnimation(float xPos, float yPos, float scaleW, float scaleH, int aniSpeed, int aniLength) {
      this.xPos = xPos;
      this.yPos = yPos;
      this.scaleW = scaleW;
      this.scaleH = scaleH;
      this.aniSpeed = aniSpeed;
      this.aniLength = aniLength;
   }

   public void updateAnimation() {
      aniTick++;
      if (aniTick >= aniSpeed) {
         aniIndex++;
         aniTick = 0;
         if (aniIndex >= aniLength) {
            aniIndex = 0;
         }
      }
   }
}
