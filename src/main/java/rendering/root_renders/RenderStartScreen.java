package rendering.root_renders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.StartScreen;
import main_classes.Game;
import rendering.SwingRender;
import utils.ResourceLoader;

public class RenderStartScreen implements SwingRender {

   private StartScreen startScreen;
   private BufferedImage mouseImg;
   private int mouseImgW;
   private int mouseImgH;
   private Font font;

   public RenderStartScreen(StartScreen startScreen) {
      this.startScreen = startScreen;
      mouseImg = ResourceLoader.getExpImageSprite(ResourceLoader.BASIC_MOUSE);
      mouseImgW = 100 * 3;
      mouseImgH = 100 * 3;
      font = ResourceLoader.getInfoFont();
   }

   @Override
   public void draw(Graphics g) {
      // Background
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

      // MouseImg
      g.drawImage(mouseImg,
            (int) ((Game.GAME_DEFAULT_WIDTH / 2 - mouseImgW / 2) * Game.SCALE),
            (int) ((Game.GAME_DEFAULT_HEIGHT / 2 - mouseImgH / 2) * Game.SCALE),
            (int) (mouseImgW * Game.SCALE),
            (int) (mouseImgH * Game.SCALE), null);

      // Text
      g.setColor(Color.WHITE);
      g.setFont(font);
      g.drawString(
            "Press SPACE",
            (int) (430 * Game.SCALE), (int) (600 * Game.SCALE));

      // Fade
      if (startScreen.fadeActive) {
         g.setColor(new Color(0, 0, 0, startScreen.alphaFade));
         g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
      }

   }

   @Override
   public void dispose() {
      this.mouseImg.flush();
      this.mouseImg = null;
   }

}
