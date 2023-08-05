package game_events;

/** An event that is fired in Flying-mode, whenever the game should not update itself */
public record SetGameplayEvent(boolean active) implements GeneralEvent {}
