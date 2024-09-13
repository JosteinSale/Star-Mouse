package cutscenes.effects;

import game_events.GeneralEvent;
import game_events.SetRedLightEvent;
import gamestates.Gamestate;

public class RedLightEffect implements UpdatableEffect, DrawableEffect {
   private boolean active;
   public int alpha = 0;
   private int redLightDir = 1; // 1 = opp, -1 = ned

   @Override
   public void activate(GeneralEvent evt) {
      SetRedLightEvent redEvt = (SetRedLightEvent) evt;
      this.active = redEvt.active();
      if (active == true) {
         this.active = true;
      } else {
         this.active = false;
      }
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new SetRedLightEvent(false);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return true; // Supports all gamestates
   }

   @Override
   public void update() {
      alpha += (2 * redLightDir);
      if (alpha < 0) {
         alpha = 0;
         redLightDir *= -1;
      } else if (alpha > 100) {
         alpha = 100;
         redLightDir *= -1;
      }
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public void reset() {
      this.alpha = 0;
      this.active = false;
   }
}
