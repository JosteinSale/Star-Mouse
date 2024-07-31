package game_events;

/** In Exploring:An event that is fired when the player goes to a new area */
public record GoToAreaEvent(int area, int reenterDir) implements GeneralEvent {
}
