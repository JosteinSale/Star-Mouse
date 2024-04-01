package game_events;

/** In Exploring: this event triggers a red light that fades on and off, 
 * until the effect is turned off. */
public record SetRedLightEvent(boolean active) implements GeneralEvent {};
