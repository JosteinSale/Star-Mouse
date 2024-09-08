package main_classes;

import java.awt.Graphics;

import gamestates.Gamestate;
import gamestates.StartScreen;
import rendering.RenderStartScreen;

public class View {
   protected RenderStartScreen rStartScreen;

   public View(StartScreen startScreen) {
      this.rStartScreen = new RenderStartScreen(startScreen);
   }

   public void draw(Graphics g) {
      switch (Gamestate.state) {
         case START_SCREEN:
            rStartScreen.draw(g);
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
         default:
            break;
      }
   }

}
