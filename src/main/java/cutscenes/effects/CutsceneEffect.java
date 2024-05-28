package cutscenes.effects;

import game_events.GeneralEvent;
import gamestates.Gamestate;

public interface CutsceneEffect {

   /** Starts the cutscene effect */
   public void activate(GeneralEvent evt);

   /** Gets the event associated with the effect, in the form of a new object.
    * This method will be called by the cutsceneManager 
    * to extract the string-representation of the class of the GeneralEvent.
    */
   public GeneralEvent getAssociatedEvent();

   /** Returns true if the effect supports calling from the given state */
   public boolean supportsGamestate(Gamestate state);
}
