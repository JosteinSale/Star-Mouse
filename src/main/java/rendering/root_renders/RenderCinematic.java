package rendering.root_renders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gamestates.Cinematic;
import main_classes.Game;
import rendering.SwingRender;
import rendering.misc.RenderCutscene;
import utils.ResourceLoader;

public class RenderCinematic implements SwingRender {
   private Cinematic cinematic;
   private RenderCutscene rCutscene;

   private Font skipFont;

   public RenderCinematic(Cinematic cinematic, RenderCutscene rCutscene) {
      this.cinematic = cinematic;
      this.rCutscene = rCutscene;
      this.skipFont = ResourceLoader.getInfoFont();
   }

   @Override
   public void draw(Graphics g) {
      rCutscene.draw(g);
      this.drawSkipText(g);
   }

   private void drawSkipText(Graphics g) {
      g.setColor(Color.GRAY);
      g.setFont(skipFont);
      g.drawString(
            "ENTER to skip",
            (int) (30 * Game.SCALE), (int) (30 * Game.SCALE));
   }

   @Override
   public void dispose() {

   }
}
