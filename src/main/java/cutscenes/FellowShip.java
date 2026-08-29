package cutscenes;

import static utils.Constants.Flying.SHIP_HITBOX_HEIGHT;
import static utils.Constants.Flying.SHIP_HITBOX_WIDTH;

import java.awt.geom.Rectangle2D;

import entities.Dimensions;
import entities.MyRectangle;
import entities.flying.ShipFlame;
import entities.flying.ShipSmoke;

public class FellowShip {
   public ShipFlame flame;
   public ShipSmoke smoke;
   public MyRectangle hitbox;
   private boolean onScreen = true;
   private int framesBeforeTakeOff;
   public int height = 50;
   private int speed = 3;

   public FellowShip(float x, float y, int framesBeforeTakeOff) {
      this.hitbox = new MyRectangle(x, y, (int) SHIP_HITBOX_WIDTH, (int) SHIP_HITBOX_HEIGHT);
      this.framesBeforeTakeOff = framesBeforeTakeOff;
      this.flame = new ShipFlame();
      this.smoke = new ShipSmoke(hitbox);
   }

   public void update() {
      this.flame.update();
      this.smoke.update();
      if (framesBeforeTakeOff > 0) {
         framesBeforeTakeOff--;
      } else {
         hitbox.move(0, -speed);
         if ((hitbox.y() + height) < -200) {
            onScreen = false;
         }
      }
   }

   public boolean isOnScreen() {
      return onScreen;
   }

}
