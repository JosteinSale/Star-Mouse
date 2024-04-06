package game_events;

/** An event in Flying that loads a boss, and handles the transition to
 * that boss.
 */
public record GoToBossEvent (int bossNr) implements GeneralEvent {}
