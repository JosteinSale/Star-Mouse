package rendering.root_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.enemies.Enemy;
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
   private MySubImage vectorImg;

   public RenderLevelEditor(LevelEditor levelEditor, EntityImages entityImages, Images images) {
      this.images = images;
      this.le = levelEditor;
      this.entityImages = entityImages;
      this.vectorImg = images.getFlyImageSprite(Images.VECTOR_SPRITE, false)
            .getSubimage(0, 0, 102, 14);
   }

   public void loadLevel(int level) {
      this.clImg = images.getFlyImageForeground(
            "level" + Integer.toString(le.level) + "_cl.png");
   }

   public void draw(SpriteBatch sb) {
      drawMapAndText(sb);
      drawEntities(sb);
      drawCursor(sb);
      drawSettingVector(sb);
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
         Rectangle2D.Float hitbox = le.hitboxes.get(i);
         int hbX = (int) hitbox.getX() + le.editorXOffset;
         int hbY = (int) hitbox.getY() + le.getEditorY();

         // Charge timer
         int chargeTimer = le.chargeTimers.get(i);
         if (chargeTimer != 0) {
            DrawUtils.drawText(
                  sb, MyColor.RED, DrawUtils.infoFont,
                  Integer.toString(chargeTimer),
                  hbX, hbY - 20);
         }

         // Hitbox
         DrawUtils.fillRect(
               sb, MyColor.BLACK,
               (float) hbX, (float) hbY,
               (float) hitbox.getWidth(), (float) hitbox.getHeight());

         // Image
         int flipX = le.flipXs.get(i);
         MySubImage img = entityImages.getImageFor(
               info.typeConstant, info.editorImgRow, info.editorImgCol);
         Rectangle2D.Float adjustedHitbox = getEditorAdjustedHitbox(hitbox);
         DrawUtils.drawRotatedImage(sb, adjustedHitbox, flipX, 0.0, img);

         // Direction vector
         Vector2 vector = le.vectors.get(i);
         if (vector.x != 0 || vector.y != 0) {
            double rotation = Math.atan2(vector.y, vector.x);
            DrawUtils.drawRotatedImage(sb, adjustedHitbox, Enemy.RIGHT, rotation, vectorImg);
            drawVectorText(sb, vector, rotation, hbX, hbY);
         }
      }
   }

   private void drawVectorText(SpriteBatch sb, Vector2 vector, double rotation, int hbX, int hbY) {
      DrawUtils.drawText(
            sb, MyColor.RED, DrawUtils.infoFont,
            "x" + Integer.toString((int) vector.x) + " y" + Integer.toString((int) vector.y),
            (int) (Math.cos(rotation) * 153 + hbX),
            (int) (Math.sin(rotation) * 153 + hbY));
   }

   private void drawCursor(SpriteBatch sb) {
      if (le.settingVector) {
         return;
      }
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

   private void drawSettingVector(SpriteBatch sb) {
      if (le.settingVector) {
         Rectangle2D.Float enemyHb = le.hitboxes.get(le.hitboxes.size() - 1);
         double rotation = Math.atan2(le.directionVector.y, le.directionVector.x);
         Rectangle2D.Float adjustedHitbox = getEditorAdjustedHitbox(enemyHb);
         DrawUtils.drawRotatedImage(sb, adjustedHitbox, Enemy.RIGHT, rotation, vectorImg);
         drawVectorText(sb, le.directionVector, rotation, (int) adjustedHitbox.x, (int) adjustedHitbox.y);
      }
   }

   // Adjust the hitbox position based on the editor's current offset and scroll.
   private Rectangle2D.Float getEditorAdjustedHitbox(Rectangle2D.Float hitbox) {
      return new Rectangle2D.Float(
            (float) hitbox.getX() + le.editorXOffset,
            (float) hitbox.getY() + le.getEditorY(),
            (float) hitbox.getWidth(),
            (float) hitbox.getHeight());
   }
}
