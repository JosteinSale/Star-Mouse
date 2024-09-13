package rendering;

import java.awt.Graphics;
import java.awt.Rectangle;

import main_classes.Game;
import ui.NumberDisplay;
import utils.ResourceLoader;
import java.awt.image.BufferedImage;

import java.awt.Font;
import java.awt.Color;

import static utils.Constants.UI.NUMBER_SELECT_HEIGHT;
import static utils.Constants.UI.NUMBER_SELECT_WIDTH;
import static utils.Constants.UI.NUMBER_DISPLAY_HEIGHT;
import static utils.Constants.UI.NUMBER_DISPLAY_WIDTH;

public class RenderNumberDisplay implements SwingRender {
   private NumberDisplay nrDisplay;
   private BufferedImage bgImg;
   private BufferedImage selectedNrImg;
   private Color fadeColor;
   private Font numberFont;
   private Rectangle bgRect;

   public RenderNumberDisplay() {
      this.bgImg = ResourceLoader.getExpImageSprite(ResourceLoader.NUMBER_DISPLAY);
      this.selectedNrImg = ResourceLoader.getExpImageSprite(ResourceLoader.NUMBER_SELECT);
      this.fadeColor = new Color(0, 0, 0, 150);
      this.numberFont = ResourceLoader.getHeaderFont();
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
      g.setColor(fadeColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      // Background
      g.drawImage(
            bgImg,
            (int) (bgRect.x * Game.SCALE),
            (int) (bgRect.y * Game.SCALE),
            (int) (bgRect.width * Game.SCALE),
            (int) (bgRect.height * Game.SCALE), null);

      // Selected Nr
      g.drawImage(
            selectedNrImg,
            (int) ((bgRect.x + 108 + (nrDisplay.digitIndex * 81)) * Game.SCALE),
            (int) ((bgRect.y + 28) * Game.SCALE),
            (int) (NUMBER_SELECT_WIDTH * Game.SCALE),
            (int) (NUMBER_SELECT_HEIGHT * Game.SCALE), null);

      // Numbers
      g.setColor(Color.GREEN);
      g.setFont(numberFont);
      for (int i = 0; i < 4; i++) {
         g.drawString(
               Integer.toString(nrDisplay.currentCode[i]),
               (int) ((bgRect.x + 108 + (i * 81)) * Game.SCALE),
               (int) ((bgRect.y + 160) * Game.SCALE));
      }
   }

   @Override
   public void dispose() {
   }

}
