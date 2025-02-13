package rendering;

import java.awt.image.BufferedImage;

/** Similar to the MyImage-class, except for sub images. */
public class MySubImage {
   private BufferedImage subImg; // Replace with your desired implementation

   public MySubImage(BufferedImage subImg) {
      this.subImg = subImg;
   }

   public BufferedImage getImage() {
      return this.subImg;
   }

   public int getWidth() {
      return this.subImg.getWidth();
   }

   public int getHeight() {
      return this.subImg.getHeight();
   }
}
