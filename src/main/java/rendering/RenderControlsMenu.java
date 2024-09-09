package rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main_classes.Game;
import ui.ControlsMenu;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;

public class RenderControlsMenu implements SwingRender {
   private ControlsMenu menu;
   private Color bgColor = new Color(0, 0, 0, 230);
   private Font headerFont;
   private Font menuFont;
   private BufferedImage pointerImg;
   private BufferedImage selectImg;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;
   public int keyFuncX = 250;
   public int keyBindX = 750;

   public RenderControlsMenu(ControlsMenu controlsMenu) {
      this.menu = controlsMenu;
      this.loadImages();
      this.loadFonts();
      this.calcDrawValues();
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
      this.selectImg = ResourceLoader.getExpImageSprite(ResourceLoader.KEY_SELECT);
   }

   private void loadFonts() {
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getInfoFont();
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   @Override
   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      // Background
      g.setColor(bgColor);
      g.fillRect(
            (int) (bgX * Game.SCALE), (int) (bgY * Game.SCALE),
            (int) (bgW * Game.SCALE), (int) (bgH * Game.SCALE));

      g2.setColor(Color.WHITE);
      g2.drawRect(
            (int) ((bgX + 10) * Game.SCALE), (int) ((bgY + 10) * Game.SCALE),
            (int) ((bgW - 20) * Game.SCALE), (int) ((bgH - 20) * Game.SCALE));

      // Text
      g.setFont(headerFont);
      g.setColor(Color.WHITE);
      g.drawString("CONTROLS", (int) (420 * Game.SCALE), (int) (150 * Game.SCALE));
      g.setFont(menuFont);
      g.drawString("(To change, select it and type a new key)",
            (int) (200 * Game.SCALE), (int) (210 * Game.SCALE));

      for (int i = 0; i < menu.keyFunctions.length; i++) {
         g.drawString(
               menu.keyFunctions[i],
               (int) (keyFuncX * Game.SCALE), (int) ((menu.cursorMinY + i * menu.menuOptionsDiff) * Game.SCALE));
      }
      for (int i = 0; i < menu.keyBindings.length; i++) {
         g.drawString(
               menu.keyBindings[i],
               (int) (keyBindX * Game.SCALE), (int) ((menu.cursorMinY + i * menu.menuOptionsDiff) * Game.SCALE));
      }

      // Cursor
      g2.drawImage(
            pointerImg,
            (int) (menu.cursorX * Game.SCALE), (int) ((menu.cursorY - 30) * Game.SCALE),
            (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);

      if (menu.settingKey) {
         g2.drawImage(
               selectImg,
               (int) (240 * Game.SCALE),
               (int) ((menu.cursorY - 32) * Game.SCALE),
               (int) (620 * Game.SCALE), (int) (40 * Game.SCALE), null);
      }
   }

   @Override
   public void dispose() {

   }

}
