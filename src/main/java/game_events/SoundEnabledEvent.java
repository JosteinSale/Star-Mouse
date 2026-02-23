package game_events;

/**
 * In Exploring: An event that sets the sound enabled-status.
 * SoundType can be "music", "sfx" or "ambience".
 */
public record SoundEnabledEvent(boolean enabled, String soundType) implements GeneralEvent {
   public static final String MUSIC = "music";
   public static final String SFX = "sfx";
   public static final String AMBIENCE = "ambience";
}
