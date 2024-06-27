package data_storage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main_classes.Game;
import utils.ResourceLoader;

/** A small class that can be used to display a 'Saving Game'-message.
 * It handles its own active-status, so you can just call the update-
 * and draw-methods every frame.
 */
public class DrawSaving {
   private boolean active = false;
   private Font font;
   private int displayTimer = 60;
   private int displayTick;
   private int X;
   private int Y;

   public DrawSaving() {
      this.font = ResourceLoader.getMenuFont();
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
      }
      else {
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
      g.setColor(Color.WHITE);
      g.setFont(font);
      g.drawString("Game Saved", (int) (X * Game.SCALE), (int) (Y * Game.SCALE));
   }
   
}
