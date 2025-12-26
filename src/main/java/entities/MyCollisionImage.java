package entities;

import com.badlogic.gdx.graphics.Pixmap;

import utils.Resource;

/**
 * A wrapper for a collision image, for use in the model classes.
 * It provides abstraction from the concrete image implementation.
 */
public class MyCollisionImage implements Resource {

   private Pixmap collisionImg;

   public MyCollisionImage(Pixmap collisionImg) {
      this.collisionImg = collisionImg;
   }

   public Pixmap getImage() {
      return this.collisionImg;
   }

   public int getWidth() {
      return collisionImg.getWidth();
   }

   public int getHeight() {
      return collisionImg.getHeight();
   }

   @Override
   public void flush() {
      if (!collisionImg.isDisposed()) {
         collisionImg.dispose();
      }
   }

}
