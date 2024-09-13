package entities.exploring;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class NpcManager {
    public ArrayList<NPC> allNpcs = new ArrayList<>();
    ArrayList<Rectangle2D.Float> hitboxes = new ArrayList<>();

    public NpcManager() {
    }

    public void addNpc(NPC npc) {
        allNpcs.add(npc);
        hitboxes.add(npc.getHitbox());
    }

    public void update() {
        for (NPC npc : allNpcs) {
            npc.update();
        }
    }

    public ArrayList<Rectangle2D.Float> getHitboxes() {
        return this.hitboxes;
    }

    public void setNewStartingCutscene(int npcNr, int cutsceneIndex) {
        allNpcs.get(npcNr).setNewStartingCutscene(cutsceneIndex);
    }

    public void setNpcDir(String name, int dir) {
        for (NPC npc : allNpcs) {
            if (npc.getName().equals(name)) {
                npc.setDir(dir);
                return;
            }
        }
    }

    public void setSprite(String name, boolean poseActive, int colIndex, int rowIndex) {
        for (NPC npc : allNpcs) {
            if (npc.getName().equals(name)) {
                npc.setSprite(poseActive, colIndex, rowIndex);
                return;
            }
        }
    }

    public void adjustNpcPos(String name, float deltaX, float deltaY) {
        for (NPC npc : allNpcs) {
            if (npc.getName().equals(name)) {
                npc.adjustPos(deltaX, deltaY);
                return;
            }
        }
    }

    /** Returns the amount of NPC's */
    public int getAmount() {
        return this.allNpcs.size();
    }

    public NPC getNpc(int index) {
        return allNpcs.get(index);
    }
}
