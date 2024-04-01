package game_events;

/** In Exploring: When this event is fired, it displays a NumberDisplay and sets the given passcode */
public record NumberDisplayEvent (int[] passCode) implements GeneralEvent {}
