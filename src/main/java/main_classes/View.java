package main_classes;

import java.awt.Graphics;

import gamestates.Gamestate;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderInfoBox;
import rendering.misc.RenderInfoChoice;
import rendering.misc.RenderOptionsMenu;
import rendering.root_renders.RenderExploring;
import rendering.root_renders.RenderFlying;
import rendering.root_renders.RenderLevelSelect;
import rendering.root_renders.RenderMainMenu;
import rendering.root_renders.RenderStartScreen;

/**
 * Renders all the gamestates.
 * It initializes one specialized render-object for each gamestate,
 * and keeps these references. Additional renderers that are re-used in various
 * parts of the game are also initialized here.
 * The renderer for each state in turn keeps a reference
 * to its respective model, + any additional renderers it may need.
 */
public class View {
   private RenderStartScreen rStartScreen;
   private RenderInfoBox rInfoBox;
   private RenderInfoChoice rInfoChoice;
   private RenderMainMenu rMainMenu;
   private RenderLevelSelect rLevelSelect;
   private RenderOptionsMenu rOptionsMenu;
   private RenderExploring rExploring;
   private RenderCutscene rCutscene;
   private RenderFlying rFlying;

   public View(Game game) {
      this.rStartScreen = new RenderStartScreen(game.getStartScreen());
      this.rInfoBox = new RenderInfoBox(game.getTextboxManager().getInfoBox());
      this.rInfoChoice = new RenderInfoChoice(
            game.getTextboxManager().getInfoChoice(),
            rInfoBox.getBackground(),
            rInfoBox.getFont());
      this.rOptionsMenu = new RenderOptionsMenu(game.getOptionsMenu(), game.getOptionsMenu().getControlsMenu());
      this.rMainMenu = new RenderMainMenu(game, rOptionsMenu, rInfoChoice);
      this.rLevelSelect = new RenderLevelSelect(game, rMainMenu.getBgImg());
      this.rCutscene = new RenderCutscene(game.getTextboxManager(), rInfoBox, rInfoChoice);
      this.rExploring = new RenderExploring(game, rOptionsMenu, rCutscene, rInfoBox, rInfoChoice);
      this.rFlying = new RenderFlying(game, game.getFlying(), rCutscene);
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
         case FLYING:
            rFlying.draw(g);
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
         case FLYING:
            rFlying.dispose();
            break;
         default:
            break;
      }
   }

   /** Is needed to load new renderers when we load a new level */
   public RenderExploring getRenderExploring() {
      return this.rExploring;
   }

   public RenderCutscene getRenderCutscene() {
      return this.rCutscene;
   }

   public RenderFlying getRenderFlying() {
      return this.rFlying;
   }

}
