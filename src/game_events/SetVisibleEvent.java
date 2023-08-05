package game_events;

/**When this event is fired, it sets the player's visibility boolean */
public record SetVisibleEvent(boolean visible) implements GeneralEvent {}
