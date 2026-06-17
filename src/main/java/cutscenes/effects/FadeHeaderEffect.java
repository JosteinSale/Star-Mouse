package cutscenes.effects;

import static utils.Constants.Exploring.Cutscenes.FADE_FROM;
import static utils.Constants.Exploring.Cutscenes.FADE_TO;

import com.badlogic.gdx.math.Rectangle;

import cutscenes.events.FadeHeaderEvent;
import cutscenes.events.GeneralEvent;
import game_states.Gamestate;
import main_classes.Game;
import rendering.MyColor;

/**
 * Must be used in combination with some other event to advance,
 * like wait or screenFade.
 * This is because we want the text to stay visible (= active)
 * while we also activate other effects.
 */
public class FadeHeaderEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   private MyColor color;
   public String headerText;
   public Rectangle headerBox;
   public int alphaFade = 0;
   private int headerFadeSpeed = 10;

   public FadeHeaderEffect() {
      this.headerBox = new Rectangle(0, 300, Game.GAME_DEFAULT_WIDTH, 100);
   }

   @Override
   public void activate(GeneralEvent evt) {
      FadeHeaderEvent headerEvt = (FadeHeaderEvent) evt;
      this.active = true;
      this.color = headerEvt.color();
      this.headerText = headerEvt.text();
      this.headerBox.y = headerEvt.yPos();
      if (headerEvt.fadeDir().equals(FADE_FROM)) {
         this.headerFadeSpeed = -headerEvt.fadeSpeed();
         this.alphaFade = 255;
      } else if (headerEvt.fadeDir().equals(FADE_TO)) {
         this.headerFadeSpeed = headerEvt.fadeSpeed();
         this.alphaFade = 0;
      }
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return (new FadeHeaderEvent(FADE_FROM, MyColor.WHITE, 0, 0, "gey"));
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

   public MyColor getColor() {
      return MyColor.getColorWithAlpha(color, alphaFade);
   }
}
