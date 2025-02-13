package rendering.flying;

import java.awt.Graphics;

import rendering.MyImage;
import gamestates.flying.MapManager2;
import main_classes.Game;
import utils.DrawUtils;
import utils.ResourceLoader;

public class RenderMap2 {
   private MapManager2 map;
   protected MyImage clImg;
   protected MyImage bgImg;
   private int bgImgHeight;

   public RenderMap2(MapManager2 map) {
      this.map = map;
   }

   public void loadNewMap(int lvl, int bgImgHeight) {
      this.bgImg = ResourceLoader.getFlyImageBackground("level" + Integer.toString(lvl) + "_bg.png");
      this.clImg = ResourceLoader.getFlyImageCollision("level" + Integer.toString(lvl) + "_cl.png");
      this.bgImgHeight = bgImgHeight * 3;
   }

   public void drawMaps(Graphics g) {
      DrawUtils.drawImage(
            g, bgImg,
            0, (int) (map.bgYOffset * Game.SCALE),
            Game.GAME_DEFAULT_WIDTH, bgImgHeight);
      DrawUtils.drawImage(
            g, clImg,
            -150, (int) map.clYOffset,
            map.clImgWidth, map.clImgHeight);
   }

   public void dispose() {

   }
}
