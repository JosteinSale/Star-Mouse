package game_events;

/**
 * An event is an object that is sent to all listeners that have
 * registered with an EventBus once someone posts the event to the
 * EventBus. The event (usually) contains information about what
 * happened, and the listeners can react to this information.
 * <p>
 * An event producer creates an event object and posts it to the
 * EventBus. The created event object should be in a class which
 * implements this interface.
 * <p>
 *
 */
public interface GeneralEvent {}
