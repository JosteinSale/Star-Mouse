package cutscenes.events;

import game_states.Gamestate;

/**
 * An event that triggers a transition to cinematic, and starts the given
 * cutscene.
 * It returns to the given gamestate after the cutscene is done.
 */
public record StartCinematicEvent(
      String fileName, Gamestate returnGamestate) implements GeneralEvent {
}
