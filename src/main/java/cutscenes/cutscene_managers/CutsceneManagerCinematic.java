package cutscenes.cutscene_managers;

import cutscenes.effects.FadeEffect;
import cutscenes.effects.FadeHeaderEffect;
import cutscenes.effects.FillScreenEffect;
import cutscenes.effects.ObjectMoveEffect;
import cutscenes.effects.RedLightEffect;
import cutscenes.effects.SetOverlayEffect;
import cutscenes.effects.WaitEffect;
import cutscenes.events.EventHandler;
import cutscenes.events.ObjectMoveEvent;
import game_states.Gamestate;
import inputs.Inputs;
import main_classes.Game;
import ui.ITextboxManager;

public class CutsceneManagerCinematic extends DefaultCutsceneManager {
   private ObjectMoveEffect objectMoveEffect;

   public CutsceneManagerCinematic(Game game, EventHandler eventHandler, ITextboxManager textboxManager,
         Gamestate state) {
      super(game, eventHandler, textboxManager, state);
      this.addCutsceneEffects();
   }

   private void addCutsceneEffects() {
      this.addEffect(new WaitEffect());
      this.addEffect(new SetOverlayEffect(game)); // We will use this for drawing backgrounds
      this.addEffect(new RedLightEffect());

      // Effects that need to be accessible from the cutsceneManager:
      this.objectMoveEffect = new ObjectMoveEffect(game);
      this.addEffect(objectMoveEffect);

      // Effects that need to be on the top layer
      this.addEffect(new FillScreenEffect());
      this.addEffect(new FadeEffect(this.eventHandler, this));
      this.addEffect(new FadeHeaderEffect());
   }

   /** Will be called in the update-loop from Cinematic */
   public void handleKeyBoardInputs() {
      if (Inputs.interactIsPressed) {
         Inputs.interactIsPressed = false;
         this.advance();
      }
   }

   public void moveObject(ObjectMoveEvent evt) {
      this.objectMoveEffect.moveObject(evt);
   }

   public void clearObjects() {
      this.objectMoveEffect.reset();
   }

}
