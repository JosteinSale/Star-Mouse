package gamestates.flying;

/** A container class for constants related to specific flying levels */
public class FlyLevelInfo {
   private static int[] bgImgHeights = {
         7600, 10740, 9750, 9200, 10500, 1200, 1200,
         1200, 1200, 1200, 1200, 1200, 1200, 1200 };
   private static int[] clImgHeights = {
         6500, 10000, 10000, 10000, 10000, 10000, 10000,
         10000, 10000, 10000, 10000, 10000, 10000, 10000 };
   private static float[] resetPoints = {
         20f, 1300f, 1000f, 20f, 20f, 20f, 20f,
         20f, 20f, 20f, 20f, 20f, 20f, 20f };
   private static float[] checkPoints = {
         99999f, 13500f, 11950f, 11000f, 11700f, 99999f, 99999f,
         99999f, 99999f, 99999f,
         99999f, 99999f, 99999f, 99999f };
   private static float[] skipLevelPoints = {
         17000f, 27500f, 23800f, 24200f, 27500f, 9900f, 9900f,
         9900f, 9900f, 9900f, 9900f, 9900f, 9900f, 9900f };
   private static float[] songResetPoints = {
         0f, 8f, 8f, 0f, 0f, 0f, 0f,
         0f, 0f, 0f, 0f, 0f, 0f, 0f };
   private static float[] songCheckPoints = {
         0f, 110f, 0f, 0f, 96f, 0f, 0f,
         0f, 0f, 0f, 0f, 0f, 0f, 0f };

   /** Returns the scaled height of the background image */
   public static int getBgImgHeight(int level) {
      return bgImgHeights[level];
   }

   /** Returns the unscaled height of the collission image */
   public static int getClImgHeight(int level) {
      return clImgHeights[level];
   }

   public static float getResetPoint(int level) {
      return resetPoints[level];
   }

   public static float getCheckPoint(int level) {
      return checkPoints[level];
   }

   public static float getSkipLevelPoint(int level) {
      return skipLevelPoints[level];
   }

   public static float getSongResetPoint(int level) {
      return songResetPoints[level];
   }

   public static float getSongCheckPoint(int level) {
      return songCheckPoints[level];
   }

}
