package gamestates.flying;

import java.awt.image.BufferedImage;

import main_classes.Game;

/**
 * The MapManager2's task is to handle everything related to map-specific images
 * and associated numerical values in Flying.
 */
public class MapManager2 {
   public BufferedImage clImg;
   public int clImgHeight;
   public int clImgWidth;
   public float clYOffset;
   protected float clXOffset;

   protected int bgImgHeight;
   public float bgYOffset;

   protected float yProgess = 0;

   public void loadNewMap(int level, int bgImgHeight, Game game) {
      this.bgImgHeight = bgImgHeight;
      this.yProgess = 0;
      this.clImg = game.getImages().getFlyImageCollision("level" + Integer.toString(level) + "_cl.png");
      this.clImgHeight = FlyLevelInfo.getClImgHeight(level) * 3;
      this.clImgWidth = 450 * 3;
      this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
      this.clXOffset = 150;
      this.bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight;
   }

   public void resetOffsetsTo(float skipYPos, float bgConversionRatio) {
      clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150 + skipYPos;
      bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight + (skipYPos * bgConversionRatio);
   }
}
