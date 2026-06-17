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
   private String fadeDirection;
   private boolean shouldAdvance = false;

   public FadeEffect() {
      this.fader = new Fader();
   }

   @Override
   public void activate(GeneralEvent evt) {
      FadeEvent fadeEvt = (FadeEvent) evt;
      this.fadeDirection = fadeEvt.in_out();
      if (fadeDirection.equals(FADE_TO)) {
         fader.fadeTo(
               fadeEvt.color(),
               Fader.FadeSpeedByInt(fadeEvt.speed()),
               () -> shouldAdvance = true);
      } else {
         fader.fadeFrom(
               fadeEvt.color(),
               Fader.FadeSpeedByInt(fadeEvt.speed()),
               () -> shouldAdvance = true);
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
