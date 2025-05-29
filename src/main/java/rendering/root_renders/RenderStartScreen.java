package rendering.root_renders;

import java.awt.Graphics;

import gamestates.StartScreen;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.ResourceLoader;

public class RenderStartScreen implements SwingRender {

   private StartScreen startScreen;
   private MyImage mouseImg;
   private int mouseImgW = 100 * 3;
   private int mouseImgH = 100 * 3;

   public RenderStartScreen(StartScreen startScreen) {
      this.startScreen = startScreen;
      mouseImg = ResourceLoader.getExpImageSprite(ResourceLoader.BASIC_MOUSE);
   }

   @Override
   public void draw(Graphics g) {
      // Background
      DrawUtils.fillScreen(g, MyColor.BLACK);

      // MouseImg
      DrawUtils.drawImage(
            g, mouseImg,
            Game.GAME_DEFAULT_WIDTH / 2 - mouseImgW / 2,
            Game.GAME_DEFAULT_HEIGHT / 2 - mouseImgH / 2,
            mouseImgW, mouseImgH);

      // Text
      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.infoFont,
            "Press SPACE",
            430, 600);

      // Fade
      if (startScreen.fadeActive) {
         DrawUtils.fillScreen(g, new MyColor(0, 0, 0, startScreen.alphaFade));
      }

   }

   @Override
   public void dispose() {
      this.mouseImg.flush();
      this.mouseImg = null;
   }

}
