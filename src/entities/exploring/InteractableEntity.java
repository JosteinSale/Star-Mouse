package entities.exploring;

import java.awt.geom.Rectangle2D;

public class InteractableEntity {
    protected Rectangle2D.Float hitbox;

    public InteractableEntity(Rectangle2D.Float hitbox) {
        this.hitbox = hitbox;
    }
}
