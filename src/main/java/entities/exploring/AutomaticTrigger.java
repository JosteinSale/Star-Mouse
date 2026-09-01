package entities.exploring;

import entities.Dimensions;
import entities.MyRectangle;

public class AutomaticTrigger extends MyRectangle {
   private String name;
   private float startY; // for use in Flying
   private int startCutscene = 0;
   private boolean hasPlayed = false;

   public AutomaticTrigger(Dimensions hitbox, String name) {
      super(hitbox);
      startY = hitbox.y();
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

   public boolean hasPlayed() {
      return this.hasPlayed;
   }

   public void setPlayed(boolean played) {
      this.hasPlayed = played;
   }

   public String getName() {
      return this.name;
   }

   /** For use in Flying */
   public void resetTo(float y) {
      startCutscene = 0;
      hasPlayed = false;
      setY(startY + y);
   }

}
