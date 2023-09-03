package game_events;

/** An event that plays a specific SFX */
public record PlaySFXEvent(int SFXIndex) implements GeneralEvent {}
