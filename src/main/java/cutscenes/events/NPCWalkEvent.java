package cutscenes.events;

import utils.Constants.Direction;

/**
 * In Exploring: An event that causes an npc to walk to the target coordinate,
 * in the span of a set amount of frames. Npc-index is found in the
 * levelData-sheet.
 */
public record NPCWalkEvent(
            int npcIndex, Direction dir, float targetX, float targetY, int walkDuration)
            implements GeneralEvent {
}
