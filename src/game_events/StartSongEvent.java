package game_events;

/** Starts the song with the corresponding index */
public record StartSongEvent(int index) implements GeneralEvent {}
