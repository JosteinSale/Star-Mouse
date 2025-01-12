package rendering.root_renders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.StartScreen;
import main_classes.Game;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.ResourceLoader;

public class RenderStartScreen implements SwingRender {

   private StartScreen startScreen;
   private BufferedImage mouseImg;
   private int mouseImgW = 100 * 3;
   private int mouseImgH = 100 * 3;

   public RenderStartScreen(StartScreen startScreen) {
      this.startScreen = startScreen;
      mouseImg = ResourceLoader.getExpImageSprite(ResourceLoader.BASIC_MOUSE);
   }

   @Override
   public void draw(Graphics g) {
      // Background
      DrawUtils.fillRect(
            g, Color.BLACK, 0, 0,
            Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);

      // MouseImg
      DrawUtils.drawImage(
            g, mouseImg,
            Game.GAME_DEFAULT_WIDTH / 2 - mouseImgW / 2,
            Game.GAME_DEFAULT_HEIGHT / 2 - mouseImgH / 2,
            mouseImgW, mouseImgH);

      // Text
      DrawUtils.drawText(
            g, Color.WHITE, DrawUtils.infoFont, "Press SPACE", 430, 600);

      // Fade
      if (startScreen.fadeActive) {
         DrawUtils.fillRect(
               g, new Color(0, 0, 0, startScreen.alphaFade),
               0, 0, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
      }

   }

   @Override
   public void dispose() {
      this.mouseImg.flush();
      this.mouseImg = null;
   }

}
