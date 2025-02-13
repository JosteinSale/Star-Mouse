package utils;

import rendering.MyImage;
import rendering.MySubImage;

public class HelpMethods2 {

   public static MySubImage[][] GetEnemyAnimations(
         MyImage img, int spriteW, int spriteH, int rows, int cols) {
      return HelpMethods2.GetUnscaled2DAnimationArray(
            img, rows, cols, spriteW, spriteH);
   }

   /** Returns an unscaled 2D animation array */
   public static MySubImage[][] GetUnscaled2DAnimationArray(
         MyImage img, int aniRows, int aniCols, int spriteW, int spriteH) {
      MySubImage[][] animations = new MySubImage[aniRows][aniCols];
      for (int r = 0; r < aniRows; r++) {
         for (int c = 0; c < aniCols; c++) {
            animations[r][c] = img.getSubimage(
                  c * spriteW,
                  r * spriteH, spriteW, spriteH);
         }
      }
      return animations;
   }

   /** Returns an unscaled 1D animation array */
   public static MySubImage[] GetUnscaled1DAnimationArray(
         MyImage img, int aniCols, int spriteW, int spriteH) {
      MySubImage[] animation = new MySubImage[aniCols];
      for (int c = 0; c < aniCols; c++) {
         animation[c] = img.getSubimage(
               c * spriteW, 0, spriteW, spriteH);
      }
      return animation;
   }
}
