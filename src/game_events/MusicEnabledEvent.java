package game_events;

/** In Exploring: An event that sets the music enabled-status */
public record MusicEnabledEvent(boolean enabled) implements GeneralEvent {}
