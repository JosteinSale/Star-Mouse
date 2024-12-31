package rendering.flying;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main_classes.Game;
import ui.GameoverOverlay;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.HelpMethods.DrawCenteredString;

public class RenderGameOver {
   private GameoverOverlay gameOver;
   private Color bgColor = new Color(0, 0, 0, 140);
   private Font headerFont;
   private Font menuFont;

   private BufferedImage pointerImg;
   private BufferedImage[] deathAnimation;

   public RenderGameOver(GameoverOverlay gameoverOverlay) {
      this.gameOver = gameoverOverlay;
      loadImages();
      loadFonts();
   }

   private void loadImages() {
      this.pointerImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);

      BufferedImage deathImg = ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_DEATH_SPRITES);
      this.deathAnimation = new BufferedImage[26];
      for (int i = 0; i < deathAnimation.length; i++) {
         deathAnimation[i] = deathImg.getSubimage(
               i * 40, 0, 40, 40);
      }
   }

   private void loadFonts() {
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
               (int) (gameOver.playerX * Game.SCALE), (int) (gameOver.playerY * Game.SCALE),
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
         setTextColor(i, g);
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE), (int) ((450 + i * gameOver.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
         DrawCenteredString(g2, gameOver.menuOptions[i], rect, menuFont);
      }

      // Cursor
      g2.drawImage(
            pointerImg,
            (int) (gameOver.cursorX * Game.SCALE), (int) ((gameOver.cursorY - 30) * Game.SCALE),
            (int) (CURSOR_WIDTH * Game.SCALE), (int) (CURSOR_HEIGHT * Game.SCALE), null);
   }

   private void setTextColor(int index, Graphics g) {
      if (gameOver.textShouldBeGray(index)) {
         g.setColor(new Color(255, 255, 255, 130));
      } else {
         g.setColor(Color.WHITE);
      }
   }

}
