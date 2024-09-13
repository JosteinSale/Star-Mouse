package gamestates.exploring;

import entities.exploring.PlayerExp;
import main_classes.Game;

/**
 * The MapManager1's task is to handle everything related to map-specific images
 * and associated numerical values in Exploring.
 */
public class MapManager1 {

   public int xLevelOffset;
   public int yLevelOffset;
   public int xBorder = (int) (0.5 * Game.GAME_DEFAULT_WIDTH);
   public int yBorder = (int) (0.5 * Game.GAME_DEFAULT_HEIGHT) + 50;
   public int maxLvlOffsetX;
   public int maxLvlOffsetY;
   public boolean cameraDeattached = false;

   public MapManager1() {
   }

   public void adjustOffsets(PlayerExp player) {
      xLevelOffset = getOffset(
            (int) player.getHitbox().x,
            xLevelOffset,
            xBorder,
            maxLvlOffsetX);
      yLevelOffset = getOffset(
            (int) player.getHitbox().y,
            yLevelOffset,
            yBorder,
            maxLvlOffsetY);
   }

   private int getOffset(int playerXY, int XYOffset, int border, int maxOffset) {
      int diffX = playerXY - XYOffset;
      int offset = XYOffset;

      if (diffX > border) {
         offset += diffX - border;
      } else if (diffX < border) {
         offset += diffX - border;
      }
      if (offset > maxOffset) {
         offset = maxOffset;
      } else if (offset < 0) {
         offset = 0;
      }
      return offset;
   }

   public boolean cameraDeattached() {
      return cameraDeattached;
   }
}
