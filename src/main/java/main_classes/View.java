package main_classes;

import java.awt.Graphics;

import gamestates.Gamestate;
import rendering.misc.RenderCutscene;
import rendering.misc.RenderInfoBox;
import rendering.misc.RenderInfoChoice;
import rendering.misc.RenderOptionsMenu;
import rendering.root_renders.RenderBossMode;
import rendering.root_renders.RenderCinematic;
import rendering.root_renders.RenderExploring;
import rendering.root_renders.RenderFlying;
import rendering.root_renders.RenderLevelSelect;
import rendering.root_renders.RenderMainMenu;
import rendering.root_renders.RenderStartScreen;

/**
 * The View initializes one specialized render-object for each gamestate,
 * and calls the draw-method on them.
 * NOTE: All renders are supposed to be treated like singleton-objects.
 * (We should enforce this later).
 * 
 * Some rendering tasks can be re-used, and are thus split up into smaller
 * objects that can be passed along to the root-renders that need them.
 * 
 * The renderer for each state keeps a reference
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
   private RenderBossMode rBossMode;
   private RenderCinematic rCinematic;

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
      this.rFlying = new RenderFlying(game, game.getFlying(), rCutscene, rOptionsMenu);
      this.rBossMode = new RenderBossMode(
            game, rCutscene, rOptionsMenu,
            rFlying.getRenderPlayer(), rFlying.getRenderProjectiles());
      this.rCinematic = new RenderCinematic(rCutscene);
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
            break;
         case BOSS_MODE:
            rBossMode.draw(g);
            break;
         case CINEMATIC:
            rCinematic.draw(g);
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
         case FLYING:
            rFlying.dispose();
            break;
         case CINEMATIC:
            rCinematic.dispose();
         default:
            break;
      }
   }

   /** Getters - Are needed to load new renderers when we load a new level */
   public RenderExploring getRenderExploring() {
      return this.rExploring;
   }

   public RenderCutscene getRenderCutscene() {
      return this.rCutscene;
   }

   public RenderFlying getRenderFlying() {
      return this.rFlying;
   }

   public RenderBossMode getRenderBossMode() {
      return this.rBossMode;
   }

}
