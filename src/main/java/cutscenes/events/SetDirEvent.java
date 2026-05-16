package cutscenes.events;

import utils.Constants.Direction;

/** In Exploring: Sets an entity's direction */
public record SetDirEvent(String entityName, Direction dir) implements GeneralEvent {
}
