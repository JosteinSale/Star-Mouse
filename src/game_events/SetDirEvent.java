package game_events;

/** In Exploring: Sets an entity's direction */
public record SetDirEvent (String entityName, int dir) implements GeneralEvent {}
