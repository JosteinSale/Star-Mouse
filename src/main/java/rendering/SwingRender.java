package rendering;

import java.awt.Graphics;

/** All Render objects should be treated like singletons */
public interface SwingRender {

   /** Draws the gamestate */
   public void draw(Graphics g);

   /** Disposes of visual resources */
   public void dispose();

}
