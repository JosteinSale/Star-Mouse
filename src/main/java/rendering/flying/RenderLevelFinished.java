package rendering.flying;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main_classes.Game;
import ui.LevelFinishedOverlay;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class RenderLevelFinished {
   private LevelFinishedOverlay lf;

   private Font infoFont;
   private Font continueFont;
   private BufferedImage cursorImg;

   private int statusX = 330;
   private int statusY = 390;
   private int yDiff = 50;

   public RenderLevelFinished(LevelFinishedOverlay levelFinished) {
      this.lf = levelFinished;
      this.loadResources();
   }

   private void loadResources() {
      this.infoFont = ResourceLoader.getInfoFont();
      this.continueFont = ResourceLoader.getNameFont();
      this.cursorImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
   }

   public void draw(Graphics g) {
      g.setColor(Color.WHITE);
      g.setFont(infoFont);
      for (int i = 0; i < lf.linesToDraw; i++) {
         int endLetter = lf.lettersPerLine;
         if ((i + 1) == lf.linesToDraw) {
            endLetter = lf.currentLetter - (i * lf.lettersPerLine);
         }
         drawPartialSentence(
               g, lf.statusText[i], endLetter,
               (int) (statusX * Game.SCALE),
               (int) ((statusY + i * yDiff) * Game.SCALE));
      }

      // Draws 'Continue' with cursor if all text has appeared
      if (lf.currentLetter == (lf.lettersPerLine * 3)) {
         g.setFont(continueFont);
         g.drawString(
               "Continue",
               (int) (420 * Game.SCALE),
               (int) (600 * Game.SCALE));
         g.drawImage(
               cursorImg,
               (int) (330 * Game.SCALE),
               (int) (570 * Game.SCALE),
               (int) (CURSOR_WIDTH * Game.SCALE),
               (int) (CURSOR_HEIGHT * Game.SCALE), null);
      }
   }

   private void drawPartialSentence(Graphics g, String s, int endLetter, int x, int y) {
      g.drawString(
            s.substring(0, endLetter), x, y);
   }

}
