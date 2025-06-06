package rendering.exploring;

import java.awt.Graphics;

import gamestates.exploring.MapManager1;
import main_classes.Game;
import rendering.MyImage;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.Images;

public class RenderMap1 implements SwingRender {
   private MapManager1 map;
   protected MyImage clImg;
   protected MyImage landScapeImg;
   protected MyImage bgImg;
   protected MyImage fgImg;
   protected int bgImgHeight;
   protected int bgImgWidth;
   protected int fgImgWidth;
   protected int fgImgHeight;
   protected int landScapeImgWidth;
   protected int landScapeImgHeight;

   public RenderMap1(MapManager1 map, Images images, int levelIndex, int areaIndex) {
      this.map = map;
      this.loadImgs(images, levelIndex, areaIndex);
      this.calcLvlOffset();
      this.adjustImageSizes();
   }

   private void loadImgs(Images images, Integer levelIndex, Integer areaIndex) {
      String imgName = "level" + levelIndex.toString() +
            "_area" + areaIndex.toString();
      landScapeImg = images.getExpImageLandscape(imgName + "_ls.png");
      bgImg = images.getExpImageBackground(imgName + "_bg.png");
      clImg = images.getExpImageCollision(imgName + "_cl.png");
      fgImg = images.getExpImageForeground(imgName + "_fg.png");
   }

   private void calcLvlOffset() {
      map.maxLvlOffsetX = (bgImg.getWidth() * 3) - Game.GAME_DEFAULT_WIDTH;
      map.maxLvlOffsetY = (bgImg.getHeight() * 3) - Game.GAME_DEFAULT_HEIGHT;
   }

   private void adjustImageSizes() {
      landScapeImgWidth = landScapeImg.getWidth();
      landScapeImgHeight = landScapeImg.getHeight();
      bgImgWidth = bgImg.getWidth() * 3;
      bgImgHeight = bgImg.getHeight() * 3;
      fgImgWidth = fgImg.getWidth() * 3;
      fgImgHeight = fgImg.getHeight() * 3;
   }

   public void drawLandscape(Graphics g) {
      DrawUtils.drawImage(
            g, landScapeImg,
            (int) (0 - map.xLevelOffset * 0.1f), 0,
            landScapeImgWidth, landScapeImgHeight);
   }

   public void drawBackground(Graphics g) {
      DrawUtils.drawImage(
            g, bgImg,
            0 - map.xLevelOffset, 0 - map.yLevelOffset,
            bgImgWidth, bgImgHeight);
   }

   public void drawForeground(Graphics g) {
      DrawUtils.drawImage(
            g, fgImg,
            0 - map.xLevelOffset, 0 - map.yLevelOffset,
            fgImgWidth, fgImgHeight);
   }

   @Override
   public void draw(Graphics g) {
   }

   @Override
   public void dispose() {
      this.bgImg.flush();
      this.fgImg.flush();
      this.landScapeImg.flush();
      this.bgImg = null;
      this.fgImg = null;
      this.landScapeImg = null;
   }

}
