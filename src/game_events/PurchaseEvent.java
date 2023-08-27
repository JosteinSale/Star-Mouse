package game_events;

/** In Exploring: An event that checks if the player has the required amount of credits in their inventory.
 * If yes, cutscene +1 is started. Else, cutscene +2 is started.
 */
public record PurchaseEvent(int credits) implements GeneralEvent {}
