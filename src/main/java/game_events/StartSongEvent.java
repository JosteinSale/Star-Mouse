package game_events;

/** In Exploring/Flying: Starts the song with the corresponding index */
public record StartSongEvent(int index, boolean shouldLoop) implements GeneralEvent {}
