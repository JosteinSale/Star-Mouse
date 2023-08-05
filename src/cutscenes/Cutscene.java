package cutscenes;

import java.util.ArrayList;

import game_events.GeneralEvent;
import static utils.Constants.Exploring.Cutscenes.*;

/**
 * Cutscenes are essentially sequences of events.
 * Each cutscene can have one or more sequences, which are stored in ArrayLists.
 * Each sequence can have one or more events, which are stored in ArrayLists.
 * 
 * To advance a cutscene, a sequcne is drawn from the cutscene and added
 * to the eventhandler. There, all events will be triggered at once.
 * Events must implement the GeneralEvent interface.
 * 
 * Trigger-variable: the element which triggers the cutscene 
 * (objects / doors / npc's / automatic triggerboxes).
 */
public class Cutscene {
    private ArrayList<ArrayList<GeneralEvent>> sequences;
    private int sequenceIndex = 0;
    private int trigger;
    private boolean canReset = true;
    private boolean hasPlayed = false;

    public Cutscene() {
        this.sequences = new ArrayList<>();
    }

    public void setCanReset(boolean canReset) {
        this.canReset = canReset;
    }

    public void setTrigger(int triggerObject) {
        this.trigger = triggerObject;
    }

    public void addSequence(ArrayList<GeneralEvent> sequence) {
        this.sequences.add(sequence);
    }

    public boolean hasMoreSequences() {
        return sequenceIndex < (sequences.size() - 1);
    }

    public ArrayList<GeneralEvent> getNextSequence() {
        sequenceIndex += 1;
        return sequences.get(sequenceIndex);
    }

    public ArrayList<GeneralEvent> getFirstSequence() {
        return sequences.get(0);
    }

    public boolean canReset() {
        return this.canReset;
    }

    public void reset() {
        this.sequenceIndex = 0;
    }

    public boolean hasPlayed() {
        return this.hasPlayed;
    }

    public void setPlayed() {
        this.hasPlayed = true;
    }

    public int getTrigger() {
        return this.trigger;
    }
}

