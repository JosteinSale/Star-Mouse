package utils.parsing;

import cutscenes.events.GeneralEvent;

/** An interface for a parser for a single GeneralEvent */
public interface EventParser {
   GeneralEvent parseEvent(String[] lineData);
}
