package cutscenes.effects;

import game_events.FillScreenEvent;
import game_events.GeneralEvent;
import gamestates.Gamestate;

/** Fills the screen with a given color */
public class FillScreenEffect implements DrawableEffect {
   private boolean active;
   public String color;

   @Override
   public void activate(GeneralEvent evt) {
      FillScreenEvent fillEvt = (FillScreenEvent) evt;
      this.active = fillEvt.active();
      if (active) {
         this.color = fillEvt.color();
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
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void reset() {
      this.active = false;
   }

}
