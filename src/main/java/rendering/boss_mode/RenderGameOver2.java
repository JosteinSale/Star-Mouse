package rendering.boss_mode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

import main_classes.Game;
import ui.GameoverOverlay2;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.ResourceLoader;

public class RenderGameOver2 {
   private GameoverOverlay2 gameOver;
   private Color bgColor = new Color(0, 0, 0, 140);
   private BufferedImage pointerImg;
   private BufferedImage[] deathAnimation;
   private ArrayList<Rectangle> menuRects;

   public RenderGameOver2(GameoverOverlay2 gameOver) {
      this.gameOver = gameOver;
      this.loadResources();
      this.constructMenuRects();
   }

   private void loadResources() {
      this.pointerImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);
      BufferedImage deathImg = ResourceLoader.getFlyImageSprite(
            ResourceLoader.SHIP_DEATH_SPRITES);
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
         DrawUtils.drawImage(
               g, deathAnimation[gameOver.aniIndex],
               (int) gameOver.playerX, (int) gameOver.playerY,
               120, 120);

      }
   }

   private void drawMenu(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Background
      g.setColor(bgColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      // Text
      DrawUtils.drawText(
            g2, Color.WHITE, DrawUtils.headerFont,
            "YOU DIED", 400, 350);

      for (int i = 0; i < gameOver.menuOptions.length; i++) {
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

}
