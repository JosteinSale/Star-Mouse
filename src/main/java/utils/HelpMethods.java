package utils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import entities.CollisionPixels;
import entities.MyCollisionImage;
import rendering.MyImage;
import rendering.MySubImage;

public class HelpMethods {

   /**
    * Checks if the pixel at the given coordinates is solid (i.e. collidable).
    * Solid is defined as the red value in RGBA of the pixel being > 0 && < 100.
    * If the given pixel is outside the bounds of the image, it returns false.
    */
   public static boolean IsSolid(int pixelX, int pixelY, MyCollisionImage collisionImg) {
      if (pixelX > collisionImg.getWidth() || pixelX < 0 ||
            pixelY > collisionImg.getHeight() | pixelY < 0) {
         return false;
      }

      int pix = collisionImg.getImage().getPixel(pixelX, pixelY);
      Color c = new Color();
      Color.rgba8888ToColor(c, pix);
      // Note that a value of c.r == 0 is used to signify 'no pixel' in the collision
      // image, so we don't want to treat it as solid
      if (c.r > 0.0f && c.r < 0.39f) { // 0.39f ~ 100 int
         return true;
      } else {
         return false;
      }
   }

   public static int GetPixelThatCollides(
         CollisionPixels collisionPixels, MyCollisionImage clImg,
         float xLevelOffset, float yLevelOffset) {
      Point2D[] pixels = collisionPixels.get();
      for (int i = 0; i < pixels.length; i++) {
         if (IsSolid(
               (int) ((pixels[i].getX() - xLevelOffset) / 3f),
               (int) ((pixels[i].getY() - yLevelOffset) / 3f),
               clImg)) {
            return i;
         }
      }
      return -1;
   }

   /**
    * Checks all pixels in the given CollisionPixels,
    * and returns true if any of them touches something solid.
    * Note: the coordinates of the square are divided by 3, to match the scale of
    * the collision image.
    */
   public static boolean CollidesWithMap(
         CollisionPixels collisionPixels, MyCollisionImage clImg,
         float xLevelOffset, float yLevelOffset) {
      return GetPixelThatCollides(collisionPixels, clImg, xLevelOffset, yLevelOffset) > -1;
   }

   public static boolean CollidesWithNpc(Rectangle2D.Float playerHitbox, ArrayList<Rectangle2D.Float> npcHitboxes) {
      for (Rectangle2D.Float npcHitbox : npcHitboxes) {
         if (npcHitbox.intersects(playerHitbox)) {
            return true;
         }
      }
      return false;
   }

   /**
    * Takes a single text string and chops it into several lines, according to the
    * given line length limit. Then returns them as a list.
    * 
    * @param text
    * @param lineLengthLimit
    * @return
    */
   public static ArrayList<String> ChopStringIntoLines(String text, int lineLengthLimit) {
      ArrayList<String> formattedStrings = new ArrayList<>();

      String[] words = text.split(" ");
      int letterCount = 0;
      String line = "";
      for (String word : words) {
         if ((letterCount + word.length()) > lineLengthLimit) {
            // Add new
            formattedStrings.add(line.trim());
            line = word + " ";
            letterCount = word.length() + 1; // +1 for space
         } else {
            line += (word + " ");
            letterCount += word.length() + 1;
         }
      }
      formattedStrings.add(line);
      return formattedStrings;
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
