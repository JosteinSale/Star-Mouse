package cutscenes.effects;

import java.awt.Color;
import java.awt.Graphics;

import game_events.DarkenEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;
import main_classes.Game;

public class DarkenScreenEffect implements DrawableEffect {
   private boolean active;
   private Color color;
   public int alpha;

   @Override
   public void activate(GeneralEvent evt) {
      DarkenEvent darkEvt = (DarkenEvent) evt;
      this.active = darkEvt.active();
      this.alpha = darkEvt.alpha();
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new DarkenEvent(0, true);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // Supports all gamestates
   }

   @Override
   public void reset() {
      this.active = false;
   }

   @Override
   public boolean isActive() {
      return active;
   }

}
