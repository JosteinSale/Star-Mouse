package game_events;

/** Can be used to add a SimpleAnimation-object on screen, that can later be moved */
public record AddObjectEvent(
   String objectName, float xPos, float yPos) implements GeneralEvent {}
