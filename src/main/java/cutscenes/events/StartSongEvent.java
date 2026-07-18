package cutscenes.events;

/** In Exploring/Flying: Starts the song with the corresponding identifier. */
public record StartSongEvent(String audioId, boolean shouldLoop) implements GeneralEvent {
}
