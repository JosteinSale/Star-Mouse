package gamestates.flying;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main_classes.Game;
import utils.ResourceLoader;

import java.awt.Image;

/**
 * The MapManager2's task is to handle everything related to map-specific images
 * and associated numerical values in Flying.
 */
public class MapManager2 {
   protected BufferedImage clImg;
   protected Image scaledClImg;
   protected Image scaledBgImg;
   protected int clImgHeight;
   protected int clImgWidth;
   protected int bgImgHeight;
   protected float clYOffset;
   protected float clXOffset;
   protected float bgYOffset;
   protected float yProgess = 0;

   public MapManager2(int lvl, int bgImgHeight) {
      this.bgImgHeight = bgImgHeight;
      this.loadMapAndOffsets(lvl, bgImgHeight);
   }

   private void loadMapAndOffsets(int lvl, int bgImgHeight) {
      this.yProgess = 0;
      this.clImg = ResourceLoader.getFlyImageCollision("level" + Integer.toString(lvl) + "_cl.png");
      this.clImgHeight = clImg.getHeight() * 3;
      this.clImgWidth = clImg.getWidth() * 3;
      this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
      this.clXOffset = 150;
      BufferedImage bgImg = ResourceLoader.getFlyImageBackground("level" + Integer.toString(lvl) + "_bg.png");
      this.bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight;
      scaledClImg = clImg.getScaledInstance(
            (int) (clImgWidth * Game.SCALE),
            (int) (clImgHeight * Game.SCALE), Image.SCALE_SMOOTH);
      scaledBgImg = bgImg.getScaledInstance(
            (Game.GAME_WIDTH),
            (int) (bgImgHeight * Game.SCALE), Image.SCALE_SMOOTH);
      bgImg.flush();
   }

   public void drawMaps(Graphics g) {
      g.drawImage(
            scaledBgImg,
            0, (int) (bgYOffset * Game.SCALE), null);
      g.drawImage(
            scaledClImg,
            (int) (-150 * Game.SCALE),
            (int) (clYOffset * Game.SCALE), null);
   }

   public void resetOffsetsTo(float skipYPos, float bgConversionRatio) {
      clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150 + skipYPos;
      bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight + (skipYPos * bgConversionRatio);
   }

   public void flush() {
      this.clImg.flush();
      this.scaledBgImg.flush();
      this.scaledClImg.flush();
   }
}
