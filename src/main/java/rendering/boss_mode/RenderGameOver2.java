package rendering.boss_mode;

import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import ui.GameoverOverlay2;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

public class RenderGameOver2 {
   private GameoverOverlay2 gameOver;
   private MyColor bgColor = new MyColor(0, 0, 0, 140);
   private MyImage pointerImg;
   private MySubImage[] deathAnimation;
   private ArrayList<Rectangle> menuRects;

   public RenderGameOver2(GameoverOverlay2 gameOver, Images images) {
      this.gameOver = gameOver;
      this.loadResources(images);
      this.constructMenuRects();
   }

   private void loadResources(Images images) {
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
      MyImage deathImg = images.getFlyImageSprite(
            Images.SHIP_DEATH_SPRITES, true);
      this.deathAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            deathImg, 26, 40, 40);
   }

   private void constructMenuRects() {
      this.menuRects = new ArrayList<>();
      for (int i = 0; i < gameOver.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE),
               (int) ((450 + i * gameOver.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE),
               (int) (50 * Game.SCALE));
         menuRects.add(rect);
      }
   }

   public void draw(Graphics g) {
      if (gameOver.deathAnimationActive) {
         drawDeathAnimation(g);
      } else {
         drawMenu(g);
      }
   }

   private void drawDeathAnimation(Graphics g) {
      if (gameOver.aniIndex < deathAnimation.length) { // Will draw nothing at index 26 - 30
         DrawUtils.drawSubImage(
               g, deathAnimation[gameOver.aniIndex],
               (int) gameOver.playerX, (int) gameOver.playerY,
               120, 120);

      }
   }

   private void drawMenu(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Background
      DrawUtils.fillScreen(g2, bgColor);

      // Text
      DrawUtils.drawText(
            g2, MyColor.WHITE, DrawUtils.headerFont,
            "YOU DIED", 400, 350);

      for (int i = 0; i < gameOver.menuOptions.length; i++) {
         DrawUtils.DrawCenteredString(
               g2, gameOver.menuOptions[i], menuRects.get(i),
               DrawUtils.menuFont, MyColor.WHITE);
      }

      // Cursor
      DrawUtils.drawImage(
            g2, pointerImg,
            gameOver.cursorX, gameOver.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

}
