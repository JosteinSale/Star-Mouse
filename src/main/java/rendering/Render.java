package rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** All Render objects should be treated like singletons */
public interface Render {

   /** Draws the gamestate */
   public void draw(SpriteBatch sb);

}
