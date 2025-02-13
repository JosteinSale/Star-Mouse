package rendering.flying;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import main_classes.Game;
import rendering.MyImage;
import rendering.misc.RenderOptionsMenu;
import ui.PauseFlying;
import utils.DrawUtils;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

public class RenderPauseFly {
   private PauseFlying pause;
   private RenderOptionsMenu rOptions;
   private ArrayList<Rectangle> menuRects;
   private Color bgColor = new Color(0, 0, 0, 140);
   private MyImage pointerImg;

   public RenderPauseFly(PauseFlying pause, RenderOptionsMenu rOptions) {
      this.pause = pause;
      this.rOptions = rOptions;
      this.pointerImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);
      this.constructMenuRects();
   }

   private void constructMenuRects() {
      this.menuRects = new ArrayList<>();
      for (int i = 0; i < pause.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE),
               (int) ((pause.cursorMinY - 40 + i * pause.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE),
               (int) (50 * Game.SCALE));
         menuRects.add(rect);
      }
   }

   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Background
      DrawUtils.fillScreen(g2, bgColor);

      if (pause.optionsMenu.isActive()) {
         rOptions.draw(g);
      } else {
         // Text
         DrawUtils.drawText(
               g2, Color.WHITE, DrawUtils.headerFont,
               "PAUSE",
               450, 200);
         for (int i = 0; i < pause.menuOptions.length; i++) {
            DrawCenteredString(
                  g2, pause.menuOptions[i],
                  menuRects.get(i), DrawUtils.menuFont);
         }

         // Cursor
         DrawUtils.drawImage(
               g2, pointerImg,
               pause.cursorX, pause.cursorY - 30,
               CURSOR_WIDTH, CURSOR_HEIGHT);
      }
   }
}
