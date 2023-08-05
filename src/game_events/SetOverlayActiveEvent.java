package game_events;

/** Sets the active status of the overlay image */
public record SetOverlayActiveEvent(boolean active) implements GeneralEvent {}
