package game_events;

/** Fades out the currently playing song and ambience, 
 * and then stops and resets them */
public record FadeOutLoopEvent() implements GeneralEvent {}
