package game_events;

import java.util.ArrayList;

/**
 * Contains a single listener, which is a method that can react to all events.
 * Contains a list of GeneralEvents, that can be stored and triggered 
 * in response to a specific event.
 * All events are cleared out after they are triggered.
 */
public class EventHandler {
    private EventListener listener;
    private ArrayList<GeneralEvent> events;

    public EventHandler() {
        this.events = new ArrayList<>();
    }

    public void addEventListener(EventListener listener) {
        this.listener = listener;
    }

    public void addEvent(GeneralEvent event) {
        this.events.add(event);
    }

    public void triggerEvents() {
        for (GeneralEvent event : events) {
            listener.onEventOccurred(event);
        }
        events.clear();
    }
}
