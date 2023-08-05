package game_events;

/** An event that causes the player to walk to the target coordinate, 
 * in the span of a set amount of frames.
*/
public record PlayerWalkEvent (float targetX, float targetY, int framesDuration) implements GeneralEvent {}
