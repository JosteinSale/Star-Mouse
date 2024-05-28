package cutscenes.effects;

import game_events.GeneralEvent;
import game_events.WaitEvent;
import gamestates.Gamestate;

public class WaitEffect implements UpdatableEffect {
   private int waitDuration;
   private int tick;
   private boolean isActive;
   private boolean shouldAdvance;

   @Override
   public void activate(GeneralEvent evt) {
      WaitEvent waitEvt = (WaitEvent) evt;
      this.waitDuration = waitEvt.waitFrames();
      this.tick = 0;
      this.isActive = true;
   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new WaitEvent(0);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.EXPLORING || state == Gamestate.FLYING);
   }

   @Override
   public void update() {
      this.tick++;
      if (tick >= waitDuration) {
         this.isActive = false;
         this.shouldAdvance = true;
      }
   }

   @Override
   public boolean isActive() {
      return this.isActive;
   }

   @Override
   public void automaticReset() {
      this.shouldAdvance = false;
   }

   @Override
   public boolean shouldAdvance() {
      return this.shouldAdvance;
   }
   
}
