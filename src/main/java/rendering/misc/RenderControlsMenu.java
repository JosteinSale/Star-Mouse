package rendering.misc;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.Render;
import ui.ControlsMenu;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderControlsMenu implements Render {
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

   public RenderControlsMenu(ControlsMenu controlsMenu, Images images) {
      this.menu = controlsMenu;
      this.loadImages(images);
      this.calcDrawValues();
   }

   private void loadImages(Images images) {
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
      this.selectImg = images.getExpImageSprite(
            Images.KEY_SELECT, true);
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   @Override
   public void draw(SpriteBatch sb) {
      // Background
      DrawUtils.fillRect(
            sb, bgColor,
            bgX, bgY, bgW, bgH);
      DrawUtils.drawRect(
            sb, MyColor.WHITE,
            bgX + 10, bgY + 10, bgW - 20, bgH - 20);

      // Text
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.headerFont,
            "CONTROLS",
            420, 150);
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.infoFont,
            "(To change, select it and type a new key)",
            200, 210);
      for (int i = 0; i < menu.keyFunctions.length; i++) {
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.infoFont,
               menu.keyFunctions[i],
               keyFuncX, menu.cursorMinY + i * menu.menuOptionsDiff);
      }
      for (int i = 0; i < menu.keyBindings.length; i++) {
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.infoFont,
               menu.keyBindings[i],
               keyBindX, menu.cursorMinY + i * menu.menuOptionsDiff);
      }

      // Cursor
      DrawUtils.drawImage(
            sb, pointerImg,
            menu.cursorX, menu.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
      if (menu.settingKey) {
         DrawUtils.drawImage(
               sb, selectImg,
               240, menu.cursorY - 32,
               620, 40);
      }
   }
}
