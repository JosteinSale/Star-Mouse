package game_events;

/** An event that triggers a big dialogue box */
public record SmallDialogueEvent (
   String name, int speed, String text, int portraitIndex) 
      implements GeneralEvent, TextBoxEvent {};
