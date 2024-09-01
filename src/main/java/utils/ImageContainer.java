package utils;

import java.util.ArrayList;
import java.awt.Image;

/**
 * A container class for images, which can simplify the process of flushing
 * images to free up memory.
 * 
 * Add image references with the addImage-method.
 * When the images are no longer needed to be kept in memory, call the
 * flush-method.
 */
public class ImageContainer {
   private ArrayList<Image> images;

   public ImageContainer() {
      this.images = new ArrayList<>();
   }

   public void addImage(Image img) {
      this.images.add(img);
   }

   public void flushAll() {
      for (Image img : images) {
         img.flush();
         img = null;
      }
   }
}
