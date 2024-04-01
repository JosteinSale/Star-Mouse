package game_events;

/** In Exploring/Flying: An event that sets the active status of a black screen */
public record BlackScreenEvent (boolean active) implements GeneralEvent {}
