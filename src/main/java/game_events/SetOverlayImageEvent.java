package game_events;

/** In Exploring/Flying: Sets the filename of the overlay image */
public record SetOverlayImageEvent(
   boolean active, String fileName, Float scaleW, float ScaleH) implements GeneralEvent {}
