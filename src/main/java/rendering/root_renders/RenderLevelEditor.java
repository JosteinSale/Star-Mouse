package rendering.root_renders;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import gamestates.LevelEditor;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.flying.EntityImages;
import utils.DrawUtils;
import utils.ResourceLoader;

public class RenderLevelEditor {

   private LevelEditor le;
   private EntityImages entityImages;
   private MyImage clImg;

   public RenderLevelEditor(LevelEditor levelEditor, EntityImages entityImages) {
      this.le = levelEditor;
      this.entityImages = entityImages;
      this.clImg = ResourceLoader.getFlyImageCollision(
            "level" + Integer.toString(levelEditor.level) + "_cl.png");
   }

   public void draw(Graphics g) {
      drawMapAndText(g);
      drawEntities(g);
      drawCursor(g);
   }

   private void drawMapAndText(Graphics g) {
      // Map
      DrawUtils.drawImage(
            g, clImg,
            (int) le.clXOffset, le.clYOffset,
            le.clImgWidth, le.clImgHeight);

      // Top text
      DrawUtils.drawText(
            g, MyColor.BLACK, DrawUtils.infoFont,
            "direction : " + Integer.toString(le.curDirection),
            20, 20);
      DrawUtils.drawText(
            g, MyColor.BLACK, DrawUtils.infoFont,
            "shootTimer : " + Integer.toString(le.shootTimer),
            20, 50);
      DrawUtils.drawText(
            g, MyColor.BLACK, DrawUtils.infoFont,
            "y :" + Integer.toString(le.mapYOffset),
            700, 20);
   }

   private void drawEntities(Graphics g) {
      for (int i = 0; i < le.addedEntityNames.size(); i++) {
         EntityInfo info = le.entityFactory.getEntityInfo(le.addedEntityNames.get(i));
         Rectangle2D hitbox = le.hitboxes.get(i);

         // Text
         DrawUtils.drawText(
               g, MyColor.BLACK, DrawUtils.infoFont,
               Integer.toString(le.shootTimers.get(i)),
               (int) hitbox.getX(),
               (int) (hitbox.getY() - le.mapYOffset - 20));

         // Hitbox
         DrawUtils.fillRect(
               g, MyColor.BLACK,
               hitbox.getX(), hitbox.getY() - le.mapYOffset,
               hitbox.getWidth(), hitbox.getHeight());

         // Image
         int dir = le.directions.get(i);
         int xOffset = info.drawOffsetX;
         int yOffset = info.drawOffsetY;
         int spriteW = info.spriteW;
         int spriteH = info.spriteH;
         if (dir == -1) {
            xOffset -= 3 * spriteW;
         }
         MySubImage img = entityImages.getImageFor(
               info.typeConstant, info.editorImgRow, info.editorImgCol);
         DrawUtils.drawSubImage(
               g, img,
               (int) (hitbox.getX() - xOffset),
               (int) (hitbox.getY() - yOffset - le.mapYOffset),
               spriteW * 3 * dir,
               spriteH * 3);
      }
   }

   private void drawCursor(Graphics g) {
      EntityInfo info = le.entityFactory.getEntityInfo(le.selectedName);
      MySubImage img = entityImages.getImageFor(
            info.typeConstant, info.editorImgRow, info.editorImgCol);
      DrawUtils.drawSubImage(
            g, img,
            le.cursorX, le.cursorY,
            info.spriteW * 3, info.spriteH * 3);
   }
}
