package rendering.misc;

import main_classes.Game;
import rendering.MyImage;
import rendering.MyColor;
import rendering.Render;
import ui.InfoChoice;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.INFOBOX_WIDTH;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static utils.Constants.UI.INFOBOX_HEIGHT;

public class RenderInfoChoice implements Render {
   private InfoChoice ic;
   private MyImage background;
   private MyImage cursorImg;
   private Rectangle questionRect;
   private int infoChY;
   private int cursorY;
   private int cursorW;
   private int cursorH;

   public RenderInfoChoice(InfoChoice infoChoice, Images images) {
      this.ic = infoChoice;
      this.background = images.getExpImageSprite(
            Images.INFO_BOX, true);
      this.cursorImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_BLACK, true);
      this.calcInfoChoiceValues();
   }

   private void calcInfoChoiceValues() {
      this.infoChY = 580;
      this.cursorY = infoChY + 90;
      this.cursorW = (int) (CURSOR_WIDTH * 0.6f);
      this.cursorH = (int) (CURSOR_HEIGHT * 0.6f);
      this.questionRect = new Rectangle(
            (int) (this.ic.infoChX * Game.SCALE), (int) ((infoChY + 20) * Game.SCALE),
            (int) (INFOBOX_WIDTH * Game.SCALE), (int) (50 * Game.SCALE));
   }

   @Override
   public void draw(SpriteBatch sb) {
      // Background
      DrawUtils.drawImage(
            sb, background,
            ic.infoChX, infoChY,
            INFOBOX_WIDTH, INFOBOX_HEIGHT);

      // Text - Question, left choice and right choice
      DrawUtils.drawCenteredText(
            sb, ic.question, questionRect,
            DrawUtils.infoFont, MyColor.BLACK);
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            ic.leftChoice,
            ic.infoChX + 150, infoChY + 110);
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            ic.rightChoice,
            ic.infoChX + 400, infoChY + 110);

      // Cursor
      DrawUtils.drawImage(
            sb, cursorImg,
            ic.cursorX, cursorY,
            cursorW, cursorH);
   }
}
