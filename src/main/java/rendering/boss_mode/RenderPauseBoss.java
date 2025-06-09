package rendering.boss_mode;

import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.misc.RenderOptionsMenu;
import ui.PauseBoss;
import utils.DrawUtils;
import utils.Images;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class RenderPauseBoss {
   private PauseBoss pause;
   private MyColor bgColor = new MyColor(0, 0, 0, 140);
   private MyImage pointerImg;
   private ArrayList<Rectangle> menuRects;
   private RenderOptionsMenu rOptions;

   public RenderPauseBoss(PauseBoss pause, RenderOptionsMenu rOptions, Images images) {
      this.pause = pause;
      this.rOptions = rOptions;
      this.menuRects = new ArrayList<>();
      constructMenuRects();
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
   }

   private void constructMenuRects() {
      for (int i = 0; i < pause.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               (int) (425 * Game.SCALE),
               (int) ((pause.cursorMinY - 40 + i * pause.menuOptionsDiff) * Game.SCALE),
               (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
         menuRects.add(rect);
      }
   }

   public void draw(SpriteBatch sb) {
      // Background
      DrawUtils.fillScreen(sb, bgColor);

      if (pause.optionsMenu.isActive()) {
         rOptions.draw(sb);
      } else {
         // Text
         DrawUtils.drawText(
               sb, MyColor.WHITE, DrawUtils.headerFont,
               "PAUSE", 450, 200);
         for (int i = 0; i < pause.menuOptions.length; i++) {
            DrawUtils.drawCenteredText(
                  sb, pause.menuOptions[i], menuRects.get(i),
                  DrawUtils.menuFont, MyColor.WHITE);
         }

         // Cursor
         DrawUtils.drawImage(
               sb, pointerImg,
               pause.cursorX, pause.cursorY - 30,
               CURSOR_WIDTH, CURSOR_HEIGHT);
      }
   }
}
