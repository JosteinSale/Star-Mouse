package cutscenes.effects;

import static utils.Constants.Exploring.Cutscenes.FADE_FROM;
import static utils.Constants.Exploring.Cutscenes.FADE_TO;

import cutscenes.events.FadeEvent;
import cutscenes.events.GeneralEvent;
import game_states.Gamestate;
import utils.Fader;

/**
 * Note: in the event of standard fades, when fading out, the isActive-boolean
 * is left true. Thus it will still be drawn black, to cover for premature
 * resetting before drawing is complete in fullscreen-mode.
 * The effect is properly reset the next time the player enters the area.
 */
public class FadeEffect implements UpdatableEffect, DrawableEffect, AdvancableEffect {
   private Fader fader;
   private Runnable onStandardFadeToComplete;
   private Runnable onStandardFadeFromComplete;
   private String fadeDirection;
   private boolean standardFade; // TODO - I don't like standardFade >:[
   private boolean shouldAdvance = false;

   /**
    * Takes two runnables as argument:
    * 
    * @onFadeToComplete will run when the fadeTo[color] completes
    * @onFadeFromComplete will run when the fadeFrom[color] completes
    */
   public FadeEffect(Runnable onStandardFadeToComplete, Runnable onStandardFadeFromComplete) {
      this.onStandardFadeToComplete = onStandardFadeToComplete;
      this.fader = new Fader();
   }

   @Override
   public void activate(GeneralEvent evt) {
      FadeEvent fadeEvt = (FadeEvent) evt;
      this.fadeDirection = fadeEvt.in_out();
      this.standardFade = fadeEvt.standardFade();
      // This will run when a fadeTo[color] is completed
      Runnable r1 = () -> {
         if (standardFade) {
            onStandardFadeToComplete.run();
         } else {
            this.shouldAdvance = true;
         }
      };
      // This will run when a fadeFrom[color] is completed
      Runnable r2 = () -> {
         if (standardFade) {
            onStandardFadeFromComplete.run();
         } else {
            this.shouldAdvance = true;
         }
      };

      if (fadeDirection.equals(FADE_TO)) {
         fader.fadeTo(fadeEvt.color(), Fader.FadeSpeedByInt(fadeEvt.speed()), r1);
      } else {
         fader.fadeFrom(fadeEvt.color(), Fader.FadeSpeedByInt(fadeEvt.speed()), r2);
      }
   }

   @Override
   public void update() {
      fader.update();
   }

   @Override
   public boolean isActive() {
      return fader.isFading();
   }

   public boolean isStandardFadeActive() {
      return (fader.isFading() && this.standardFade);
   }

   @Override
   public boolean shouldAdvance() {
      return this.shouldAdvance;
   }

   @Override
   public void reset() {
      this.shouldAdvance = false;
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new FadeEvent(FADE_FROM, null, 0, false);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // All gamestates are supported
   }

   public Fader getFader() {
      return fader;
   }

}
