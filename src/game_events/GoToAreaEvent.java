package game_events;

/** An event that is fired when the player goes to a new area */
public record GoToAreaEvent(int area, int exitedDoor) implements GeneralEvent {}
