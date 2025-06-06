package gamestates.boss_mode;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import main_classes.Game;
import utils.Images;

/**
 * The MapManager3's task is to handle everything related to map-specific images
 * and associated numerical values in BossMode.
 */
public class MapManager3 {
   protected Image scaledBgImg;

   public void loadMap(int bossNr, Images images) {
      BufferedImage bgImg = images.getBossBackground("boss" + Integer.toString(bossNr) + ".png").getImage();
      scaledBgImg = bgImg.getScaledInstance(
            Game.GAME_WIDTH,
            Game.GAME_HEIGHT, Image.SCALE_SMOOTH);
      bgImg.flush();
   }

   public void drawMap(Graphics g) {
      g.drawImage(
            scaledBgImg,
            0, 0, null);
   }

   public void flush() {
      this.scaledBgImg.flush();
      this.scaledBgImg = null;
   }

}
