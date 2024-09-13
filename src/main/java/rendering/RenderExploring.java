package rendering;

import java.awt.Graphics;
import java.util.ArrayList;

import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import gamestates.exploring.Area;
import gamestates.exploring.Exploring;
import main_classes.Game;
import ui.PauseExploring;

public class RenderExploring implements SwingRender {
   private Game game;
   private Exploring exploring;
   private PauseExploring pause;
   private RenderPauseExploring rPause;
   private ArrayList<RenderArea> rAreaList;
   private RenderCutscene rCutscene;

   public RenderExploring(Game game, RenderOptionsMenu rOptionsMenu, RenderCutscene rCutscene) {
      this.exploring = game.getExploring();
      this.pause = exploring.getPauseOverlay();
      this.rPause = new RenderPauseExploring(game, rOptionsMenu);
      this.rAreaList = new ArrayList<>();
      this.rCutscene = rCutscene;
   }

   /**
    * Call whenever you enter EXPLORING, FLYING, BOSSMODE or CINEMATIC,
    * or change area within EXPLORING
    */
   public void setCutsceneManager(DefaultCutsceneManager cutsceneManager) {
      this.rCutscene.setCutsceneManager(cutsceneManager);
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
      rCutscene.draw(g);
      if (pause.isActive()) {
         rPause.draw(g);
         // } else if (mechanicActive) {
         // mechanicOverlay.draw(g);
      }
   }

   @Override
   public void dispose() {
   }

}
