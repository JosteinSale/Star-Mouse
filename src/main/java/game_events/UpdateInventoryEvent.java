package game_events;

/** Can alter the amount of credits, 
 * bombs and ship health in player inventory.
 * Can be called from a cutscene, or whenever a player transitions
 * between levels / states.
 */
public record UpdateInventoryEvent(String type, int amount) implements GeneralEvent {}
