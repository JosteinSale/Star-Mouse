package rendering.flying;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import ui.LevelFinishedOverlay;
import utils.DrawUtils;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class RenderLevelFinished {
   private LevelFinishedOverlay lf;
   private BufferedImage cursorImg;

   private int statusX = 330;
   private int statusY = 390;
   private int yDiff = 50;

   public RenderLevelFinished(LevelFinishedOverlay levelFinished) {
      this.lf = levelFinished;
      this.cursorImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_WHITE);
   }

   public void draw(Graphics g) {
      DrawUtils.fillScreen(g, Color.BLACK);
      drawHeader(g);
      if (lf.areLettersAppearing()) {
         drawStats(g);
      }
      if (lf.hasContinueAppeared()) {
         drawContinue(g);
      }
   }

   private void drawStats(Graphics g) {
      for (int i = 0; i < lf.linesToDraw; i++) {
         int endLetter = lf.lettersPerLine;
         if ((i + 1) == lf.linesToDraw) {
            endLetter = lf.currentLetter - (i * lf.lettersPerLine);
         }
         DrawUtils.drawText(
               g, Color.WHITE, DrawUtils.infoFont,
               lf.statusText[i].substring(0, endLetter),
               statusX, statusY + i * yDiff);
      }
   }

   private void drawHeader(Graphics g) {
      DrawUtils.drawText(
            g, new Color(255, 255, 255, lf.headerAlpha), DrawUtils.headerFont,
            "Level Finished",
            320, 200);
   }

   private void drawContinue(Graphics g) {
      DrawUtils.drawText(
            g, Color.WHITE, DrawUtils.nameFont,
            "Continue",
            420, 600);
      DrawUtils.drawImage(
            g, cursorImg,
            330, 570,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

}
