package rendering.exploring;

import java.awt.Graphics;
import java.awt.Rectangle;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.SwingRender;
import ui.NumberDisplay;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.NUMBER_SELECT_HEIGHT;
import static utils.Constants.UI.NUMBER_SELECT_WIDTH;
import static utils.Constants.UI.NUMBER_DISPLAY_HEIGHT;
import static utils.Constants.UI.NUMBER_DISPLAY_WIDTH;

public class RenderNumberDisplay implements SwingRender {
   private NumberDisplay nrDisplay;
   private MyImage bgImg;
   private MyImage selectedNrImg;
   private MyColor fadeColor;
   private Rectangle bgRect;

   public RenderNumberDisplay(Images images) {
      this.bgImg = images.getExpImageSprite(
            Images.NUMBER_DISPLAY, true);
      this.selectedNrImg = images.getExpImageSprite(
            Images.NUMBER_SELECT, true);
      this.fadeColor = new MyColor(0, 0, 0, 150);
      calcDrawValues();
   }

   /** Should be called when the player enters Exploring or enters a new area. */
   public void setNrDisplay(NumberDisplay nrDisplay) {
      this.nrDisplay = nrDisplay;
   }

   private void calcDrawValues() {
      this.bgRect = new Rectangle(
            Game.GAME_DEFAULT_WIDTH / 2 - NUMBER_DISPLAY_WIDTH / 2,
            Game.GAME_DEFAULT_HEIGHT / 2 - NUMBER_DISPLAY_HEIGHT / 2,
            NUMBER_DISPLAY_WIDTH, NUMBER_DISPLAY_HEIGHT);
   }

   @Override
   public void draw(Graphics g) {
      // Dark overlay
      DrawUtils.drawRect(
            g, fadeColor,
            0, 0,
            Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);

      // Background
      DrawUtils.drawImage(
            g, bgImg,
            bgRect.x, bgRect.y,
            bgRect.width, bgRect.height);

      // Numbers
      for (int i = 0; i < 4; i++) {
         DrawUtils.drawText(
               g, MyColor.GREEN, DrawUtils.headerFont,
               Integer.toString(nrDisplay.currentCode[i]),
               bgRect.x + 108 + (i * 81), bgRect.y + 160);
      }

      // The selected number gets an image around it
      DrawUtils.drawImage(
            g, selectedNrImg,
            bgRect.x + 108 + (nrDisplay.digitIndex * 81), bgRect.y + 28,
            NUMBER_SELECT_WIDTH, NUMBER_SELECT_HEIGHT);

   }

   @Override
   public void dispose() {
   }

}
