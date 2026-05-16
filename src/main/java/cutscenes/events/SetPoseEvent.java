package cutscenes.events;

/**
 * In Exploring: this event sets the pose for an entity
 * to a specified row/col-index
 */
public record SetPoseEvent(
      String entity, boolean poseActive, int colIndex, int rowIndex) implements GeneralEvent {
};
