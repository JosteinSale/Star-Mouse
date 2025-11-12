package entities.bossmode;

/**
 * A container class for info pertaining to a spefific animation.
 */
public class AnimationInfo {
   public int aniRow;
   protected int nrOfFrames;
   protected int speed;
   protected int loopBackToIndex;
   protected boolean reverse; // True if the animation should play in reverse

   public AnimationInfo(int aniRow, int nrOfFrames, int speed, int loopBackToIndex, boolean reverse) {
      this.aniRow = aniRow;
      this.nrOfFrames = nrOfFrames;
      this.speed = speed;
      this.loopBackToIndex = loopBackToIndex;
      this.reverse = reverse;
   }
}
