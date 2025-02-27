package game_events;

/** In In Exploring: An event that is fired when an infobox is to be shown */
public record InfoBoxEvent (String text) implements GeneralEvent, TextBoxEvent {};
