package game_events;

/** In Exploring/Flying: 
 * When this event is fired, a timer starts. When the timer is finished, 
 * the 'advance'-method in CutsceneManager is called.
 */
public record WaitEvent (int waitFrames) implements GeneralEvent {}
