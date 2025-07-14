package rendering.flying;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import rendering.MyColor;
import rendering.MyImage;
import rendering.misc.RenderOptionsMenu;
import ui.PauseFlying;
import utils.DrawUtils;
import utils.Images;

import static utils.Constants.UI.CURSOR_HEIGHT;
import static utils.Constants.UI.CURSOR_WIDTH;

public class RenderPauseFly {
   private PauseFlying pause;
   private RenderOptionsMenu rOptions;
   private ArrayList<Rectangle> menuRects;
   private MyColor bgColor = new MyColor(0, 0, 0, 140);
   private MyImage pointerImg;

   public RenderPauseFly(PauseFlying pause, RenderOptionsMenu rOptions, Images images) {
      this.pause = pause;
      this.rOptions = rOptions;
      this.pointerImg = images.getExpImageSprite(
            Images.CURSOR_SPRITE_WHITE, true);
      this.constructMenuRects();
   }

   private void constructMenuRects() {
      this.menuRects = new ArrayList<>();
      for (int i = 0; i < pause.menuOptions.length; i++) {
         Rectangle rect = new Rectangle(
               425, pause.cursorMinY - 40 + i * pause.menuOptionsDiff,
               200, 50);
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
               "PAUSE",
               450, 200);
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
