package rendering;

import java.awt.Graphics;
import java.util.ArrayList;

import gamestates.exploring.Area;
import gamestates.exploring.Exploring;
import main_classes.Game;
import ui.PauseExploring;

public class RenderExploring implements SwingRender {
   private Game game;
   private Exploring exploring;
   private PauseExploring pause;
   private RenderPauseExploring renderPause;
   private ArrayList<RenderArea> rAreaList;

   public RenderExploring(Game game, RenderOptionsMenu rOptionsMenu) {
      this.exploring = game.getExploring();
      this.pause = exploring.getPauseOverlay();
      this.renderPause = new RenderPauseExploring(game, rOptionsMenu);
      this.rAreaList = new ArrayList<>();
   }

   /** Is called from Exploring when we load a new level */
   public void loadLevel(int level) {
      ArrayList<Area> areas = exploring.getAreas();
      for (int i = 0; i < areas.size(); i++) {
         this.rAreaList.add(new RenderArea(game, areas.get(i), level, i + 1));
      }
   }

   @Override
   public void draw(Graphics g) {
      rAreaList.get(exploring.currentArea - 1).draw(g);
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
