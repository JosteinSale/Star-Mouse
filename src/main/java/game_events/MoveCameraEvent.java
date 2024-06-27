package game_events;

/** An event that will deattatch the camera and move it a certain amount of pixels. */
public record MoveCameraEvent(int deltaX, int deltaY, int duration) implements GeneralEvent {}
