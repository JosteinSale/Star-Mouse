package rendering.misc;

import java.awt.Graphics;
import java.awt.Graphics2D;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.SwingRender;
import ui.ControlsMenu;
import utils.DrawUtils;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;

public class RenderControlsMenu implements SwingRender {
   private ControlsMenu menu;
   private MyColor bgColor = new MyColor(0, 0, 0, 230);
   private MyImage pointerImg;
   private MyImage selectImg;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;
   public int keyFuncX = 250;
   public int keyBindX = 750;

   public RenderControlsMenu(ControlsMenu controlsMenu) {
      this.menu = controlsMenu;
      this.loadImages();
      this.calcDrawValues();
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);
      this.selectImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.KEY_SELECT);
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
      DrawUtils.fillRect(
            g2, bgColor,
            bgX, bgY, bgW, bgH);
      DrawUtils.drawRect(
            g2, MyColor.WHITE,
            bgX + 10, bgY + 10, bgW - 20, bgH - 20);

      // Text
      DrawUtils.drawText(
            g2, MyColor.WHITE, DrawUtils.headerFont,
            "CONTROLS",
            420, 150);
      DrawUtils.drawText(
            g2, MyColor.WHITE, DrawUtils.infoFont,
            "(To change, select it and type a new key)",
            200, 210);
      for (int i = 0; i < menu.keyFunctions.length; i++) {
         DrawUtils.drawText(
               g2, MyColor.WHITE, DrawUtils.infoFont,
               menu.keyFunctions[i],
               keyFuncX, menu.cursorMinY + i * menu.menuOptionsDiff);
      }
      for (int i = 0; i < menu.keyBindings.length; i++) {
         DrawUtils.drawText(
               g2, MyColor.WHITE, DrawUtils.infoFont,
               menu.keyBindings[i],
               keyBindX, menu.cursorMinY + i * menu.menuOptionsDiff);
      }

      // Cursor
      DrawUtils.drawImage(
            g2, pointerImg,
            menu.cursorX, menu.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
      if (menu.settingKey) {
         DrawUtils.drawImage(
               g2, selectImg,
               240, menu.cursorY - 32,
               620, 40);
      }
   }

   @Override
   public void dispose() {

   }

}
