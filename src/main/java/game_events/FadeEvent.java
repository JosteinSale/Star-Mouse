package game_events;

/** In Exploring/Flying: An event that triggers a fade out to black */
public record FadeEvent(String in_out, int speed, boolean standardFade) implements GeneralEvent {}
