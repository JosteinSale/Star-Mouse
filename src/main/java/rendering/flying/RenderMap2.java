package rendering.flying;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game_states.flying.MapManager2;
import rendering.MyImage;
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
      this.clImg = images.getFlyImageForeground("level" + Integer.toString(lvl) + "_cl.png");
      this.bgImgHeight = bgImgHeight;
   }

   public void drawMaps(SpriteBatch sb) {
      DrawUtils.drawImage(
            sb, bgImg,
            0, (int) map.bgYOffset,
            Game.GAME_DEFAULT_WIDTH, bgImgHeight);
      DrawUtils.drawImage(
            sb, clImg,
            -150, (int) map.clYOffset,
            map.clImgWidth, map.clImgHeight);
   }
}
