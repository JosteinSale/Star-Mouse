package game_events;

/** In Flying: this event triggers x amount of fellow ship following player at the 
 * start of the level. TakeOffTimer decides how long until the ship takes off.
 */
public record FellowShipEvent(int[] xPos, int[] yPos, int[] takeOffTimer) implements GeneralEvent {}
