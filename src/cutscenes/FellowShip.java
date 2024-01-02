package cutscenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.flying.ShipFlame;
import main.Game;

import static utils.Constants.Flying.Sprites.SHIP_SPRITE_WIDTH;
import static utils.Constants.Flying.Sprites.SHIP_SPRITE_HEIGHT;

public class FellowShip {
   private BufferedImage shipImg;
   private ShipFlame flame;   // A bit wasteful in terms of memory usage.
   private float xPos;
   private float yPos;
   private boolean onScreen = true;
   private int framesBeforeTakeOff;
   private int shipHeight = 50;
   private int speed = 3;

   public FellowShip(float x, float y, int framesBeforeTakeOff, BufferedImage shipImg) {
      this.xPos = x;
      this.yPos = y;
      this.framesBeforeTakeOff = framesBeforeTakeOff;
      this.shipImg = shipImg;
      this.flame = new ShipFlame();
   }

   public void update() {
      this.flame.update();
      if (framesBeforeTakeOff > 0) {
         framesBeforeTakeOff--;
      } 
      else {
         yPos -= speed;
         if ((yPos + shipHeight) < 0) {
            onScreen = false;
         }
      }
   }

   public void draw(Graphics g) {
      g.drawImage(
         shipImg, 
         (int) ((xPos - 20) * Game.SCALE), 
         (int) ((yPos - 20) * Game.SCALE),
         (int) (SHIP_SPRITE_WIDTH * 3 * Game.SCALE),
         (int) (SHIP_SPRITE_HEIGHT * 3 * Game.SCALE), null);

      flame.draw(g, xPos + 2.5f, yPos + shipHeight);
   }

   public boolean isOnScreen() {
      return onScreen;
   }

}
