package entities;

import java.awt.geom.Rectangle2D;

public class Entity {
   public Rectangle2D.Float hitbox;

   public Entity(Rectangle2D.Float hitbox) {
      this.hitbox = hitbox;
   }
}
