package rendering.root_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import game_states.LevelEditor;
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
            (int) le.clImgX, le.clImgY,
            le.clImgWidth, le.clImgHeight);

      // Top text
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            "direction : " + Integer.toString(le.enemyFlipX),
            20, 20);
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            "shootTimer : " + Integer.toString(le.shootTimer),
            20, 50);
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            "y :" + Integer.toString(le.getEditorY()),
            700, 20);
   }

   private void drawEntities(SpriteBatch sb) {
      for (int i = 0; i < le.addedEntities.size(); i++) {
         EntityInfo info = le.getEntityInfo(le.addedEntities.get(i));
         Rectangle hitbox = le.hitboxes.get(i);
         int hbX = (int) hitbox.getX() + le.editorXOffset;
         int hbY = (int) hitbox.getY() + le.getEditorY();

         // Text
         DrawUtils.drawText(
               sb, MyColor.BLACK, DrawUtils.infoFont,
               Integer.toString(le.shootTimers.get(i)),
               hbX, hbY - 20);

         // Hitbox
         DrawUtils.fillRect(
               sb, MyColor.BLACK,
               (float) hbX, (float) hbY,
               (float) hitbox.getWidth(), (float) hitbox.getHeight());

         // Image
         int flipX = le.flipXs.get(i);
         MySubImage img = entityImages.getImageFor(
               info.typeConstant, info.editorImgRow, info.editorImgCol);
         Rectangle2D.Float adjustedHitbox = new Rectangle2D.Float(
               (float) hitbox.getX() + le.editorXOffset,
               (float) hitbox.getY() + le.getEditorY(),
               (float) hitbox.getWidth(),
               (float) hitbox.getHeight());
         DrawUtils.drawRotatedImage(sb, adjustedHitbox, flipX, 0.0, img);
      }
   }

   private void drawCursor(SpriteBatch sb) {
      EntityInfo info = le.getEntityInfo(le.selectedEntity);
      MySubImage img = entityImages.getImageFor(
            info.typeConstant, info.editorImgRow, info.editorImgCol);
      int width = info.spriteW * 3;
      int height = info.spriteH * 3;
      int x = le.cursorX - (width / 2);
      int y = le.cursorY - (height / 2);
      DrawUtils.drawSubImage(
            sb, img,
            x, y,
            width, height);
   }
}
