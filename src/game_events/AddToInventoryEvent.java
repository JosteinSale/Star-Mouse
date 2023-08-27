package game_events;

/** In Exploring/Flying: An event that adds an item to the inventory */
public record AddToInventoryEvent (String name, String description, String imgFileName) implements GeneralEvent {}
