package cutscenes.cutsceneManagers;

import cutscenes.effects.FadeEffect;
import cutscenes.effects.FadeHeaderEffect;
import cutscenes.effects.FellowShipEffect;
import cutscenes.effects.FillScreenEffect;
import cutscenes.effects.SetOverlayEffect;
import cutscenes.effects.WaitEffect;
import game_events.EventHandler;
import gamestates.Gamestate;
import main_classes.Game;
import ui.ITextboxManager;

public class CutsceneManagerFly extends DefaultCutsceneManager {

   public CutsceneManagerFly(Gamestate state, Game game, EventHandler eventHandler, ITextboxManager textboxManager) {
      super(game, eventHandler, textboxManager, state);
      this.addCutsceneEffects();
   }

   private void addCutsceneEffects() {
      this.addEffect(new WaitEffect());
      this.addEffect(new FellowShipEffect());
      this.addEffect(new SetOverlayEffect());
      this.addEffect(new FillScreenEffect());
      this.addEffect(new FadeEffect(eventHandler));
      this.addEffect(new FadeHeaderEffect());
   }

   /** Flushes out all cutscenes */
   public void clear() {
      this.clearCutscenes();
   }

}
