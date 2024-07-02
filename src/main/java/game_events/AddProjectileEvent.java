package game_events;

/** An event that can be used to add a projectile in flying / bossMode */
public record AddProjectileEvent(
   int type, int xPos, int yPos, int xSpeed, int ySpeed) implements GeneralEvent {}
