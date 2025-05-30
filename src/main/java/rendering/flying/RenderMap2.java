package rendering.flying;

import java.awt.Graphics;

import rendering.MyImage;
import gamestates.flying.MapManager2;
import main_classes.Game;
import utils.DrawUtils;
import utils.Images;

public class RenderMap2 {
   private Images images;
   private MapManager2 map;
   protected MyImage clImg;
   protected MyImage bgImg;
   private int bgImgHeight;

   public RenderMap2(MapManager2 map, Images images) {
      this.images = images;
      this.map = map;
   }

   public void loadNewMap(int lvl, int bgImgHeight) {
      this.bgImg = images.getFlyImageBackground("level" + Integer.toString(lvl) + "_bg.png");
      this.clImg = images.getFlyImageCollision("level" + Integer.toString(lvl) + "_cl.png");
      this.bgImgHeight = bgImgHeight;
   }

   public void drawMaps(Graphics g) {
      DrawUtils.drawImage(
            g, bgImg,
            0, (int) map.bgYOffset,
            Game.GAME_DEFAULT_WIDTH, bgImgHeight);
      DrawUtils.drawImage(
            g, clImg,
            -150, (int) map.clYOffset,
            map.clImgWidth, map.clImgHeight);
   }

   public void dispose() {

   }
}
