package rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.MainMenu;
import main_classes.Game;
import utils.ResourceLoader;

import static utils.Constants.UI.*;
import static utils.HelpMethods.DrawCenteredString;

public class RenderMainMenu implements SwingRender {

   private MainMenu mainMenu;
   private RenderOptionsMenu rOptionsMenu;
   private RenderLoadSave rLoadSave;
   public BufferedImage bgImg;
   private BufferedImage titleImg;
   private BufferedImage cursorImg;
   private Font menuFont;
   public ArrayList<Rectangle> menuRectangles;

   public RenderMainMenu(Game game, MainMenu mainMenu) {
      this.mainMenu = mainMenu;
      bgImg = ResourceLoader.getExpImageBackground(ResourceLoader.LEVEL_SELECT_BG);
      cursorImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
      titleImg = ResourceLoader.getExpImageBackground(ResourceLoader.MAIN_MENU_TITLE);
      menuFont = ResourceLoader.getNameFont();
      this.makeMenuRectangles();
      this.rOptionsMenu = new RenderOptionsMenu(mainMenu.getOptionsMenu(), mainMenu.getOptionsMenu().getControlsMenu());
      this.rLoadSave = new RenderLoadSave(game, mainMenu.getLoadSaveMenu());
   }

   private void makeMenuRectangles() {
      this.menuRectangles = new ArrayList<>();
      for (int i = 0; i < mainMenu.alternatives.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE), (int) ((450 + i * mainMenu.cursorYStep) * Game.SCALE),
               (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
         menuRectangles.add(rect);
      }
   }

   @Override
   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Background
      g.drawImage(bgImg,
            (int) mainMenu.bgX, 0, Game.GAME_WIDTH + 55, Game.GAME_HEIGHT + 50, null);

      // Text
      g.setColor(Color.WHITE);
      g.setFont(menuFont);
      for (int i = 0; i < mainMenu.alternatives.length; i++) {
         DrawCenteredString(g2, mainMenu.alternatives[i], menuRectangles.get(i), menuFont);
      }
      g.drawImage(
            titleImg,
            (int) (280 * Game.SCALE),
            (int) (100 * Game.SCALE),
            (int) (500 * Game.SCALE),
            (int) (200 * Game.SCALE),
            null);

      // Cursor
      g.drawImage(
            cursorImg,
            (int) (mainMenu.cursorX * Game.SCALE),
            (int) ((mainMenu.cursorY - CURSOR_HEIGHT / 2) * Game.SCALE),
            (int) (CURSOR_WIDTH * Game.SCALE),
            (int) (CURSOR_HEIGHT * Game.SCALE),
            null);

      // Options
      if (mainMenu.getOptionsMenu().isActive()) {
         rOptionsMenu.draw(g);
      }
      // LoadSave Menu
      else if (mainMenu.getLoadSaveMenu().isActive()) {
         rLoadSave.draw(g);
      }

      // Fade
      if (mainMenu.fadeInActive || mainMenu.fadeOutActive) {
         g.setColor(new Color(0, 0, 0, mainMenu.alphaFade));
         g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
      }
   }

   @Override
   public void dispose() {
      // Do nothing
   }

   public BufferedImage getBgImg() {
      return this.bgImg;
   }

}
