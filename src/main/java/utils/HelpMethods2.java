package utils;

import java.awt.image.BufferedImage;

public class HelpMethods2 {

   public static BufferedImage[][] GetEnemyAnimations(
         BufferedImage img, int spriteW, int spriteH, int rows, int cols) {
      return HelpMethods2.GetUnscaled2DAnimationArray(
            img, rows, cols, spriteW, spriteH);
   }

   /** Returns an unscaled 2D animation array */
   public static BufferedImage[][] GetUnscaled2DAnimationArray(
         BufferedImage img, int aniRows, int aniCols, int spriteW, int spriteH) {
      BufferedImage[][] animations = new BufferedImage[aniRows][aniCols];
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
   public static BufferedImage[] GetUnscaled1DAnimationArray(
         BufferedImage img, int aniCols, int spriteW, int spriteH) {
      BufferedImage[] animation = new BufferedImage[aniCols];
      for (int c = 0; c < aniCols; c++) {
         animation[c] = img.getSubimage(
               c * spriteW, 0, spriteW, spriteH);
      }
      return animation;
   }
}
