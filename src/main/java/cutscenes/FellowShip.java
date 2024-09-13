package cutscenes;

import entities.flying.ShipFlame;

public class FellowShip {
   public ShipFlame flame;
   public float xPos;
   public float yPos;
   private boolean onScreen = true;
   private int framesBeforeTakeOff;
   public int height = 50;
   private int speed = 3;

   public FellowShip(float x, float y, int framesBeforeTakeOff) {
      this.xPos = x;
      this.yPos = y;
      this.framesBeforeTakeOff = framesBeforeTakeOff;
      this.flame = new ShipFlame();
   }

   public void update() {
      this.flame.update();
      if (framesBeforeTakeOff > 0) {
         framesBeforeTakeOff--;
      } else {
         yPos -= speed;
         if ((yPos + height) < -45) {
            onScreen = false;
         }
      }
   }

   public boolean isOnScreen() {
      return onScreen;
   }

}
