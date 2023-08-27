package game_events;

/** In Flying: An event that sets the active-status of the updateGameplay-function */
public record SetGameplayEvent(boolean active) implements GeneralEvent {}
