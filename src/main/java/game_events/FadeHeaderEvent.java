package game_events;

/** In Flying: An event that fades in/out a specific header text at a specific y-pos. */
public record FadeHeaderEvent(String inOut, int yPos, int fadeSpeed, String text) implements GeneralEvent {}
