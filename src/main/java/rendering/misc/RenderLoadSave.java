package rendering.misc;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import data_storage.SaveData;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.SwingRender;
import ui.LoadSaveMenu;
import utils.DrawUtils;
import utils.ResourceLoader;

public class RenderLoadSave implements SwingRender {
   private Game game;
   private LoadSaveMenu menu;
   private RenderInfoChoice rInfoChoice;
   private Rectangle headerRect;
   private MyColor bgColor = new MyColor(0, 0, 0, 230);
   private MyImage pointerImg;

   private int bgW;
   private int bgH;
   private int bgX;
   private int bgY;
   private int cursorX = 170;
   private int optionsX = 250;
   private int optionsY = 330;

   public RenderLoadSave(Game game, LoadSaveMenu menu, RenderInfoChoice rInfoChoice) {
      this.game = game;
      this.menu = menu;
      this.rInfoChoice = rInfoChoice;
      calcDrawValues();
      loadImages();
      constructRectangles();
   }

   private void calcDrawValues() {
      bgW = OPTIONS_WIDTH;
      bgH = OPTIONS_HEIGHT;
      bgX = (int) ((Game.GAME_DEFAULT_WIDTH / 2) - (bgW / 2));
      bgY = (int) ((Game.GAME_DEFAULT_HEIGHT / 2) - (bgH / 2));
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);
   }

   private void constructRectangles() {
      this.headerRect = new Rectangle(
            0, (int) (150 * Game.SCALE),
            Game.GAME_WIDTH, 50);
   }

   @Override
   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Background
      DrawUtils.fillRect(
            g2, bgColor,
            bgX, bgY,
            bgW, bgH);
      DrawUtils.drawRect(
            g2, MyColor.WHITE,
            bgX + 10, bgY + 10,
            bgW - 20, bgH - 20);

      // Header text
      DrawUtils.DrawCenteredString(
            g, menu.currentMenu, headerRect,
            DrawUtils.headerFont, MyColor.WHITE);

      // Menu Options
      drawMenuOptions(g);

      // Cursor
      DrawUtils.drawImage(
            g2, pointerImg,
            cursorX, menu.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

   private void drawMenuOptions(Graphics g) {
      SaveData saveData = game.getSaveData();

      // Left side: draw the menu options
      for (int i = 0; i < menu.menuOptions.length; i++) {
         DrawUtils.drawText(
               g, MyColor.WHITE, DrawUtils.menuFont,
               menu.menuOptions[i],
               optionsX, optionsY + i * menu.menuOptionsDiff);
      }
      // Right side: if the save is started, draw the date and time. Else, draw
      // [empty]
      int xPos = optionsX + 200;
      for (int i = 0; i < 3; i++) {
         if (saveData.getProgValuesFor(i + 1).saveStarted) {
            DrawUtils.drawText(
                  g, MyColor.WHITE, DrawUtils.menuFont,
                  saveData.getProgValuesFor(i + 1).lastUsed,
                  xPos, optionsY + i * menu.menuOptionsDiff);
         } else {
            DrawUtils.drawText(
                  g, MyColor.WHITE, DrawUtils.menuFont,
                  "[EMPTY]",
                  xPos, optionsY + i * menu.menuOptionsDiff);
         }
      }
      if (menu.infoChoiceActive) {
         this.rInfoChoice.draw(g);
      }
   }

   @Override
   public void dispose() {
   }

}
