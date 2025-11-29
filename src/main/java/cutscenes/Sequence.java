package cutscenes;

import java.util.ArrayList;

import game_events.GeneralEvent;

/** A simple class which represents a single sequence within a cutscene */
public class Sequence {
   public ArrayList<GeneralEvent> events;

   public Sequence() {
      this.events = new ArrayList<>();
   }

   public void addEvent(GeneralEvent event) {
      this.events.add(event);
   }
}
