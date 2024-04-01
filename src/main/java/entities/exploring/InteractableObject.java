package entities.exploring;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.Entity;

public class InteractableObject extends Entity {
    private String name;                      // Kan brukes i bugtesting
    private int startCutscene = 0;
    // private BufferedImage objectImg;
    // private boolean isVisible;
    // private boolean isActive;
    // private Action action;   (action can be performed when object is interacted with)

    public InteractableObject(Float hitbox, String name) {
        super(hitbox);
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
}
