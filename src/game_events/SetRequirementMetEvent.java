package game_events;

/** In Exploring: An event that sets a particular requirement met for a particular door */
public record SetRequirementMetEvent (int doorIndex, int requirementIndex) implements GeneralEvent {}
