package game_states.flying;

import static utils.Constants.Flying.COLLISION_MAP_Y_OFFSET;
import static utils.Constants.Flying.COLLISION_MAP_X_OFFSET;
import static utils.Constants.Flying.COLLISION_MAP_WIDTH;

import entities.MyCollisionImage;
import main_classes.Game;

/**
 * The MapManager2's task is to handle everything related to map-specific images
 * and associated numerical values in Flying.
 */
public class MapManager2 {
   public MyCollisionImage clImg;
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
      this.clImgWidth = COLLISION_MAP_WIDTH;
      this.clXOffset = COLLISION_MAP_X_OFFSET;
      this.clYOffset = clImgResetPos();
      this.bgYOffset = bgImgResetPos();
   }

   private int clImgResetPos() {
      return Game.GAME_DEFAULT_HEIGHT - clImgHeight + COLLISION_MAP_Y_OFFSET;
   }

   private int bgImgResetPos() {
      return Game.GAME_DEFAULT_HEIGHT - bgImgHeight;
   }

   public void resetOffsetsTo(float skipYPos, float bgConversionRatio) {
      clYOffset = clImgResetPos() + skipYPos;
      bgYOffset = bgImgResetPos() + (skipYPos * bgConversionRatio);
   }
}
