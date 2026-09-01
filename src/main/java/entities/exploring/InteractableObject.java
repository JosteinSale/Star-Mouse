package entities.exploring;

import entities.Dimensions;
import entities.MyRectangle;

/** Mostly just called 'object' in the rest of the code */
public class InteractableObject extends MyRectangle {
   private String name;
   private int startCutscene = 0;

   public InteractableObject(Dimensions hitbox, String name) {
      super(hitbox);
      this.name = name;
   }

   public MyRectangle getHitbox() {
      return this;
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
