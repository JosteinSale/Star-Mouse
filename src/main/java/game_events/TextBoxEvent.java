package game_events;

/** An interface for textBox-events. They are handled separately from regular effects.
 * If they require interaction with the player, the CutsceneManager will handle
 * this in the handleKeyboardInputs-method. A textBoxManager will handle the rest.
 * */
public interface TextBoxEvent {}
