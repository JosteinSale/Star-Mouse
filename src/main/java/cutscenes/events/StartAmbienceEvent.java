package cutscenes.events;

/** In Exploring/Flying: Starts the ambience track with the given identifier. */
public record StartAmbienceEvent(String ambienceId) implements GeneralEvent {
}
