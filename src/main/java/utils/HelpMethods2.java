package utils;

import java.awt.image.BufferedImage;

public class HelpMethods2 {

   public static BufferedImage[][] GetEnemyAnimations(
         BufferedImage img, int spriteW, int spriteH, int rows, int cols) {
      BufferedImage[][] animations = new BufferedImage[rows][cols];
      for (int j = 0; j < rows; j++) {
         for (int i = 0; i < cols; i++) {
            animations[j][i] = img.getSubimage(
                  i * spriteW, j * spriteH, spriteW, spriteH);
         }
      }
      return animations;
   }

   /** Returns an unscaled 2D animation array */
   public static BufferedImage[][] GetAnimationArray(
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
   public static BufferedImage[] GetSimpleAnimationArray(
         BufferedImage img, int aniCols, int spriteW, int spriteH) {
      BufferedImage[] animation = new BufferedImage[aniCols];
      for (int c = 0; c < aniCols; c++) {
         animation[c] = img.getSubimage(
               c * spriteW, 0, spriteW, spriteH);
      }
      return animation;
   }
}
