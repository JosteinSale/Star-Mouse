package cutscenes.effects;

import static utils.Constants.Exploring.Cutscenes.FADE_IN;
import static utils.Constants.Exploring.Cutscenes.FADE_OUT;

import java.awt.Color;
import java.awt.Graphics;

import game_events.EventHandler;
import game_events.FadeEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;
import main_classes.Game;

public class FadeEffect implements UpdatableEffect, DrawableEffect, AdvancableEffect {
   private EventHandler eventHandler;
   private String fadeDirection;
   private Color color;
   private boolean standardFade;
   private boolean isActive = false;
   private boolean shouldAdvance = false;
   private int fadeSpeed;
   private int alphaFade;

   public FadeEffect(EventHandler eventHandler) {
      this.eventHandler = eventHandler;
   }

   @Override
   public void activate(GeneralEvent evt) {
      FadeEvent fadeEvt = (FadeEvent) evt;
      this.fadeDirection = fadeEvt.in_out();
      this.color = this.getColor(fadeEvt.color());
      this.fadeSpeed = fadeEvt.speed();
      this.standardFade = fadeEvt.standardFade();
      this.isActive = true;
      if (fadeDirection.equals(FADE_IN)) {
         alphaFade = 255;
      }
      else {
         alphaFade = 0;
      }
   }

   private Color getColor(String string) {
      switch (string) {
         case "black" : return Color.BLACK;
         case "white" : return Color.WHITE;
         default : throw new IllegalArgumentException("No color available for: " + string);
      }
   }

   @Override
   public void update() {
      if (fadeDirection.equals(FADE_OUT)) {
         updateFadeOut();
      }
      else {
         updateFadeIn();
      }
   }

   private void updateFadeOut() {
      this.alphaFade += fadeSpeed;
      if (this.alphaFade > 255) {
         alphaFade = 255;
         this.isActive = false;
         if (standardFade) {
            eventHandler.triggerEvents();
         } else {
            this.shouldAdvance = true;
         }
      }
   }

   private void updateFadeIn() {
      this.alphaFade -= fadeSpeed;
      if (this.alphaFade < 0) {
         alphaFade = 0;
         this.isActive = false;
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
   public void draw(Graphics g) {
      g.setColor(
         new Color(color.getRed(), color.getGreen(), color.getBlue(), alphaFade)
         );
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new FadeEvent(FADE_IN, null, 0, false);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true;  // All gamestates are supported
   }

}