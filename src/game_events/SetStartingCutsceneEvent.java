package game_events;

/** In Exploring/Flying: This event sets the starting cutscene 
 * for a specific element to a specific index */
public record SetStartingCutsceneEvent (int triggerObject, int elementNr, int cutsceneIndex) implements GeneralEvent {}
