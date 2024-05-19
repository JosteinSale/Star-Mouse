package gamestates.exploring;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.exploring.PlayerExp;
import main_classes.Game;
import utils.LoadSave;

/**
 * The MapManager1's task is to handle everything related to map-specific images
 * and associated numerical values in Exploring.
 */
public class MapManager1 {

   protected BufferedImage clImg;
   protected BufferedImage landScapeImg;
   protected BufferedImage bgImg;
   protected BufferedImage fgImg;
   protected int bgImgHeight;
   protected int bgImgWidth;
   protected int fgImgWidth;
   protected int fgImgHeight;
   protected int landScapeImgWidth;
   protected int landScapeImgHeight;
   protected int xLevelOffset;
   protected int yLevelOffset;
   protected int xBorder = (int) (0.5 * Game.GAME_DEFAULT_WIDTH);
   protected int yBorder = (int) (0.5 * Game.GAME_DEFAULT_HEIGHT) + 50;
   protected int maxLvlOffsetX;
   protected int maxLvlOffsetY;

   public MapManager1(Integer levelIndex, Integer areaIndex) {
      this.loadImgs(levelIndex, areaIndex);
      this.calcLvlOffset();
      this.adjustImageSizes();
   }

   private void loadImgs(Integer levelIndex, Integer areaIndex) {
      String imgName = "level" + levelIndex.toString() + "_area" + areaIndex.toString();
      landScapeImg = LoadSave.getExpImageLandscape(imgName + "_ls.png");
      bgImg = LoadSave.getExpImageBackground(imgName + "_bg.png");
      clImg = LoadSave.getExpImageCollision(imgName + "_cl.png");
      fgImg = LoadSave.getExpImageForeground(imgName + "_fg.png");
   }

   private void calcLvlOffset() {
      this.maxLvlOffsetX = (bgImg.getWidth() * 3) - Game.GAME_DEFAULT_WIDTH;
      this.maxLvlOffsetY = (bgImg.getHeight() * 3) - Game.GAME_DEFAULT_HEIGHT;
   }

   private void adjustImageSizes() {
      landScapeImgWidth = (int) (landScapeImg.getWidth() * Game.SCALE);
      landScapeImgHeight = (int) (landScapeImg.getHeight() * Game.SCALE);
      bgImgWidth = (int) (bgImg.getWidth() * 3 * Game.SCALE);
      bgImgHeight = (int) (bgImg.getHeight() * 3 * Game.SCALE);
      fgImgWidth = (int) (fgImg.getWidth() * 3 * Game.SCALE);
      fgImgHeight = (int) (fgImg.getHeight() * 3 * Game.SCALE);
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

   public void drawLandscape(Graphics g) {
      g.drawImage(
         landScapeImg,
         (int) ((0 - xLevelOffset * 0.1f) * Game.SCALE), 0,
         landScapeImgWidth, landScapeImgHeight, null);

   }

   public void drawBackground(Graphics g) {
      g.drawImage(
         bgImg,
         (int) ((0 - xLevelOffset) * Game.SCALE),
         (int) ((0 - yLevelOffset) * Game.SCALE),
         bgImgWidth, bgImgHeight, null);
   }

   public void drawForeground(Graphics g) {
      g.drawImage(
         fgImg, 
         (int) ((0 - xLevelOffset) * Game.SCALE), 
         (int) ((0 - yLevelOffset) * Game.SCALE), 
         fgImgWidth, fgImgHeight, null);
   }

}
