package rendering.root_renders;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import gamestates.MainMenu;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.Render;
import rendering.misc.RenderInfoChoice;
import rendering.misc.RenderLoadSave;
import rendering.misc.RenderOptionsMenu;
import utils.DrawUtils;
import utils.Images;
import utils.Singleton;

import static utils.Constants.UI.*;

public class RenderMainMenu extends Singleton implements Render {

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
      Images images = game.getImages();
      bgImg = images.getMiscImage(
            Images.LEVEL_SELECT_BG, true);
      cursorImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
      titleImg = images.getMiscImage(
            Images.MAIN_MENU_TITLE, true);
      this.makeMenuRectangles();
      this.rLoadSave = new RenderLoadSave(
            game, mainMenu.getLoadSaveMenu(), rInfoChoice);
   }

   private void makeMenuRectangles() {
      this.menuRectangles = new ArrayList<>();
      for (int i = 0; i < mainMenu.alternatives.length; i++) {
         Rectangle rect = new Rectangle(
               425,
               450 + i * mainMenu.cursorYStep,
               200,
               50);
         menuRectangles.add(rect);
      }
   }

   @Override
   public void draw(SpriteBatch sb) {

      // Render the main menu at the bottom, regardless of what is ontop
      drawMainMenu(sb);

      // Options
      if (mainMenu.getOptionsMenu().isActive()) {
         rOptionsMenu.draw(sb);
      }
      // LoadSave Menu
      else if (mainMenu.getLoadSaveMenu().isActive()) {
         rLoadSave.draw(sb);
      }

      // Fade
      if (mainMenu.fadeInActive || mainMenu.fadeOutActive) {
         DrawUtils.fillScreen(sb, new MyColor(0, 0, 0, mainMenu.alphaFade));
      }
   }

   private void drawMainMenu(SpriteBatch sb) {
      // Background
      DrawUtils.drawImage(
            sb, bgImg,
            (int) mainMenu.bgX, 0,
            Game.GAME_DEFAULT_WIDTH + 55, Game.GAME_DEFAULT_HEIGHT + 50);

      // Text
      for (int i = 0; i < mainMenu.alternatives.length; i++) {
         DrawUtils.drawCenteredText(
               sb, mainMenu.alternatives[i], menuRectangles.get(i),
               DrawUtils.menuFont, MyColor.WHITE);
      }
      DrawUtils.drawImage(
            sb, titleImg,
            280, 100,
            500, 200);

      // Cursor
      DrawUtils.drawImage(
            sb, cursorImg,
            mainMenu.cursorX, mainMenu.cursorY - CURSOR_HEIGHT / 2,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

}
