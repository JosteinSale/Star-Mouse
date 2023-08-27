package game_events;

/** In Exploring: An event that is fired in exploring-mode, 
 * in order to transition to a specific level in flying mode. */
public record GoToFlyingEvent(int lvl) implements GeneralEvent {}
