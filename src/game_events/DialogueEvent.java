package game_events;

/** In Exploring/Flying: An event that is triggered when a character has dialogue */
public record DialogueEvent (String name, int speed, String text, int portraitIndex) implements GeneralEvent {};
