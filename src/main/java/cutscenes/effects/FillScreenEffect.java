package cutscenes.effects;

import java.awt.Color;
import java.awt.Graphics;

import game_events.FillScreenEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;
import main_classes.Game;

/** Fills the screen with a given color */
public class FillScreenEffect implements DrawableEffect {
   private boolean active;
   private Color fillColor;

   @Override
   public void activate(GeneralEvent evt) {
      FillScreenEvent fillEvt = (FillScreenEvent) evt;
      this.active = fillEvt.active();
      if (active) {
         this.fillColor = this.setColor(fillEvt.color());
      }
   }

   private Color setColor(String color) {
      switch (color) {
         case "black" : return Color.BLACK;
         case "white" : return Color.WHITE;
         default : throw new IllegalArgumentException("color " + color + "is not supported");
      }
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new FillScreenEvent(null, active);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // All gamestates are supported
   }

   @Override
   public void draw(Graphics g) {
      g.setColor(fillColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
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
