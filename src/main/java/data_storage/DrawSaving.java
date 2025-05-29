package data_storage;

import rendering.MyColor;
import java.awt.Graphics;
import utils.DrawUtils;

/**
 * A small class that can be used to display a 'Saving Game'-message.
 * It handles its own active-status, so you can just call the update-
 * and draw-methods every frame.
 */
public class DrawSaving {
   private boolean active = false;
   private int displayTimer = 90;
   private int displayTick;
   private int X;
   private int Y;

   public DrawSaving() {
      this.X = 800;
      this.Y = 700;
   }

   public void start() {
      this.displayTick = displayTimer;
      this.active = true;
   }

   public void update() {
      if (!active) {
         return;
      } else {
         displayTick--;
         if (displayTick == 0) {
            active = false;
         }
      }
   }

   public void draw(Graphics g) {
      if (!active) {
         return;
      }
      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.menuFont,
            "Game Saved", X, Y);
   }

}
