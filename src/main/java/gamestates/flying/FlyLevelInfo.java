package gamestates.flying;

/** A container class for constants related to specific flying levels */
public class FlyLevelInfo {
   private int[] bgImgHeights = {
         7600, 10740, 9750, 9200, 10500, 1200, 1200,
         1200, 1200, 1200, 1200, 1200, 1200, 1200 };
   private int[] clImgHeights = {
         6500, 10000, 10000, 10000, 10000, 10000, 10000,
         10000, 10000, 10000, 10000, 10000, 10000, 10000 };
   private float[] resetPoints = {
         20f, 1300f, 1000f, 20f, 20f, 20f, 20f,
         20f, 20f, 20f, 20f, 20f, 20f, 20f };
   private float[] checkPoints = {
         99999f, 13500f, 11950f, 11000f, 11700f, 99999f, 99999f,
         99999f, 99999f, 99999f,
         99999f, 99999f, 99999f, 99999f };
   private float[] skipLevelPoints = {
         17000f, 27500f, 23800f, 24200f, 27500f, 9900f, 9900f,
         9900f, 9900f, 9900f, 9900f, 9900f, 9900f, 9900f };
   private float[] songResetPoints = {
         0f, 8f, 8f, 0f, 0f, 0f, 0f,
         0f, 0f, 0f, 0f, 0f, 0f, 0f };
   private float[] songCheckPoints = {
         0f, 110f, 0f, 0f, 0f, 0f, 0f,
         0f, 0f, 0f, 0f, 0f, 0f, 0f };

   public int getBgImgHeight(int level) {
      return bgImgHeights[level];
   }

   public int getClImgHeight(int level) {
      return clImgHeights[level];
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
