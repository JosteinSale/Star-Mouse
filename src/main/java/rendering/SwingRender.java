package rendering;

import java.awt.Graphics;

public interface SwingRender {

   /** Draws the gamestate */
   public void draw(Graphics g);

   /** Disposes of visual resources */
   public void dispose();

}
