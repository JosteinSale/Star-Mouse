package gamestates.flying;

/** A container class for constants related to specific flying levels */
public class FlyLevelInfo {
   private int[] bgImgHeights = { 7600, 10740, 9750 };
   private float[] resetPoints = { 20f, 1300f, 1000f };
   private float[] checkPoints = { 99999f, 13500f, 11950f };
   private float[] skipLevelPoints = { 17000f, 27500f, 23800f };
   private float[] songResetPoints = { 0f, 8f, 8f };
   private float[] songCheckPoints = { 0f, 110f, 0f };

   public int getBgImgHeight(int level) {
      return bgImgHeights[level];
   }

   public float getResetPoint(int level) {
      return resetPoints[level];
   }

   public float getCheckPoint(int level) {
      return checkPoints[level];
   }

   public float getSkipLevelPoint(int level) {
      return skipLevelPoints[level];
   }

   public float getSongResetPoint(int level) {
      return songResetPoints[level];
   }

   public float getSongCheckPoint(int level) {
      return songCheckPoints[level];
   }

}