package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import main_classes.Game;
import rendering.MyImage;
import utils.DrawUtils;
import utils.Images;

/**
 * The MapManager3's task is to handle everything related to map-specific images
 * and associated numerical values in BossMode.
 */
public class RenderBossMap {
   protected MyImage bgImg;

   public void loadMap(int bossNr, Images images) {
      this.bgImg = images.getBossBackground("boss" + Integer.toString(bossNr) + ".png");
   }

   public void draw(SpriteBatch sb) {
      DrawUtils.drawImage(
            sb, bgImg,
            0, 0,
            Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
   }

}
