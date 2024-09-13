package cutscenes.cutsceneManagers;

import static utils.Constants.Exploring.Cutscenes.AUTOMATIC;

import java.util.ArrayList;

import cutscenes.Cutscene;
import cutscenes.effects.DarkenScreenEffect;
import cutscenes.effects.FadeEffect;
import cutscenes.effects.FadeHeaderEffect;
import cutscenes.effects.FillScreenEffect;
import cutscenes.effects.ObjectMoveEffect;
import cutscenes.effects.RedLightEffect;
import cutscenes.effects.SetOverlayEffect;
import cutscenes.effects.WaitEffect;
import game_events.EventHandler;
import game_events.ObjectMoveEvent;
import gamestates.Gamestate;
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
      this.addEffect(new DarkenScreenEffect());
      this.addEffect(new FillScreenEffect());
      this.addEffect(new FadeEffect(this.eventHandler, this));
      this.addEffect(new FadeHeaderEffect());
   }

   @Override
   public void addCutscene(ArrayList<Cutscene> cutscenesForElement) {
      // We always put cinematic cutscenes in the AUTOMATIC-list. Check this:
      for (Cutscene c : cutscenesForElement) {
         if (c.getTrigger() != AUTOMATIC) {
            throw new IllegalArgumentException("Only use AUTOMATIC as triggerobject for cinematic cutscenes");
         }
      }
      this.automaticCutscenes.add(cutscenesForElement);
   }

   /** Will be called in the update-loop from Cinematic */
   public void handleKeyBoardInputs() {
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
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
