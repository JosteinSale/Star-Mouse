package cutscenes.events;

/** An event that plays a specific SFX */
public record PlaySFXEvent(String sfxId) implements GeneralEvent {
}
