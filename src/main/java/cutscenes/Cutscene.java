package cutscenes;

import java.util.ArrayList;

/**
 * Cutscenes are essentially sequences of events.
 * Each cutscene can have one or more sequences, which are stored in ArrayLists.
 * Each sequence can have one or more events, which are stored in ArrayLists.
 * 
 * To advance a cutscene, a sequence is drawn from the cutscene and added
 * to the eventhandler. There, all events will be triggered at once.
 * Events must implement the GeneralEvent interface.
 * 
 * Trigger-variable: the element which triggers the cutscene
 * (objects / doors / npc's / automatic triggerboxes).
 */
public class Cutscene {
   private ArrayList<Sequence> sequences;
   private int curSequence = 0;
   private String entityName; // E.g. "door2" or "npc5"
   private boolean canReset = true;
   private boolean hasPlayed = false;

   public Cutscene() {
      this.sequences = new ArrayList<>();
   }

   public void setCanReset(boolean canReset) {
      this.canReset = canReset;
   }

   public void setName(String triggeringEntity) {
      this.entityName = triggeringEntity;
   }

   public void addSequence(Sequence sequence) {
      this.sequences.add(sequence);
   }

   public boolean hasMoreSequences() {
      int finalSequence = sequences.size() - 1;
      return curSequence < finalSequence;
   }

   public Sequence getNextSequence() {
      curSequence += 1;
      return sequences.get(curSequence);
   }

   public Sequence getFirstSequence() {
      return sequences.get(0);
   }

   public boolean canReset() {
      return this.canReset;
   }

   public void reset() {
      this.curSequence = 0;
   }

   public boolean hasPlayed() {
      return this.hasPlayed;
   }

   public void setPlayed() {
      this.hasPlayed = true;
   }

   public String getEntityName() {
      return this.entityName;
   }

   public Sequence getSequence(int i) {
      return this.sequences.get(i);
   }
}
