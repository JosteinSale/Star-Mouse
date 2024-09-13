package cutscenes.effects;

import static utils.Constants.Exploring.Cutscenes.FADE_IN;
import static utils.Constants.Exploring.Cutscenes.FADE_OUT;

import cutscenes.cutsceneManagers.CutsceneManagerExp;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import game_events.EventHandler;
import game_events.FadeEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;

/**
 * Note: in the event of standard fades, when fading out, the isActive-boolean
 * is left true. Thus it will still be drawn black, to cover for premature
 * resetting before drawing is complete in fullscreen-mode.
 * The effect is properly reset the next time the player enters the area.
 */
public class FadeEffect implements UpdatableEffect, DrawableEffect, AdvancableEffect {
   private EventHandler eventHandler;
   private CutsceneManagerExp cutsceneManager;
   private String fadeDirection;
   public String color;
   private boolean standardFade;
   private boolean isActive = false;
   private boolean shouldAdvance = false;
   private int fadeSpeed;
   public int alphaFade;

   /**
    * Takes the cutsceneManager as an argument, in case of EXPLORING -> sets
    * cutsceneManager.ative to false after a standardFade.
    */
   public FadeEffect(EventHandler eventHandler, DefaultCutsceneManager cutsceneManager) {
      this.eventHandler = eventHandler;
      if (cutsceneManager instanceof CutsceneManagerExp cm) {
         this.cutsceneManager = cm;
      }
   }

   @Override
   public void activate(GeneralEvent evt) {
      FadeEvent fadeEvt = (FadeEvent) evt;
      this.fadeDirection = fadeEvt.in_out();
      this.color = fadeEvt.color();
      this.fadeSpeed = fadeEvt.speed();
      this.standardFade = fadeEvt.standardFade();
      this.isActive = true;
      if (fadeDirection.equals(FADE_IN)) {
         alphaFade = 255;
      } else {
         alphaFade = 0;
      }
   }

   @Override
   public void update() {
      if (fadeDirection.equals(FADE_OUT)) {
         updateFadeOut();
      } else {
         updateFadeIn();
      }
   }

   private void updateFadeOut() {
      this.alphaFade += fadeSpeed;
      if (this.alphaFade > 255) {
         alphaFade = 255;
         if (standardFade) {
            eventHandler.triggerEvents();
            cutsceneManager.setActive(false);
         } else {
            this.isActive = false;
            this.shouldAdvance = true;
         }
      }
   }

   private void updateFadeIn() {
      this.alphaFade -= fadeSpeed;
      if (this.alphaFade < 0) {
         alphaFade = 0;
         this.isActive = false;
         this.cutsceneManager.setActive(false);
         if (!standardFade) {
            this.shouldAdvance = true;
         }
      }
   }

   @Override
   public boolean isActive() {
      return this.isActive;
   }

   public boolean isStandardFadeActive() {
      return (this.isActive && this.standardFade);
   }

   @Override
   public boolean shouldAdvance() {
      return this.shouldAdvance;
   }

   @Override
   public void reset() {
      this.shouldAdvance = false;
      this.isActive = false;
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new FadeEvent(FADE_IN, null, 0, false);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // All gamestates are supported
   }

}
