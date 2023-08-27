package game_events;

/** In Exploring/Flying: Sets the active status of the overlay image */
public record SetOverlayActiveEvent(boolean active) implements GeneralEvent {}
