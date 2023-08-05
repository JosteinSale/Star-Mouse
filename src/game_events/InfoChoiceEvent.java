package game_events;

/** An event that displays an infobox with two choices that the player can pick.
 * Each choice triggers a different cutscene upon selection.
  */
public record InfoChoiceEvent(String question, String leftChoice, String rightChoice) implements GeneralEvent {}
