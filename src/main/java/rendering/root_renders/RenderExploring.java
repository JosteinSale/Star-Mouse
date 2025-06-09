package rendering.root_renders;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import gamestates.exploring.Area;
import gamestates.exploring.Exploring;
import main_classes.Game;
import rendering.Render;
import rendering.exploring.RenderArea;
import rendering.exploring.RenderMechanic;
import rendering.exploring.RenderPauseExploring;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderInfoBox;
import rendering.misc.RenderInfoChoice;
import rendering.misc.RenderOptionsMenu;
import ui.PauseExploring;

public class RenderExploring implements Render {
   private Game game;
   private Exploring exploring;
   private PauseExploring pause;
   private RenderPauseExploring rPause;
   private RenderMechanic rMechanic;
   private ArrayList<RenderArea> rAreaList;
   private RenderCutscene rCutscene;

   public RenderExploring(Game game, RenderOptionsMenu rOptionsMenu, RenderCutscene rCutscene, RenderInfoBox rInfoBox,
         RenderInfoChoice rInfoChoice) {
      this.game = game;
      this.exploring = game.getExploring();
      this.pause = exploring.getPauseOverlay();
      this.rPause = new RenderPauseExploring(game, rOptionsMenu);
      this.rAreaList = new ArrayList<>();
      this.rMechanic = new RenderMechanic(
            game, exploring.getMechanicOverlay(), rInfoBox, rInfoChoice);
      this.rCutscene = rCutscene;
   }

   /**
    * Call whenever you enter EXPLORING or change area within EXPLORING
    */
   public void setCutsceneManager(DefaultCutsceneManager cutsceneManager) {
      this.rCutscene.setCutsceneManager(cutsceneManager);
   }

   /** Is called from Exploring when we load a new level */
   public void loadLevel(int level) {
      // Clear old renderers
      this.rAreaList.clear();

      // Load in new areas
      ArrayList<Area> areas = exploring.getAreas();
      for (int i = 0; i < areas.size(); i++) {
         this.rAreaList.add(new RenderArea(game, areas.get(i), level, i + 1));
      }
   }

   @Override
   public void draw(SpriteBatch sb) {
      rAreaList.get(exploring.currentArea - 1).draw(sb);
      rCutscene.draw(sb);
      if (pause.isActive()) {
         rPause.draw(sb);
      } else if (exploring.isMechanicActive()) {
         rMechanic.draw(sb);
      }
   }

}
