package rendering;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import utils.Resource;

/** Similar to the MyImage-class, except for sub images. */
public class MySubImage implements Resource {
   private TextureRegion subImg; // Replace with your desired implementation

   public MySubImage(TextureRegion subImg) {
      this.subImg = subImg;
   }

   public TextureRegion getImage() {
      return this.subImg;
   }

   public int getWidth() {
      return this.subImg.getRegionWidth();
   }

   public int getHeight() {
      return this.subImg.getRegionHeight();
   }

   @Override
   public void flush() {
      // No resources to dispose of in this implementation
   }
}
