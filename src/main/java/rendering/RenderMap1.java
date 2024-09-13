package rendering;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gamestates.exploring.MapManager1;
import main_classes.Game;
import utils.ResourceLoader;

public class RenderMap1 implements SwingRender {
   private MapManager1 map;
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

   public RenderMap1(MapManager1 map, int levelIndex, int areaIndex) {
      this.map = map;
      this.loadImgs(levelIndex, areaIndex);
      this.calcLvlOffset();
      this.adjustImageSizes();
   }

   private void loadImgs(Integer levelIndex, Integer areaIndex) {
      String imgName = "level" + levelIndex.toString() + "_area" + areaIndex.toString();
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
      landScapeImgWidth = (int) (landScapeImg.getWidth() * Game.SCALE);
      landScapeImgHeight = (int) (landScapeImg.getHeight() * Game.SCALE);
      bgImgWidth = (int) (bgImg.getWidth() * 3 * Game.SCALE);
      bgImgHeight = (int) (bgImg.getHeight() * 3 * Game.SCALE);
      fgImgWidth = (int) (fgImg.getWidth() * 3 * Game.SCALE);
      fgImgHeight = (int) (fgImg.getHeight() * 3 * Game.SCALE);
   }

   public void drawLandscape(Graphics g) {
      g.drawImage(
            landScapeImg,
            (int) ((0 - map.xLevelOffset * 0.1f) * Game.SCALE), 0,
            landScapeImgWidth, landScapeImgHeight, null);

   }

   public void drawBackground(Graphics g) {
      g.drawImage(
            bgImg,
            (int) ((0 - map.xLevelOffset) * Game.SCALE),
            (int) ((0 - map.yLevelOffset) * Game.SCALE),
            bgImgWidth, bgImgHeight, null);
   }

   public void drawForeground(Graphics g) {
      g.drawImage(
            fgImg,
            (int) ((0 - map.xLevelOffset) * Game.SCALE),
            (int) ((0 - map.yLevelOffset) * Game.SCALE),
            fgImgWidth, fgImgHeight, null);
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
