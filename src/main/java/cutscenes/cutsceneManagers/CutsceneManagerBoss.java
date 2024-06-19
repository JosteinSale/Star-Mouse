package cutscenes.cutsceneManagers;

import cutscenes.effects.FadeEffect;
import cutscenes.effects.FillScreenEffect;
import cutscenes.effects.ObjectMoveEffect;
import cutscenes.effects.WaitEffect;
import game_events.EventHandler;
import game_events.ObjectMoveEvent;
import gamestates.Gamestate;
import main_classes.Game;
import ui.ITextboxManager;

public class CutsceneManagerBoss extends DefaultCutsceneManager {
   private ObjectMoveEffect objectMoveEffect;

   public CutsceneManagerBoss(Game game, EventHandler eventHandler, ITextboxManager textboxManager, Gamestate state) {
      super(game, eventHandler, textboxManager, state);
      this.addCutsceneEffects();
   }

   /* OBS: drawable effects will be drawn in the order they are added */
   private void addCutsceneEffects() {
      this.objectMoveEffect = new ObjectMoveEffect();
      this.addEffect(objectMoveEffect);
      this.addEffect(new WaitEffect());
      this.addEffect(new FillScreenEffect());
      this.addEffect(new FadeEffect(this.eventHandler));
      //this.shakeEffect = new ScreenShakeEffect(this.area);
      //this.addEffect(new SetOverlayEffect());   // We might not need it?
   }

   /** Is called from the state's update-loop if the cutsceneManager is active */
   public void handleKeyBoardInputs() {
      if (game.interactIsPressed) {
         game.interactIsPressed = false;
         this.advance();
      }
   }

   /** Flushes out all cutscenes */
   public void clear() {
      this.clearCutscenes();
   }

   public void moveObject(ObjectMoveEvent evt) {
      this.objectMoveEffect.moveObject(evt);
   }

   public void clearObjects() {
      this.objectMoveEffect.reset();
   }
   
}
