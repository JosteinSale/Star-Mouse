package rendering.boss_mode;

import main_classes.Game;
import rendering.misc.RenderOptionsMenu;
import ui.PauseBoss;
import utils.ResourceLoader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

public class RenderPauseBoss {
   private PauseBoss pause;
   private Color bgColor = new Color(0, 0, 0, 140);
   private Font headerFont;
   private Font menuFont;
   private BufferedImage pointerImg;
   private ArrayList<Rectangle> menuRectangles;
   private RenderOptionsMenu rOptions;

   public RenderPauseBoss(PauseBoss pause, RenderOptionsMenu rOptions) {
      this.pause = pause;
      this.rOptions = rOptions;
      this.menuRectangles = new ArrayList<>();
      this.loadResources();
      makeMenuOptionRectangles();
   }

   private void loadResources() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getNameFont();
   }

   private void makeMenuOptionRectangles() {
      for (int i = 0; i < pause.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE),
               (int) ((pause.cursorMinY - 40 + i * pause.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
         menuRectangles.add(rect);
      }
   }

   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Background
      g.setColor(bgColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      if (pause.optionsMenu.isActive()) {
         rOptions.draw(g);
      } else {
         // Text
         g.setFont(headerFont);
         g.setColor(Color.WHITE);
         g.drawString("PAUSE", (int) (450 * Game.SCALE), (int) (200 * Game.SCALE));
         for (int i = 0; i < pause.menuOptions.length; i++) {
            DrawCenteredString(g2, pause.menuOptions[i],
                  menuRectangles.get(i), menuFont);
         }

         // Cursor
         g2.drawImage(
               pointerImg,
               (int) (pause.cursorX * Game.SCALE),
               (int) ((pause.cursorY - 30) * Game.SCALE),
               (int) (CURSOR_WIDTH * Game.SCALE),
               (int) (CURSOR_HEIGHT * Game.SCALE), null);
      }
   }
}
