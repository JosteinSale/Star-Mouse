package rendering.root_renders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import gamestates.MainMenu;
import main_classes.Game;
import rendering.MyImage;
import rendering.SwingRender;
import rendering.misc.RenderInfoChoice;
import rendering.misc.RenderLoadSave;
import rendering.misc.RenderOptionsMenu;
import utils.DrawUtils;
import utils.ResourceLoader;

import static utils.Constants.UI.*;
import static utils.HelpMethods.DrawCenteredString;

public class RenderMainMenu implements SwingRender {

   private MainMenu mainMenu;
   private RenderOptionsMenu rOptionsMenu;
   private RenderLoadSave rLoadSave;
   public MyImage bgImg;
   private MyImage titleImg;
   private MyImage cursorImg;
   public ArrayList<Rectangle> menuRectangles;

   public RenderMainMenu(Game game, RenderOptionsMenu rOptionsMenu, RenderInfoChoice rInfoChoice) {
      this.mainMenu = game.getMainMenu();
      this.rOptionsMenu = rOptionsMenu;
      bgImg = ResourceLoader.getExpImageBackground(
            ResourceLoader.LEVEL_SELECT_BG);
      cursorImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);
      titleImg = ResourceLoader.getExpImageBackground(
            ResourceLoader.MAIN_MENU_TITLE);
      this.makeMenuRectangles();
      this.rLoadSave = new RenderLoadSave(
            game, mainMenu.getLoadSaveMenu(), rInfoChoice);
   }

   private void makeMenuRectangles() {
      this.menuRectangles = new ArrayList<>();
      for (int i = 0; i < mainMenu.alternatives.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE),
               (int) ((450 + i * mainMenu.cursorYStep) * Game.SCALE),
               (int) (200 * Game.SCALE),
               (int) (50 * Game.SCALE));
         menuRectangles.add(rect);
      }
   }

   @Override
   public void draw(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Render the main menu at the bottom, regardless of what is ontop
      drawMainMenu(g2);

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
         DrawUtils.fillScreen(g, new Color(0, 0, 0, mainMenu.alphaFade));
      }
   }

   private void drawMainMenu(Graphics g2) {
      // Background
      DrawUtils.drawImage(
            g2, bgImg,
            (int) mainMenu.bgX, 0,
            Game.GAME_DEFAULT_WIDTH + 55, Game.GAME_DEFAULT_HEIGHT + 50);

      // Text
      g2.setColor(Color.WHITE);
      for (int i = 0; i < mainMenu.alternatives.length; i++) {
         DrawCenteredString(
               g2, mainMenu.alternatives[i],
               menuRectangles.get(i), DrawUtils.menuFont);
      }
      DrawUtils.drawImage(
            g2, titleImg,
            280, 100,
            500, 200);

      // Cursor
      DrawUtils.drawImage(
            g2, cursorImg,
            mainMenu.cursorX, mainMenu.cursorY - CURSOR_HEIGHT / 2,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

   @Override
   public void dispose() {
      // Do nothing
   }

}
