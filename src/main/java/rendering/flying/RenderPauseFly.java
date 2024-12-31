package rendering.flying;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main_classes.Game;
import rendering.misc.RenderOptionsMenu;
import ui.PauseFlying;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

public class RenderPauseFly {
   private PauseFlying pause;
   private RenderOptionsMenu rOptions;

   private Color bgColor = new Color(0, 0, 0, 140);
   private Font headerFont;
   private Font menuFont;
   private BufferedImage pointerImg;

   public RenderPauseFly(PauseFlying pause, RenderOptionsMenu rOptions) {
      this.pause = pause;
      this.rOptions = rOptions;
      this.loadResources();
   }

   private void loadResources() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getNameFont();
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
            Rectangle rect = new Rectangle(
                  (int) (425 * Game.SCALE), (int) ((pause.cursorMinY - 40 + i * pause.menuOptionsDiff) * Game.SCALE),
                  (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
            DrawCenteredString(g2, pause.menuOptions[i], rect, menuFont);
         }

         // Cursor
         g2.drawImage(
               pointerImg,
               (int) (pause.cursorX * Game.SCALE), (int) ((pause.cursorY - 30) * Game.SCALE),
               (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
      }
   }
}
