package rendering.misc;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.Render;
import ui.ControlsMenu;
import ui.OptionsMenu;
import utils.DrawUtils;
import utils.Images;
import utils.Singleton;

import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;
import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.SLIDER_HEIGHT;
import static utils.Constants.UI.SLIDER_WIDTH;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderOptionsMenu extends Singleton implements Render {
   private OptionsMenu optionsMenu;
   private ControlsMenu controlsMenu;
   private RenderControlsMenu rControlsMenu;
   private MyColor bgColor = new MyColor(0, 0, 0, 230);
   private MyImage pointerImg;
   private MyImage sliderImg;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;
   private int optionsX = 250;
   private int optionsY = 330;

   public RenderOptionsMenu(OptionsMenu optionsMenu, ControlsMenu controlsMenu, Images images) {
      this.optionsMenu = optionsMenu;
      this.controlsMenu = controlsMenu;
      this.rControlsMenu = new RenderControlsMenu(controlsMenu, images);
      this.loadImages(images);
      this.calcDrawValues();
   }

   private void loadImages(Images images) {
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
      this.sliderImg = images.getExpImageSprite(
            Images.SLIDER_SPRITE, true);
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   @Override
   public void draw(SpriteBatch sb) {
      if (controlsMenu.isActive()) {
         rControlsMenu.draw(sb);
         return;
      }

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
            "OPTIONS",
            420, 150);
      for (int i = 0; i < optionsMenu.menuOptions.length; i++) {
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.menuFont,
               optionsMenu.menuOptions[i],
               optionsX, optionsY + i * optionsMenu.menuOptionsDiff);
      }

      // Sliders
      DrawUtils.fillRect(
            sb, MyColor.WHITE,
            550, 318, optionsMenu.sliderBarWidth, 5);
      DrawUtils.fillRect(
            sb, MyColor.WHITE,
            550, 390, optionsMenu.sliderBarWidth, 5);
      DrawUtils.drawImage(
            sb, sliderImg,
            optionsMenu.musicSliderX, 295,
            SLIDER_WIDTH, SLIDER_HEIGHT);
      DrawUtils.drawImage(
            sb, sliderImg,
            optionsMenu.sfxSliderX, 370,
            SLIDER_WIDTH, SLIDER_HEIGHT);

      // Cursor
      DrawUtils.drawImage(
            sb, pointerImg,
            optionsMenu.cursorX, optionsMenu.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }
}
