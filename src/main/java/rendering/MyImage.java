package rendering;

import java.awt.image.BufferedImage;

/**
 * A wrapper for an image, for use in the rendering classes.
 * It provides absraction from the concrete image implementation.
 * 
 * Thus if we want to change the type of image being used in the game, we simply
 * have to change it once in this object + once in the DrawUtils-class,
 * instead of having to change it in every single rendering class.
 */
public class MyImage {
   private BufferedImage img; // Replace with your desired image type

   public MyImage(BufferedImage img) {
      this.img = img;
   }

   public BufferedImage getImage() {
      return this.img;
   }

   public int getWidth() {
      return this.img.getWidth();
   }

   public int getHeight() {
      return this.img.getHeight();
   }

   public MySubImage getSubimage(int x, int y, int spriteW, int spriteH) {
      return new MySubImage(this.img.getSubimage(x, y, spriteW, spriteH));
   }

   public void flush() {
      this.img.flush();
   }
}
