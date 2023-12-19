package game_events;

/** In Exploring: this event makes the screen shake for a set amount of frames */
public record ScreenShakeEvent(int duration) implements GeneralEvent {}
