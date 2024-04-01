package game_events;

/** In Exploring: this event sets the sprite for an entity 
 * to a specified row/col-index */
public record SetSpriteEvent(
   String entity, boolean poseActive, int colIndex, int rowIndex) implements GeneralEvent {};
