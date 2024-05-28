package cutscenes.effects;

import java.awt.Graphics;

public interface DrawableEffect extends CutsceneEffect {
   
   /** Draws the effect as an overlay */
   public void draw(Graphics g);

   /** Returns true if the effect is currently active */
   public boolean isActive();
}
