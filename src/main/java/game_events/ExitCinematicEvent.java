package game_events;

/** An event that triggers the exit from cinematic, into the gamestate
 * given at the start of the cutscene.
 */
public record ExitCinematicEvent() implements GeneralEvent {}
