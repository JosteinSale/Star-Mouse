package main_classes;

import java.awt.Graphics;

import gamestates.Gamestate;
import gamestates.MainMenu;
import gamestates.StartScreen;
import gamestates.level_select.LevelSelect;
import rendering.RenderLevelSelect;
import rendering.RenderMainMenu;
import rendering.RenderStartScreen;

/**
 * Renders all the gamestates.
 * As arguments it takes an instance of each gamestate. Then it initializes one
 * specialized render-object for each gamestate, and keeps these references.
 */
public class View {
   protected RenderStartScreen rStartScreen;
   protected RenderMainMenu rMainMenu;
   protected RenderLevelSelect rLevelSelect;

   public View(Game game, StartScreen startScreen, MainMenu mainMenu, LevelSelect levelSelect) {
      this.rStartScreen = new RenderStartScreen(startScreen);
      this.rMainMenu = new RenderMainMenu(game, mainMenu);
      this.rLevelSelect = new RenderLevelSelect(levelSelect, rMainMenu.getBgImg());
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
         default:
            break;
      }
   }

}
