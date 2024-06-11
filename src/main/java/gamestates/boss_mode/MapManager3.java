package gamestates.boss_mode;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import main_classes.Game;
import utils.LoadSave;

/**
 * The MapManager3's task is to handle everything related to map-specific images
 * and associated numerical values in BossMode.
 */
public class MapManager3 {
   protected Image scaledBgImg;

   public void loadMap(int bossNr) {
      BufferedImage bgImg = LoadSave.getBossBackground("boss" + Integer.toString(bossNr) + ".png");
      scaledBgImg = bgImg.getScaledInstance(
         Game.GAME_WIDTH,
         Game.GAME_HEIGHT, Image.SCALE_SMOOTH);
   }

   public void drawMap(Graphics g) {
      g.drawImage(
         scaledBgImg, 
         0, 0, null);
   }

}
