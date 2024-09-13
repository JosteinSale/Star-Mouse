package cutscenes.effects;

import game_events.GeneralEvent;
import game_events.NumberDisplayEvent;
import gamestates.Gamestate;
import ui.NumberDisplay;

/**
 * Displays a numberDisplay. The display handles keyboardInputs from the player
 * to modify the numbers on the display. The acts of checking the correct code,
 * jumping to the corresponding cutscene, and resetting the display
 * is handled in the cutsceneManager.
 */
public class NumberDisplayEffect implements UpdatableEffect, DrawableEffect {
   private NumberDisplay numberDisplay;

   public NumberDisplayEffect(NumberDisplay numberDisplay) {
      this.numberDisplay = numberDisplay;
   }

   @Override
   public void activate(GeneralEvent evt) {
      NumberDisplayEvent nrEvt = (NumberDisplayEvent) evt;
      this.numberDisplay.start(nrEvt.passCode());

   }

   @Override
   public GeneralEvent getAssociatedEvent() {
      return new NumberDisplayEvent(null);
   }

   @Override
   public boolean supportsGamestate(Gamestate state) {
      return (state == Gamestate.EXPLORING);
   }

   @Override
   public void update() {
      this.numberDisplay.update();
   }

   @Override
   public boolean isActive() {
      return numberDisplay.isActive();
   }

   @Override
   public void reset() {
      // Resetting is handled in cutsceneManager :: handleNumberDisplayInputs().
   }

}
