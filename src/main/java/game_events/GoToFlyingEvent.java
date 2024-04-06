package game_events;

/** If the event is fired from an Area, it loads a new flying-level and 
 * handles the transition to that level.
 * If the event is fired from bossMode, it assumes a level has already
 * been loaded and we want to return to that level. Thus it disregards the
 * lvl-int, and only handles the transition back to Flying-mode.
*/
public record GoToFlyingEvent(int lvl) implements GeneralEvent {}
