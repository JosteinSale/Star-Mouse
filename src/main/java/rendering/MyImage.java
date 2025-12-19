package rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import utils.Resource;

/**
 * A wrapper for a texture, for use in the rendering classes.
 * It provides abstraction from the concrete image implementation.
 *
 * Thus if we want to change the type of image being used in the game, we simply
 * have to change it once in this object + once in the DrawUtils-class,
 * instead of having to change it in every single rendering class.
 */
public class MyImage implements Resource {
   private Texture texture;

   public MyImage(Texture texture) {
      this.texture = texture;
   }

   public Texture getTexture() {
      return this.texture;
   }

   public int getWidth() {
      return texture.getWidth();
   }

   public int getHeight() {
      return texture.getHeight();
   }

   public MySubImage getSubimage(int x, int y, int spriteW, int spriteH) {
      TextureRegion region = new TextureRegion(texture, x, y, spriteW, spriteH);
      return new MySubImage(region);
   }

   @Override
   public void flush() {
      texture.dispose();
   }
}