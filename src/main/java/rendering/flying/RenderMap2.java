package rendering.flying;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import gamestates.flying.MapManager2;
import main_classes.Game;
import utils.ResourceLoader;

public class RenderMap2 {
   private MapManager2 map;
   protected Image scaledClImg;
   protected Image scaledBgImg;

   public RenderMap2(MapManager2 map) {
      this.map = map;
   }

   public void loadNewMap(int lvl, int bgImgHeight) {
      BufferedImage bgImg = ResourceLoader.getFlyImageBackground("level" + Integer.toString(lvl) + "_bg.png");
      scaledClImg = map.clImg.getScaledInstance(
            (int) (map.clImgWidth * Game.SCALE),
            (int) (map.clImgHeight * Game.SCALE), Image.SCALE_SMOOTH);
      scaledBgImg = bgImg.getScaledInstance(
            (Game.GAME_WIDTH),
            (int) (bgImgHeight * Game.SCALE), Image.SCALE_SMOOTH);
      bgImg.flush();
   }

   public void drawMaps(Graphics g) {
      g.drawImage(
            scaledBgImg,
            0, (int) (map.bgYOffset * Game.SCALE), null);
      g.drawImage(
            scaledClImg,
            (int) (-150 * Game.SCALE),
            (int) (map.clYOffset * Game.SCALE), null);
   }

   public void dispose() {
      // Images may already have been flushed if player went into bossMode
      if (this.scaledBgImg != null) {
         this.scaledBgImg.flush();
         this.scaledBgImg = null;
      }
      if (this.scaledClImg != null) {
         this.scaledClImg.flush();
         this.scaledClImg = null;
      }
   }
}
