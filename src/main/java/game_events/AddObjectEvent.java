package game_events;

/** Can be used to add a SimpleAnimation-object on screen, that can later be moved */
public record AddObjectEvent(
   String objectName, String identifier, float xPos, float yPos, float scaleW, float scaleH, int aniSpeed) 
      implements GeneralEvent {}
