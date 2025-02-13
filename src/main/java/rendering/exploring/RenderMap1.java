package rendering.exploring;

import java.awt.Graphics;

import gamestates.exploring.MapManager1;
import main_classes.Game;
import rendering.MyImage;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.ResourceLoader;

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

   public RenderMap1(MapManager1 map, int levelIndex, int areaIndex) {
      this.map = map;
      this.loadImgs(levelIndex, areaIndex);
      this.calcLvlOffset();
      this.adjustImageSizes();
   }

   private void loadImgs(Integer levelIndex, Integer areaIndex) {
      String imgName = "level" + levelIndex.toString() +
            "_area" + areaIndex.toString();
      landScapeImg = ResourceLoader.getExpImageLandscape(imgName + "_ls.png");
      bgImg = ResourceLoader.getExpImageBackground(imgName + "_bg.png");
      clImg = ResourceLoader.getExpImageCollision(imgName + "_cl.png");
      fgImg = ResourceLoader.getExpImageForeground(imgName + "_fg.png");
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
