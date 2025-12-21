package rendering.root_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gamestates.StartScreen;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.Render;
import utils.DrawUtils;
import utils.Images;
import utils.Singleton;

public class RenderStartScreen extends Singleton implements Render {
   private StartScreen startScreen;
   private MyImage mouseImg;
   private int mouseImgW = 100 * 3;
   private int mouseImgH = 100 * 3;

   public RenderStartScreen(StartScreen startScreen, Images images) {
      this.startScreen = startScreen;
      mouseImg = images.getExpImageSprite(Images.BASIC_MOUSE, false);
   }

   @Override
   public void draw(SpriteBatch sb) {
      // Background
      DrawUtils.fillScreen(sb, MyColor.BLACK);

      // MouseImg
      DrawUtils.drawImage(
            sb, mouseImg,
            Game.GAME_DEFAULT_WIDTH / 2 - mouseImgW / 2,
            Game.GAME_DEFAULT_HEIGHT / 2 - mouseImgH / 2,
            mouseImgW, mouseImgH);

      // Text
      DrawUtils.drawText(
            sb, MyColor.WHITE, DrawUtils.infoFont,
            "Press SPACE",
            430, 600);
   }
}
