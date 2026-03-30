package cutscenes.events;

/** In Exploring: An event that sets the current player spriteSheet */
public record SetPlayerSheetEvent(int sheetIndex) implements GeneralEvent {
}
