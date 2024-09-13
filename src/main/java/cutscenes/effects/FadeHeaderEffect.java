package cutscenes.effects;

import game_events.FadeHeaderEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;
import main_classes.Game;

import java.awt.Rectangle;

/**
 * Must be used in combination with some other event to advance,
 * like wait or screenFade.
 * This is because we want the text to stay visible (= active)
 * while we also activate other effects.
 */
public class FadeHeaderEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   public String headerText;
   public Rectangle headerBox;
   public int alphaFade = 0;
   private int headerFadeSpeed = 10;

   public FadeHeaderEffect() {
      this.headerBox = new Rectangle( // Making a default rectangle
            0,
            (int) (300 * Game.SCALE),
            Game.GAME_WIDTH,
            (int) (100 * Game.SCALE));
   }

   @Override
   public void activate(GeneralEvent evt) {
      FadeHeaderEvent headerEvt = (FadeHeaderEvent) evt;
      this.active = true;
      if (headerEvt.inOut().equals("in")) {
         this.headerBox.y = (int) (headerEvt.yPos() * Game.SCALE);
         this.headerText = headerEvt.text();
         this.headerFadeSpeed = headerEvt.fadeSpeed();
         this.alphaFade = 0;
      } else if (headerEvt.inOut().equals("out")) {
         this.headerFadeSpeed = -headerEvt.fadeSpeed();
      }

   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return (new FadeHeaderEvent("in", 0, 0, "gey"));
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // Supports all gamestates
   }

   @Override
   public void update() {
      this.alphaFade += headerFadeSpeed;
      if (alphaFade > 255) {
         // The header is faded in. We want to still draw it, so it stays active
         alphaFade = 255;
      } else if (alphaFade < 0) {
         // The header is faded out, we inactivate the effect
         alphaFade = 0;
         active = false;
      }
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void reset() {
      this.active = false;
   }
}
