package cutscenes.effects;

import java.awt.Graphics;

import game_events.FadeHeaderEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;
import main_classes.Game;
import utils.LoadSave;

import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

/** Must be used in combination with some other event to advance, 
 * like wait or screenFade. 
 * This is because we want the text to stay visible (= active) 
 * while we also activate other effects. */
public class FadeHeaderEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   private String headerText;
   private Font headerFont;
   private Rectangle headerBox;
   private int alphaFade = 0;
   private int headerFadeSpeed = 10;

   public FadeHeaderEffect() {
      this.headerBox = new Rectangle(  // Making a default rectangle
         0, 
         (int) (300 * Game.SCALE), 
         Game.GAME_WIDTH,  
         (int) (100 * Game.SCALE)
         );
      this.headerFont = LoadSave.getHeaderFont();
   }

   @Override
   public void activate(GeneralEvent evt) {
      FadeHeaderEvent headerEvt = (FadeHeaderEvent) evt;
      this.active = true;
      this.headerText = headerEvt.text();
      this.headerBox.y = (int) (headerEvt.yPos() * Game.SCALE);
      if (headerEvt.inOut().equals("in")) {
         this.headerFadeSpeed = headerEvt.fadeSpeed();
         this.alphaFade = 0;
      }
      else if (headerEvt.inOut().equals("out")) {
         this.headerFadeSpeed = -headerEvt.fadeSpeed();
      }
      
   }
   @Override
   public GeneralEvent getAssociatedEvent() {
      return (new FadeHeaderEvent("in", 0, 0, "gey"));
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.FLYING);
   }

   @Override
   public void update() {
      this.alphaFade += headerFadeSpeed;
      if (alphaFade > 255) {
         // The header is faded in. We want to still draw it, so it stays active
         alphaFade = 255;
      }
      else if (alphaFade < 0) {
         // The header is faded out, we inactivate the effect
         alphaFade = 0;
         active = false;
      }
   }

   @Override
   public void draw(Graphics g) {
      g.setColor(new Color(255, 255, 255, alphaFade));
      DrawCenteredString(g, headerText, headerBox, headerFont);
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void automaticReset() {
      /* Do nothing */
   }

   @Override
   public boolean shouldAdvance() {
      return false;   // Another effect must advance the cutscene.
   }

}
