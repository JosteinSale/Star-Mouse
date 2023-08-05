package game_events;

/** An event that fades in/out a specific header text at a specific y-pos. */
public record FadeHeaderEvent(String inOut, int yPos, int fadeSpeed, String header) implements GeneralEvent {}
