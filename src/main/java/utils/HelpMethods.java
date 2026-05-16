package utils;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import entities.MyCollisionImage;
import rendering.MyImage;
import rendering.MySubImage;

public class HelpMethods {

   /**
    * Checks if the pixel at the given coordinates is solid (i.e. collidable).
    * Solid is defined as the red value in RGBA of the pixel being > 0 && < 100.
    */
   public static boolean IsSolid(int pixelX, int pixelY, MyCollisionImage collisionImg) {
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

   /**
    * Checks the four corners of the hitbox,
    * and returns true if any of them touches something solid.
    * Note: the coordinates of the hitbox are divided by 3, to match the scale of
    * the collision image.
    */
   public static boolean CollidesWithMap(Rectangle2D.Float hitbox, MyCollisionImage collisionImg) {
      float newX1 = (hitbox.x) / 3;
      float newY1 = (hitbox.y) / 3;
      float newX2 = (hitbox.x + hitbox.width) / 3;
      float newY2 = (hitbox.y + hitbox.height) / 3;
      if (!IsSolid((int) newX1, (int) newY1, collisionImg) &&
            !IsSolid((int) newX2, (int) newY1, collisionImg) &&
            !IsSolid((int) newX1, (int) newY2, collisionImg) &&
            !IsSolid((int) newX2, (int) newY2, collisionImg)) {
         return false;
      } else {
         return true;
      }
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
    * @param lineLimit
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
