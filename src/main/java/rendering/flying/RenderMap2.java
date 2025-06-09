package rendering.flying;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rendering.MyImage;
import gamestates.flying.MapManager2;
import main_classes.Game;
import utils.DrawUtils;
import utils.Images;

public class RenderMap2 {
   private Images images;
   private MapManager2 map;
   protected MyImage fgImg;
   protected MyImage bgImg;
   private int bgImgHeight;

   public RenderMap2(MapManager2 map, Images images) {
      this.images = images;
      this.map = map;
   }

   public void loadNewMap(int lvl, int bgImgHeight) {
      this.bgImg = images.getFlyImageBackground("level" + Integer.toString(lvl) + "_bg.png");
      this.fgImg = images.getFlyImageForeground("level" + Integer.toString(lvl) + "_cl.png");
      this.bgImgHeight = bgImgHeight;
   }

   public void drawMaps(SpriteBatch sb) {
      DrawUtils.drawImage(
            sb, bgImg,
            0, (int) map.bgYOffset,
            Game.GAME_DEFAULT_WIDTH, bgImgHeight);
      DrawUtils.drawImage(
            sb, fgImg,
            -150, (int) map.clYOffset,
            map.clImgWidth, map.clImgHeight);
   }

   public void dispose() {

   }
}
