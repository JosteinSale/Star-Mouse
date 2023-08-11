package game_events;

/** An event that is fired in exploring-mode, 
 * in order to transition to a specific level in flying mode. */
public record GoToFlyingEvent(int lvl) implements GeneralEvent {}
