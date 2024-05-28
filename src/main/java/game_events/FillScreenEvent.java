package game_events;

/** In Exploring/Flying: An event that sets active an effect that
 *  the fills the screen with a given color */
public record FillScreenEvent (String color, boolean active) implements GeneralEvent {}
