package game_events;

/** When this event is fired, a timer starts. When the timer is finished, 
 * the 'advance'-method in CutsceneManager is called.
 */
public record WaitEvent (int waitFrames) implements GeneralEvent {}
