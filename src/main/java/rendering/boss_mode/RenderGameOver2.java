package rendering.boss_mode;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

import main_classes.Game;
import ui.GameoverOverlay2;
import utils.HelpMethods2;
import utils.ResourceLoader;

public class RenderGameOver2 {
   private GameoverOverlay2 gameOver;
   private Color bgColor = new Color(0, 0, 0, 140);
   private BufferedImage pointerImg;
   private BufferedImage[] deathAnimation;
   private Font headerFont;
   private Font menuFont;

   public RenderGameOver2(GameoverOverlay2 gameOver) {
      this.gameOver = gameOver;
      this.loadResources();
   }

   private void loadResources() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
      BufferedImage deathImg = ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_DEATH_SPRITES);
      this.deathAnimation = HelpMethods2.GetSimpleAnimationArray(deathImg, 26, 40, 40);
      this.headerFont = ResourceLoader.getHeaderFont();
      this.menuFont = ResourceLoader.getNameFont();
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
         g.drawImage(
               deathAnimation[gameOver.aniIndex],
               (int) (gameOver.playerX * Game.SCALE),
               (int) (gameOver.playerY * Game.SCALE),
               (int) (120 * Game.SCALE),
               (int) (120 * Game.SCALE), null);
      }
   }

   private void drawMenu(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;

      // Background
      g.setColor(bgColor);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      // Text
      g.setFont(headerFont);
      g.setColor(Color.WHITE);
      g.drawString("YOU DIED", (int) (400 * Game.SCALE), (int) (350 * Game.SCALE));

      for (int i = 0; i < gameOver.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE),
               (int) ((450 + i * gameOver.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE),
               (int) (50 * Game.SCALE));
         DrawCenteredString(g2, gameOver.menuOptions[i], rect, menuFont);
      }

      // Cursor
      g2.drawImage(
            pointerImg,
            (int) (gameOver.cursorX * Game.SCALE),
            (int) ((gameOver.cursorY - 30) * Game.SCALE),
            (int) (CURSOR_WIDTH * Game.SCALE),
            (int) (CURSOR_HEIGHT * Game.SCALE), null);
   }

}
