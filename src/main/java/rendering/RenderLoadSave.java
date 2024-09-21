package rendering;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.OPTIONS_HEIGHT;
import static utils.Constants.UI.OPTIONS_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import data_storage.SaveData;
import main_classes.Game;
import ui.LoadSaveMenu;
import utils.ResourceLoader;

public class RenderLoadSave implements SwingRender {
   private Game game;
   private LoadSaveMenu menu;
   private RenderInfoChoice rInfoChoice;
   private Rectangle headerRect;
   private Color bgColor = new Color(0, 0, 0, 230);
   private BufferedImage pointerImg;
   private Font headerFont;
   private Font menuFont;

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
      loadFonts();
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
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
   }

   private void loadFonts() {
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getMenuFont();
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
      g.setColor(bgColor);
      g.fillRect(
            (int) (bgX * Game.SCALE), (int) (bgY * Game.SCALE),
            (int) (bgW * Game.SCALE), (int) (bgH * Game.SCALE));

      g2.setColor(Color.WHITE);
      g2.drawRect(
            (int) ((bgX + 10) * Game.SCALE), (int) ((bgY + 10) * Game.SCALE),
            (int) ((bgW - 20) * Game.SCALE), (int) ((bgH - 20) * Game.SCALE));

      // Header text
      g.setColor(Color.WHITE);
      DrawCenteredString(g, menu.currentMenu, headerRect, headerFont);

      // Menu Options
      this.drawMenuOptions(g);

      // Cursor
      g2.drawImage(
            pointerImg,
            (int) (cursorX * Game.SCALE), (int) ((menu.cursorY - 30) * Game.SCALE),
            (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);

   }

   private void drawMenuOptions(Graphics g) {
      SaveData saveData = game.getSaveData();
      g.setFont(menuFont);
      // Left side: draw the menu options
      for (int i = 0; i < menu.menuOptions.length; i++) {
         g.drawString(
               menu.menuOptions[i],
               (int) (optionsX * Game.SCALE), (int) ((optionsY + i * menu.menuOptionsDiff) * Game.SCALE));
      }
      // Right side: if the save is started, draw the date and time. Else, draw
      // [empty]
      int xPos = optionsX + 200;
      for (int i = 0; i < 3; i++) {
         if (saveData.getProgValuesFor(i + 1).saveStarted) {
            g.drawString(
                  saveData.getProgValuesFor(i + 1).lastUsed,
                  (int) (xPos * Game.SCALE), (int) ((optionsY + i * menu.menuOptionsDiff) * Game.SCALE));
         } else {
            g.drawString(
                  "[EMPTY]",
                  (int) (xPos * Game.SCALE), (int) ((optionsY + i * menu.menuOptionsDiff) * Game.SCALE));
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
