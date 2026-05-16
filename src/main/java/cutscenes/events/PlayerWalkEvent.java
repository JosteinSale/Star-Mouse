package cutscenes.events;

import utils.Constants.Direction;

/**
 * In Exploring: An event that causes the player to walk to the target
 * coordinate,
 * in the span of a set amount of frames.
 */
public record PlayerWalkEvent(Direction direction, float targetX, float targetY, int walkDuration)
            implements GeneralEvent {
}
