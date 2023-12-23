package game_events;

/** In Exploring: An event that causes the player to walk to the target coordinate, 
 * in the span of a set amount of frames.
*/
public record PlayerWalkEvent (int sheetRowIndex, float targetX, float targetY, int framesDuration) 
      implements GeneralEvent {}
