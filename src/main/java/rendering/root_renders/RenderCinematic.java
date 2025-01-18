package rendering.root_renders;

import java.awt.Color;
import java.awt.Graphics;

import rendering.SwingRender;
import rendering.misc.RenderCutscene;
import utils.DrawUtils;

public class RenderCinematic implements SwingRender {
   private RenderCutscene rCutscene;

   public RenderCinematic(RenderCutscene rCutscene) {
      this.rCutscene = rCutscene;
   }

   @Override
   public void draw(Graphics g) {
      rCutscene.draw(g);
      this.drawSkipText(g);
   }

   private void drawSkipText(Graphics g) {
      DrawUtils.drawText(
            g, Color.GRAY, DrawUtils.infoFont,
            "ENTER to skip", 30, 30);
   }

   @Override
   public void dispose() {

   }
}
