package entities.exploring;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import entities.Entity;

/** Mostly just called 'object' in the rest of the code */
public class InteractableObject extends Entity {
   private String name;
   private int startCutscene = 0;

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

   public String getName() {
      return this.name;
   }
}
