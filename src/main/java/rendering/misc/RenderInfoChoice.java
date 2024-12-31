package rendering.misc;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main_classes.Game;
import rendering.SwingRender;
import ui.InfoChoice;
import utils.ResourceLoader;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;
import static utils.Constants.UI.INFOBOX_WIDTH;
import static utils.Constants.UI.INFOBOX_HEIGHT;
import static utils.HelpMethods.DrawCenteredString;

public class RenderInfoChoice implements SwingRender {
      private InfoChoice ic;
      private BufferedImage background;
      private Font infoFont;
      private Rectangle questionRect;
      private int infoChY;
      private BufferedImage cursorImg;
      private int cursorY;
      private int cursorW;
      private int cursorH;

      public RenderInfoChoice(InfoChoice infoChoice, BufferedImage background, Font infoFont) {
            this.ic = infoChoice;
            this.background = background;
            this.infoFont = infoFont;
            this.cursorImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_BLACK);
            this.calcInfoChoiceValues();
      }

      private void calcInfoChoiceValues() {
            this.infoChY = (int) (580 * Game.SCALE);
            this.cursorY = infoChY + (int) (90 * Game.SCALE);
            this.cursorW = (int) (CURSOR_WIDTH * 0.6f * Game.SCALE);
            this.cursorH = (int) (CURSOR_HEIGHT * 0.6f * Game.SCALE);
            this.questionRect = new Rectangle(
                        this.ic.infoChX, (int) (infoChY + (20 * Game.SCALE)),
                        (int) (INFOBOX_WIDTH * Game.SCALE), (int) (50 * Game.SCALE));
      }

      @Override
      public void draw(Graphics g) {
            // Background
            g.drawImage(
                        background, ic.infoChX, infoChY,
                        (int) (INFOBOX_WIDTH * Game.SCALE),
                        (int) (INFOBOX_HEIGHT * Game.SCALE), null);

            // Text - Question, left choice and right choice
            g.setColor(Color.BLACK);
            g.setFont(infoFont);
            DrawCenteredString(g, ic.question, questionRect, infoFont);
            g.drawString(ic.leftChoice,
                        ic.infoChX + (int) (150 * Game.SCALE),
                        infoChY + (int) (110 * Game.SCALE));
            g.drawString(ic.rightChoice,
                        ic.infoChX + (int) (400 * Game.SCALE),
                        infoChY + (int) (110 * Game.SCALE));

            // Cursor
            g.drawImage(cursorImg, ic.cursorX, cursorY, cursorW, cursorH, null);
      }

      @Override
      public void dispose() {
      }

}
