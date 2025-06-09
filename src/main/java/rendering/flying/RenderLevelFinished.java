package rendering.flying;

import rendering.MyColor;
import rendering.MyImage;
import ui.LevelFinishedOverlay;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RenderLevelFinished {
   private LevelFinishedOverlay lf;
   private MyImage cursorImg;

   private int statusX = 330;
   private int statusY = 390;
   private int yDiff = 50;

   public RenderLevelFinished(LevelFinishedOverlay levelFinished, Images images) {
      this.lf = levelFinished;
      this.cursorImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
   }

   public void draw(SpriteBatch sb) {
      DrawUtils.fillScreen(sb, MyColor.BLACK);
      drawHeader(sb);
      if (lf.areLettersAppearing()) {
         drawStats(sb);
      }
      if (lf.hasContinueAppeared()) {
         drawContinue(sb);
      }
   }

   private void drawStats(SpriteBatch sb) {
      for (int i = 0; i < lf.linesToDraw; i++) {
         int endLetter = lf.lettersPerLine;
         if ((i + 1) == lf.linesToDraw) {
            endLetter = lf.currentLetter - (i * lf.lettersPerLine);
         }
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.infoFont,
               lf.statusText[i].substring(0, endLetter),
               statusX, statusY + i * yDiff);
      }
   }

   private void drawHeader(SpriteBatch sb) {
      DrawUtils.drawText(
            sb, new MyColor(255, 255, 255, lf.headerAlpha), DrawUtils.headerFont,
            "Level Finished",
            320, 200);
   }

   private void drawContinue(SpriteBatch sb) {
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.nameFont,
            "Continue",
            420, 600);
      DrawUtils.drawImage(
            sb, cursorImg,
            330, 570,
            CURSOR_WIDTH, CURSOR_HEIGHT);
   }

}
