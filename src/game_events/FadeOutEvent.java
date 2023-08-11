package game_events;

/** An event that triggers a fade out to black */
public record FadeOutEvent(int speed) implements GeneralEvent {}
