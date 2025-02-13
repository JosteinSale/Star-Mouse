package rendering.misc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import main_classes.Game;
import rendering.MyImage;
import rendering.SwingRender;
import ui.ControlsMenu;
import ui.OptionsMenu;
import utils.DrawUtils;
import utils.ResourceLoader;

import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;
import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.SLIDER_HEIGHT;
import static utils.Constants.UI.SLIDER_WIDTH;

public class RenderOptionsMenu implements SwingRender {
   private OptionsMenu optionsMenu;
   private ControlsMenu controlsMenu;
   private RenderControlsMenu rControlsMenu;
   private Color bgColor = new Color(0, 0, 0, 230);
   private MyImage pointerImg;
   private MyImage sliderImg;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;
   private int optionsX = 250;
   private int optionsY = 330;

   public RenderOptionsMenu(OptionsMenu optionsMenu, ControlsMenu controlsMenu) {
      this.optionsMenu = optionsMenu;
      this.controlsMenu = controlsMenu;
      this.rControlsMenu = new RenderControlsMenu(controlsMenu);
      this.loadImages();
      this.calcDrawValues();
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);
      this.sliderImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.SLIDER_SPRITE);
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   @Override
   public void draw(Graphics g) {
      if (controlsMenu.isActive()) {
         rControlsMenu.draw(g);
         return;
      }

      Graphics2D g2 = (Graphics2D) g;
      // Background
      DrawUtils.fillRect(
            g2, bgColor,
            bgX, bgY, bgW, bgH);
      DrawUtils.drawRect(
            g2, Color.WHITE,
            bgX + 10, bgY + 10, bgW - 20, bgH - 20);

      // Text
      DrawUtils.drawText(
            g2, Color.WHITE, DrawUtils.headerFont,
            "OPTIONS",
            420, 150);
      for (int i = 0; i < optionsMenu.menuOptions.length; i++) {
         DrawUtils.drawText(
               g2, Color.WHITE, DrawUtils.menuFont,
               optionsMenu.menuOptions[i],
               optionsX, optionsY + i * optionsMenu.menuOptionsDiff);
      }

      // Sliders
      DrawUtils.fillRect(
            g2, Color.WHITE,
            550, 318, optionsMenu.sliderBarWidth, 5);
      DrawUtils.fillRect(
            g2, Color.WHITE,
            550, 390, optionsMenu.sliderBarWidth, 5);
      DrawUtils.drawImage(
            g2, sliderImg,
            optionsMenu.musicSliderX, 295,
            SLIDER_WIDTH, SLIDER_HEIGHT);
      DrawUtils.drawImage(
            g2, sliderImg,
            optionsMenu.sfxSliderX, 370,
            SLIDER_WIDTH, SLIDER_HEIGHT);

      // Cursor
      DrawUtils.drawImage(
            g2, pointerImg,
            optionsMenu.cursorX, optionsMenu.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

   @Override
   public void dispose() {
   }

}
