package rendering.boss_mode;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.misc.RenderOptionsMenu;
import ui.PauseBoss;
import utils.DrawUtils;
import utils.Images;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class RenderPauseBoss {
   private PauseBoss pause;
   private MyColor bgColor = new MyColor(0, 0, 0, 140);
   private MyImage pointerImg;
   private ArrayList<Rectangle> menuRects;
   private RenderOptionsMenu rOptions;

   public RenderPauseBoss(PauseBoss pause, RenderOptionsMenu rOptions, Images images) {
      this.pause = pause;
      this.rOptions = rOptions;
      this.menuRects = new ArrayList<>();
      constructMenuRects();
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
   }

   private void constructMenuRects() {
      for (int i = 0; i < pause.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE),
               (int) ((pause.cursorMinY - 40 + i * pause.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
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
               g2, MyColor.WHITE, DrawUtils.headerFont,
               "PAUSE", 450, 200);
         for (int i = 0; i < pause.menuOptions.length; i++) {
            DrawUtils.DrawCenteredString(
                  g2, pause.menuOptions[i], menuRects.get(i),
                  DrawUtils.menuFont, MyColor.WHITE);
         }

         // Cursor
         DrawUtils.drawImage(
               g2, pointerImg,
               pause.cursorX, pause.cursorY - 30,
               CURSOR_WIDTH, CURSOR_HEIGHT);
      }
   }
}
