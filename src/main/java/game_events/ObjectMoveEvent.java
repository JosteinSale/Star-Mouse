package game_events;

/** Can be used to move existing objects, using the ObjectMoveEffect */
public record ObjectMoveEvent(
   String identifier, int targetX, int targetY, int duration) implements GeneralEvent {}
