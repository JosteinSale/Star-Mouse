package rendering.root_renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import entities.MyRectangle;
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
   }

   public void loadLevel(int level) {
      this.clImg = images.getFlyImageForeground(
            "level" + Integer.toString(le.level) + "_cl.png");
      this.vectorImg = images.getFlyImageSprite(Images.VECTOR_SPRITE, false)
            .getSubimage(0, 0, 102, 14);
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
            "shootTimer : " + Integer.toString(le.chargeTimer),
            20, 50);
      DrawUtils.drawText(
            sb, MyColor.BLACK, DrawUtils.infoFont,
            "y :" + Integer.toString(le.getEditorY()),
            700, 20);
   }

   private void drawEntities(SpriteBatch sb) {
      for (int i = 0; i < le.addedEntities.size(); i++) {
         EntityInfo info = le.getEntityInfo(le.addedEntities.get(i));
         MyRectangle hitbox = le.hitboxes.get(i);
         int hbX = (int) hitbox.x() + le.editorXOffset;
         int hbY = (int) hitbox.y() + le.getEditorY();

         // Charge timer
         int chargeTimer = le.chargeTimers.get(i);
         if (chargeTimer != 0) {
            DrawUtils.drawText(
                  sb, MyColor.RED, DrawUtils.infoFont,
                  Integer.toString(chargeTimer),
                  hbX, hbY - 20);
         }

         // Image
         int flipX = le.flipXs.get(i);
         MySubImage img = entityImages.getImageFor(
               info.typeConstant, info.editorImgRow, info.editorImgCol);
         MyRectangle adjustedHitbox = getEditorAdjustedHitbox(hitbox);
         DrawUtils.drawRotatedImage(sb, adjustedHitbox, flipX, 0.0, img);

         // Hitbox
         DrawUtils.drawRect(
               sb, MyColor.RED,
               (float) hbX, (float) hbY,
               (float) hitbox.width(), (float) hitbox.height());

         // Direction vector
         Vector2 vector = le.vectors.get(i);
         if (vector.x != 0 || vector.y != 0) {
            double rotation = Math.atan2(vector.y, vector.x);
            DrawUtils.drawRotatedImage(sb, adjustedHitbox, Enemy.RIGHT, rotation, vectorImg);
            drawVectorText(sb, vector, rotation, hbX, hbY);
         }
      }
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
         MyRectangle enemyHb = le.hitboxes.get(le.hitboxes.size() - 1);
         double rotation = Math.atan2(le.directionVector.y, le.directionVector.x);
         MyRectangle adjustedHitbox = getEditorAdjustedHitbox(enemyHb);
         DrawUtils.drawRotatedImage(sb, adjustedHitbox, Enemy.RIGHT, rotation, vectorImg);
         drawVectorText(sb, le.directionVector, rotation, (int) adjustedHitbox.x(), (int) adjustedHitbox.y());
      }
   }

   // Adjust the hitbox position based on the editor's current offset and scroll.
   private MyRectangle getEditorAdjustedHitbox(MyRectangle hitbox) {
      return new MyRectangle(
            (float) hitbox.x() + le.editorXOffset,
            (float) hitbox.y() + le.getEditorY(),
            hitbox.width(),
            hitbox.height());
   }

   private void drawVectorText(SpriteBatch sb, Vector2 vector, double rotation, int hbX, int hbY) {
      DrawUtils.drawText(
            sb, MyColor.BLUE, DrawUtils.infoFont,
            "x" + Integer.toString((int) vector.x) + " y" + Integer.toString((int) vector.y),
            (int) (Math.cos(rotation) * 153 + hbX),
            (int) (Math.sin(rotation) * 153 + hbY));
   }
}
