package rendering.root_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import gamestates.LevelEditor;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.flying.EntityImages;
import utils.DrawUtils;
import utils.Images;
import utils.Singleton;

public class RenderLevelEditor extends Singleton {

   private Images images;
   private LevelEditor le;
   private EntityImages entityImages;
   private MyImage clImg;

   public RenderLevelEditor(LevelEditor levelEditor, EntityImages entityImages, Images images) {
      this.images = images;
      this.le = levelEditor;
      this.entityImages = entityImages;
   }

   public void loadLevel(int level) {
      this.clImg = images.getFlyImageForeground(
            "level" + Integer.toString(le.level) + "_cl.png");
   }

   public void draw(SpriteBatch sb) {
      drawMapAndText(sb);
      drawEntities(sb);
      drawCursor(sb);
   }

   private void drawMapAndText(SpriteBatch sb) {
      // White background
      DrawUtils.fillRect(sb, MyColor.WHITE,
            0, 0,
            Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);

      // Map
      DrawUtils.drawImage(
            sb, clImg,
            (int) le.clXOffset, le.clYOffset,
            le.clImgWidth, le.clImgHeight);

      // Top text
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            "direction : " + Integer.toString(le.curDirection),
            20, 20);
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            "shootTimer : " + Integer.toString(le.shootTimer),
            20, 50);
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            "y :" + Integer.toString(le.mapYOffset),
            700, 20);
   }

   private void drawEntities(SpriteBatch sb) {
      for (int i = 0; i < le.addedEntityNames.size(); i++) {
         EntityInfo info = le.entityFactory.getEntityInfo(le.addedEntityNames.get(i));
         Rectangle2D hitbox = le.hitboxes.get(i);

         // Text
         DrawUtils.drawText(
               sb, MyColor.BLACK, DrawUtils.infoFont,
               Integer.toString(le.shootTimers.get(i)),
               (int) hitbox.getX(),
               (int) (hitbox.getY() - le.mapYOffset - 20));

         // Hitbox
         DrawUtils.fillRect(
               sb, MyColor.BLACK,
               (float) hitbox.getX(), (float) (hitbox.getY() - le.mapYOffset),
               (float) hitbox.getWidth(), (float) hitbox.getHeight());

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
               sb, img,
               (int) (hitbox.getX() - xOffset),
               (int) (hitbox.getY() - yOffset - le.mapYOffset),
               spriteW * 3 * dir,
               spriteH * 3);
      }
   }

   private void drawCursor(SpriteBatch sb) {
      EntityInfo info = le.entityFactory.getEntityInfo(le.selectedName);
      MySubImage img = entityImages.getImageFor(
            info.typeConstant, info.editorImgRow, info.editorImgCol);
      DrawUtils.drawSubImage(
            sb, img,
            le.cursorX, le.cursorY,
            info.spriteW * 3, info.spriteH * 3);
   }
}
