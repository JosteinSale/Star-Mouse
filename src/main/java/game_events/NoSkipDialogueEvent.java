package game_events;

/** An event that triggers unskippable dialogue, i.e player must wait until the dialogue
 * has finished appearing before they can advance with INTERACT. */
public record NoSkipDialogueEvent (
   String name, int speed, String text, int portraitIndex) 
      implements GeneralEvent, TextBoxEvent {}
