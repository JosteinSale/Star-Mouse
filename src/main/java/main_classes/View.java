package main_classes;

import java.awt.Graphics;

import gamestates.Gamestate;
import rendering.RenderCutscene;
import rendering.RenderExploring;
import rendering.RenderLevelSelect;
import rendering.RenderMainMenu;
import rendering.RenderOptionsMenu;
import rendering.RenderPauseExploring;
import rendering.RenderStartScreen;

/**
 * Renders all the gamestates.
 * It initializes one specialized render-object for each gamestate,
 * and keeps these references.
 * The renderer for each state in turn keeps a reference
 * to its respective model, + any additional renderers it may need.
 */
public class View {
   protected RenderStartScreen rStartScreen;
   protected RenderMainMenu rMainMenu;
   protected RenderLevelSelect rLevelSelect;
   protected RenderPauseExploring rPauseExploring;
   protected RenderOptionsMenu rOptionsMenu;
   protected RenderExploring rExploring;
   protected RenderCutscene rCutscene;

   public View(Game game) {
      this.rStartScreen = new RenderStartScreen(game);
      this.rOptionsMenu = new RenderOptionsMenu(game);
      this.rMainMenu = new RenderMainMenu(game, rOptionsMenu);
      this.rLevelSelect = new RenderLevelSelect(game, rMainMenu.getBgImg());
      this.rPauseExploring = new RenderPauseExploring(game, rOptionsMenu);
      this.rCutscene = new RenderCutscene(game);
      this.rExploring = new RenderExploring(game, rOptionsMenu, rCutscene);
   }

   public void draw(Graphics g) {
      switch (Gamestate.state) {
         case START_SCREEN:
            rStartScreen.draw(g);
            break;
         case MAIN_MENU:
            rMainMenu.draw(g);
            break;
         case LEVEL_SELECT:
            rLevelSelect.draw(g);
            break;
         case EXPLORING:
            rExploring.draw(g);
            break;
         default:
            break;
      }
   }

   /**
    * Disposes the visual resources for the current game state.
    * OBS: call this method before changing game state.
    */
   public void dispose() {
      switch (Gamestate.state) {
         case START_SCREEN:
            rStartScreen.dispose();
            break;
         case MAIN_MENU:
            rMainMenu.dispose();
            break;
         case LEVEL_SELECT:
            rLevelSelect.dispose();
            break;
         case EXPLORING:
            rExploring.dispose();
            break;
         default:
            break;
      }
   }

   /** Is needed to load new renderers when we load a new level */
   public RenderExploring getRenderExploring() {
      return this.rExploring;
   }

   /** Is needed to load new SimpleAnimation-spriteArrays for ObjectMove */
   public RenderCutscene getRenderCutscene() {
      return this.rCutscene;
   }

}
