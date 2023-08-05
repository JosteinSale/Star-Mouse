package game_events;

/** An event that triggers a fade in from black */
public record FadeInEvent(int speed) implements GeneralEvent {}
