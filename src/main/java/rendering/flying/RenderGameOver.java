package rendering.flying;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import ui.GameoverOverlay;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class RenderGameOver {
   private GameoverOverlay gameOver;
   private MyColor bgColor = new MyColor(0, 0, 0, 140);
   private MyColor grayTextColor = new MyColor(255, 255, 255, 130);
   private MyImage pointerImg;
   private MySubImage[] deathAnimation;
   private ArrayList<Rectangle> menuRects;

   public RenderGameOver(GameoverOverlay gameoverOverlay, Images images) {
      this.gameOver = gameoverOverlay;
      loadImages(images);
      constructMenuRects();
   }

   private void loadImages(Images images) {
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);

      this.deathAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.SHIP_DEATH_SPRITES, true),
            26, 40, 40);
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
            "YOU DIED",
            400, 350);

      for (int i = 0; i < gameOver.menuOptions.length; i++) {
         DrawUtils.DrawCenteredString(
               g2, gameOver.menuOptions[i], menuRects.get(i),
               DrawUtils.menuFont, getGameOverTextColor(i));
      }

      // Cursor
      DrawUtils.drawImage(
            g2, pointerImg,
            gameOver.cursorX, gameOver.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

   private MyColor getGameOverTextColor(int index) {
      if (gameOver.textShouldBeGray(index)) {
         return grayTextColor;
      } else {
         return MyColor.WHITE;
      }
   }

}
