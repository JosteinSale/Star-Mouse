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
   private String controlSchemeText;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;
   private int keyActionX = 220;
   private int keyBindX = 720;
   private int keyMinY = 220;
   private int keyYDiff = 30;

   public RenderControlsMenu(ControlsMenu controlsMenu, Images images) {
      this.menu = controlsMenu;
      this.loadImages(images);
      this.calcDrawValues();
      this.constructControlSchemeText();
   }

   private void constructControlSchemeText() {
      String text = "Control Scheme: ";
      for (String variant : menu.kbVariantNames) {
         text += variant + " ";
      }
      this.controlSchemeText = text;
   }

   private void loadImages(Images images) {
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
      this.selectImg = images.getExpImageSprite(
            Images.ITEM_SELECTED, true);
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

      // Menu title
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.headerFont,
            "CONTROLS",
            420, 150);
      // Control Scheme text
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.menuFont,
            controlSchemeText,
            keyActionX + 30, menu.cursorMinY);
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.itemFont,
            "(Change with SPACE)",
            560, 530);
      // Return text
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.menuFont,
            "Return",
            keyActionX + 30, menu.cursorMinY + menu.menuOptionsDiff);
      // Action names
      for (int i = 0; i < menu.actionNames.length; i++) {
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.infoFont,
               menu.actionNames[i],
               keyActionX, keyMinY + i * keyYDiff);
      }
      // Key names
      for (int i = 0; i < menu.getCurrentKeyNames().length; i++) {
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.infoFont,
               menu.getCurrentKeyNames()[i],
               keyBindX, keyMinY + i * keyYDiff);
      }
      // Cursor
      DrawUtils.drawImage(
            sb, pointerImg,
            menu.cursorX, menu.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
      // Selection box for control Scheme
      DrawUtils.drawImage(
            sb, selectImg,
            keyActionX + 367 + menu.selectedKeyBindingIndex * 44, menu.cursorMinY - 42,
            50, 50);

   }
}
