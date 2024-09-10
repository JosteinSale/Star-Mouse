package rendering;

import java.awt.Graphics;

import gamestates.exploring.Exploring;
import main_classes.Game;
import ui.PauseExploring;

public class RenderExploring implements SwingRender {
   private Exploring exploring;
   private PauseExploring pause;
   private RenderPauseExploring renderPause;

   public RenderExploring(Game game, RenderOptionsMenu rOptionsMenu) {
      this.exploring = game.getExploring();
      this.pause = exploring.getPauseOverlay();
      this.renderPause = new RenderPauseExploring(game, rOptionsMenu);
   }

   @Override
   public void draw(Graphics g) {
      // areas.get(currentArea - 1).draw(g);
      if (pause.isActive()) {
         renderPause.draw(g);
         // } else if (mechanicActive) {
         // mechanicOverlay.draw(g);
      }
   }

   @Override
   public void dispose() {
   }

}
