package entities.exploring;

import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;

import utils.Constants.Direction;

public class Door extends InteractableObject {
    private int areaItLeadsTo;
    private Direction reenterDir;
    private ArrayList<Boolean> unlockRequirements;
    private int startCutscene = 0;

    public Door(Float hitbox, int nrOfRequirements, String name, int areaItLeadsTo, Direction reenterDir) {
        super(hitbox, name);
        this.unlockRequirements = new ArrayList<>();
        for (int i = 0; i < nrOfRequirements; i++) {
            unlockRequirements.add(false);
        }
        this.areaItLeadsTo = areaItLeadsTo;
        this.reenterDir = reenterDir;
    }

    public int getAreaItLeadsTo() {
        return this.areaItLeadsTo;
    }

    public boolean areRequirementsMet() {
        boolean returnvalue = true;
        for (Boolean bool : unlockRequirements) {
            if (bool == false) {
                returnvalue = false;
            }
        }
        return returnvalue;
    }

    public void setRequirementMet(int index) {
        this.unlockRequirements.set(index, true);
    }

    public Direction getReenterDir() {
        return this.reenterDir;
    }

    public void setStartCutscene(int index) {
        this.startCutscene = index;
    }

    public int getStartCutscene() {
        return this.startCutscene;
    }

}
