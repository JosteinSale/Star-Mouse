package entities.animation;

import entities.animation.Animation.Type;

/**
 * Simple container class for keeping values related to a single animation
 * action
 */
public class AnimationInfo {
   public int aniRow;
   public int nrOfFrames;
   public int speed;
   public Type animationType;

   public AnimationInfo(int aniRow, int nrOfFrames, int speed, Type animationType) {
      this.aniRow = aniRow;
      this.nrOfFrames = nrOfFrames;
      this.speed = speed;
      this.animationType = animationType;
   }
}
