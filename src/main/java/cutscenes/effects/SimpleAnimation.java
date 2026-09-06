package cutscenes.effects;

import entities.AnimationFrame;

/**
 * Contains the numerical values needed to represent an animation.
 * It differs from AnimatedComponent in that it's simpler to construct,
 * and only has 1 animation, that always loops.
 * 
 * To move it, alter the public xPos and yPos-variables.
 */
public class SimpleAnimation {
   AnimationFrame animation;
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
      this.animation = new AnimationFrame(0, 0, aniSpeed, aniLength);
      this.aniLength = aniLength;
   }

   public void updateAnimation() {
      animation.update();
   }

   public int getFrame() {
      return animation.getFrame();
   }
}
