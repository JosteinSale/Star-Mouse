package game_events;

/** An event that causes the screen to darken a given amount (= alpha) */
public record DarkenEvent(int alpha, boolean active) implements GeneralEvent {}
