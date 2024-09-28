package rendering;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

import static utils.Constants.UI.MECHANIC_DISPLAY_HEIGHT;
import static utils.Constants.UI.MECHANIC_DISPLAY_WIDTH;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import main_classes.Game;
import ui.MechanicOverlay;
import utils.ResourceLoader;

public class RenderMechanic implements SwingRender {
   private Game game;
   private MechanicOverlay mechanic;
   private RenderInfoBox rInfoBox;
   private RenderInfoChoice rInfoChoice;

   private BufferedImage bgImg;
   private BufferedImage pointerImg;

   private Color inventoryColor = new Color(0, 0, 0, 230);
   private Color lazerBarColor = Color.CYAN;
   private Color hpBarColor = Color.ORANGE;
   private Color displayColor = Color.GREEN;
   private Font menuFont;
   private Font itemFont;
   private Font infoFont;

   private int cursorX = 200;
   private int barH = 21;

   private int bgImgX = Game.GAME_DEFAULT_WIDTH / 2 - (MECHANIC_DISPLAY_WIDTH / 2);
   private int bgImgY = 20;
   private int bgImgW = MECHANIC_DISPLAY_WIDTH;
   private int bgImgH = MECHANIC_DISPLAY_HEIGHT;

   private int inventoryX = bgImgX;
   private int inventoryY = 580;
   private int inventoryW = bgImgW;
   private int inventoryH = 150;

   public RenderMechanic(Game game, MechanicOverlay mechanicOverlay, RenderInfoBox rInfoBox,
         RenderInfoChoice rInfoChoice) {
      this.game = game;
      this.mechanic = mechanicOverlay;
      this.rInfoBox = rInfoBox;
      this.rInfoChoice = rInfoChoice;
      this.loadFonts();
      this.loadImages();
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_BLACK);
      this.bgImg = ResourceLoader.getExpImageSprite(ResourceLoader.MECHANIC_DISPLAY);
   }

   private void loadFonts() {
      this.menuFont = ResourceLoader.getNameFont();
      this.infoFont = ResourceLoader.getInfoFont();
      this.itemFont = ResourceLoader.getItemFont();
   }

   @Override
   public void draw(Graphics g) {
      drawDisplay(g);
      drawInventory(g);
      drawText(g);
      drawMaxedOut(g);
   }

   private void drawMaxedOut(Graphics g) {
      g.setFont(menuFont);
      for (int i = 0; i < 3; i++) {
         if (mechanic.maxedOut[i]) {
            g.setColor(new Color(0, 0, 0, 190));
            g.fillRect(
                  (int) (277 * Game.SCALE), (int) ((138 + 75 * i) * Game.SCALE),
                  (int) (263 * Game.SCALE),
                  (int) (70 * Game.SCALE));
            g.setColor(Color.RED);
            g.drawString(
                  "(max)",
                  (int) (350 * Game.SCALE), (int) ((185 + 75 * i) * Game.SCALE));
         }
      }
   }

   private void drawInventory(Graphics g) {
      g.setColor(inventoryColor);
      g.fillRect(
            (int) (inventoryX * Game.SCALE), (int) (inventoryY * Game.SCALE),
            (int) (inventoryW * Game.SCALE), (int) (inventoryH * Game.SCALE));

      g.setColor(Color.WHITE);
      g.drawRect(
            (int) ((inventoryX + 10) * Game.SCALE), (int) ((inventoryY + 10) * Game.SCALE),
            (int) ((inventoryW - 20) * Game.SCALE), (int) ((inventoryH - 20) * Game.SCALE));
   }

   private void drawText(Graphics g) {
      // EXIT
      g.setFont(menuFont);
      g.setColor(Color.GRAY.darker());
      g.drawString("EXIT", (int) (360 * Game.SCALE), (int) (410 * Game.SCALE));

      // Inventory
      g.setFont(menuFont);
      g.setColor(Color.WHITE);
      g.drawString("Inventory", (int) (410 * Game.SCALE), (int) (630 * Game.SCALE));

      g.setFont(infoFont);
      g.drawString(
            "Credits: x" + Integer.toString(game.getExploring().getProgressValues().getCredits()),
            (int) (250 * Game.SCALE), (int) (690 * Game.SCALE));
      g.drawString(
            "Bombs: x" + Integer.toString(game.getExploring().getProgressValues().getBombs()),
            (int) (620 * Game.SCALE), (int) (690 * Game.SCALE));

      // Display-text
      g.setColor(displayColor);
      g.setFont(menuFont);
      g.drawString(
            mechanic.optionNames[mechanic.selectedIndex],
            (int) (560 * Game.SCALE), (int) (200 * Game.SCALE));
      g.setFont(itemFont);
      for (int i = 0; i < 2; i++) { // Item-info
         g.drawString(
               mechanic.optionInfo[mechanic.selectedIndex][i],
               (int) (560 * Game.SCALE), (int) ((250 + i * 50) * Game.SCALE));
      }
      g.setFont(menuFont); // Item-price
      g.drawString(
            mechanic.optionInfo[mechanic.selectedIndex][2],
            (int) (560 * Game.SCALE), (int) (400 * Game.SCALE));

      if (mechanic.infoBoxActive) {
         rInfoBox.draw(g);
      } else if (mechanic.infoChoiceActive) {
         rInfoChoice.draw(g);
      }
   }

   private void drawDisplay(Graphics g) {
      // Background
      g.drawImage(
            bgImg,
            (int) (bgImgX * Game.SCALE), (int) (bgImgY * Game.SCALE),
            (int) (bgImgW * Game.SCALE), (int) (bgImgH * Game.SCALE), null);

      // Bars
      g.setColor(lazerBarColor);
      g.fillRect(
            (int) (370 * Game.SCALE), (int) (167 * Game.SCALE),
            (int) (mechanic.lazerBarW * Game.SCALE),
            (int) (barH * Game.SCALE));
      g.setColor(hpBarColor);
      g.fillRect(
            (int) (370 * Game.SCALE), (int) (240 * Game.SCALE),
            (int) (mechanic.hpBarW * Game.SCALE),
            (int) (barH * Game.SCALE));

      // Cursor
      g.drawImage(
            pointerImg,
            (int) (cursorX * Game.SCALE), (int) ((mechanic.cursorY - 30) * Game.SCALE),
            (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
   }

   @Override
   public void dispose() {
   }

}
