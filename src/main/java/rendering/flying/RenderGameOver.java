package rendering.flying;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import main_classes.Game;
import rendering.MyImage;
import rendering.MySubImage;
import ui.GameoverOverlay;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

public class RenderGameOver {
   private GameoverOverlay gameOver;
   private Color bgColor = new Color(0, 0, 0, 140);
   private MyImage pointerImg;
   private MySubImage[] deathAnimation;
   private ArrayList<Rectangle> menuRects;

   public RenderGameOver(GameoverOverlay gameoverOverlay) {
      this.gameOver = gameoverOverlay;
      loadImages();
      constructMenuRects();
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);

      this.deathAnimation = HelpMethods2.GetUnscaled1DAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_DEATH_SPRITES),
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
            g2, Color.WHITE, DrawUtils.headerFont,
            "YOU DIED",
            400, 350);

      for (int i = 0; i < gameOver.menuOptions.length; i++) {
         setTextColor(i, g);
         DrawCenteredString(
               g2, gameOver.menuOptions[i],
               menuRects.get(i), DrawUtils.menuFont);
      }

      // Cursor
      DrawUtils.drawImage(
            g2, pointerImg,
            gameOver.cursorX, gameOver.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

   private void setTextColor(int index, Graphics g) {
      if (gameOver.textShouldBeGray(index)) {
         g.setColor(new Color(255, 255, 255, 130));
      } else {
         g.setColor(Color.WHITE);
      }
   }

}
