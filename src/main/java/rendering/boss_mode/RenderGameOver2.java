package rendering.boss_mode;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.Render;
import ui.GameoverOverlay2;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

public class RenderGameOver2 implements Render {
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

   @Override
   public void draw(SpriteBatch sb) {
      if (gameOver.deathAnimationActive) {
         drawDeathAnimation(sb);
      } else {
         drawMenu(sb);
      }
   }

   private void drawDeathAnimation(SpriteBatch sb) {
      if (gameOver.aniIndex < deathAnimation.length) { // Will draw nothing at index 26 - 30
         DrawUtils.drawSubImage(
               sb, deathAnimation[gameOver.aniIndex],
               (int) gameOver.playerX, (int) gameOver.playerY,
               120, 120);

      }
   }

   private void drawMenu(SpriteBatch sb) {
      // Background
      DrawUtils.fillScreen(sb, bgColor);

      // Text
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.headerFont,
            "YOU DIED", 400, 350);

      for (int i = 0; i < gameOver.menuOptions.length; i++) {
         DrawUtils.drawCenteredText(
               sb, gameOver.menuOptions[i], menuRects.get(i),
               DrawUtils.menuFont, MyColor.WHITE);
      }

      // Cursor
      DrawUtils.drawImage(
            sb, pointerImg,
            gameOver.cursorX, gameOver.cursorY - 30,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

}
