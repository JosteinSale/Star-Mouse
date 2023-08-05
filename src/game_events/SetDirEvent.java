package game_events;

/** Sets an entity's direction */
public record SetDirEvent (String entityName, int dir) implements GeneralEvent {}
