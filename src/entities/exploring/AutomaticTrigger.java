package entities.exploring;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.Entity;

public class AutomaticTrigger extends Entity {
    private String name;
    private float startY;   // for use in Flying
    private int startCutscene = 0;
    private boolean hasPlayed = false;

    public AutomaticTrigger(Float hitbox, String name) {
        super(hitbox);
        startY = hitbox.y;
        this.name = name;
    }

    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    public void setStartCutscene(int index) {
        this.startCutscene = index;
    }

    public int getStartCutscene() {
        return this.startCutscene;
    }

    public boolean hasPlayed() {
        return this.hasPlayed;
    }

    public void setPlayed(boolean played) {
        this.hasPlayed = played;
    }

    /** For use in Flying */
    public void resetTo(float y) {
        startCutscene = 0;
        hasPlayed = false;
        hitbox.y = startY + y;
    }
    
}
