package rendering.exploring;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

import static utils.Constants.UI.MECHANIC_DISPLAY_HEIGHT;
import static utils.Constants.UI.MECHANIC_DISPLAY_WIDTH;

import java.awt.Graphics;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.SwingRender;
import rendering.misc.RenderInfoBox;
import rendering.misc.RenderInfoChoice;
import ui.MechanicOverlay;
import utils.DrawUtils;
import utils.Images;

public class RenderMechanic implements SwingRender {
   private Game game;
   private MechanicOverlay mechanic;
   private RenderInfoBox rInfoBox;
   private RenderInfoChoice rInfoChoice;

   private MyImage bgImg;
   private MyImage pointerImg;

   private MyColor inventoryColor = new MyColor(0, 0, 0, 230);
   private MyColor lazerBarColor = MyColor.CYAN;
   private MyColor hpBarColor = MyColor.ORANGE;
   private MyColor displayColor = MyColor.GREEN;

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
      this.loadImages(game.getImages());
   }

   private void loadImages(Images images) {
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_BLACK, true);
      this.bgImg = images.getExpImageSprite(
            Images.MECHANIC_DISPLAY, true);
   }

   @Override
   public void draw(Graphics g) {
      drawDisplay(g);
      drawInventory(g);
      drawText(g);
      drawMaxedOut(g);
   }

   private void drawMaxedOut(Graphics g) {
      for (int i = 0; i < 3; i++) {
         if (mechanic.maxedOut[i]) {
            DrawUtils.fillRect(
                  g, new MyColor(0, 0, 0, 190),
                  277, 138 + 75 * i,
                  263, 70);
            DrawUtils.drawText(
                  g, MyColor.RED, DrawUtils.menuFont,
                  "(max)",
                  350, 185 + 75 * i);
         }
      }
   }

   private void drawInventory(Graphics g) {
      DrawUtils.fillRect(
            g, inventoryColor,
            inventoryX, inventoryY,
            inventoryW, inventoryH);
      DrawUtils.drawRect(
            g, MyColor.WHITE,
            inventoryX + 10, inventoryY + 10,
            inventoryW - 20, inventoryH - 20);
   }

   private void drawText(Graphics g) {
      // EXIT
      DrawUtils.drawText(
            g, MyColor.DARK_GRAY, DrawUtils.menuFont,
            "EXIT", 360, 410);

      // Inventory
      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.menuFont,
            "Inventory", 410, 630);

      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.infoFont,
            "Credits: x" + Integer.toString(
                  game.getExploring().getProgressValues().getCredits()),
            250, 690);
      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.infoFont,
            "Bombs: x" + Integer.toString(
                  game.getExploring().getProgressValues().getBombs()),
            620, 690);

      // Display-text
      DrawUtils.drawText(
            g, displayColor, DrawUtils.menuFont,
            mechanic.optionNames[mechanic.selectedIndex],
            560, 200);

      for (int i = 0; i < 2; i++) { // Item-info
         DrawUtils.drawText(
               g, displayColor, DrawUtils.itemFont,
               mechanic.optionInfo[mechanic.selectedIndex][i], 560, 250 + i * 50);
      }
      // Item-price
      DrawUtils.drawText(
            g, displayColor, DrawUtils.menuFont,
            mechanic.optionInfo[mechanic.selectedIndex][2],
            560, 400);

      if (mechanic.infoBoxActive) {
         rInfoBox.draw(g);
      } else if (mechanic.infoChoiceActive) {
         rInfoChoice.draw(g);
      }
   }

   private void drawDisplay(Graphics g) {
      // Background
      DrawUtils.drawImage(
            g, bgImg,
            bgImgX, bgImgY,
            bgImgW, bgImgH);

      // Bars
      DrawUtils.fillRect(
            g, lazerBarColor,
            370, 167,
            mechanic.lazerBarW, barH);
      DrawUtils.fillRect(
            g, hpBarColor,
            370, 240,
            mechanic.hpBarW, barH);

      // Cursor
      DrawUtils.drawImage(
            g, pointerImg,
            cursorX, mechanic.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

   @Override
   public void dispose() {
   }

}
