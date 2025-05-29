package rendering.misc;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

import main_classes.Game;
import rendering.MyImage;
import rendering.MyColor;
import rendering.SwingRender;
import ui.InfoChoice;
import utils.DrawUtils;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.INFOBOX_WIDTH;
import static utils.Constants.UI.INFOBOX_HEIGHT;
import static utils.HelpMethods.DrawCenteredString;

public class RenderInfoChoice implements SwingRender {
   private InfoChoice ic;
   private MyImage background;
   private MyImage cursorImg;
   private Rectangle questionRect;
   private int infoChY;
   private int cursorY;
   private int cursorW;
   private int cursorH;

   public RenderInfoChoice(InfoChoice infoChoice) {
      this.ic = infoChoice;
      this.background = ResourceLoader.getExpImageSprite(
            ResourceLoader.INFO_BOX);
      this.cursorImg = ResourceLoader.getExpImageSprite(
            ResourceLoader.CURSOR_SPRITE_BLACK);
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
   public void draw(Graphics g) {
      // Background
      DrawUtils.drawImage(
            g, background,
            ic.infoChX, infoChY,
            INFOBOX_WIDTH, INFOBOX_HEIGHT);

      // Text - Question, left choice and right choice
      g.setColor(Color.BLACK);
      DrawCenteredString(g, ic.question, questionRect, DrawUtils.infoFont);
      DrawUtils.drawText(
            g, MyColor.BLACK, DrawUtils.infoFont,
            ic.leftChoice,
            ic.infoChX + 150, infoChY + 110);
      DrawUtils.drawText(
            g, MyColor.BLACK, DrawUtils.infoFont,
            ic.rightChoice,
            ic.infoChX + 400, infoChY + 110);

      // Cursor
      DrawUtils.drawImage(
            g, cursorImg,
            ic.cursorX, cursorY,
            cursorW, cursorH);
   }

   @Override
   public void dispose() {
   }

}
