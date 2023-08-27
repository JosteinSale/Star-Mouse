package game_events;

/** In Exploring/Flying: An event that triggers a fade out to black */
public record FadeOutEvent(int speed) implements GeneralEvent {}
