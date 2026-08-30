package utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import entities.MyRectangle;
import entities.flying.enemies.Enemy;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;

public class DrawUtils {

   // LibGDX fonts
   public static BitmapFont headerFont = ResourceLoader.getHeaderFont();
   public static BitmapFont nameFont = ResourceLoader.getNameFont();
   public static BitmapFont menuFont = ResourceLoader.getMenuFont();
   public static BitmapFont infoFont = ResourceLoader.getInfoFont();
   public static BitmapFont itemFont = ResourceLoader.getItemFont();

   private static final GlyphLayout layout = new GlyphLayout();

   public static void drawTransparentImage(SpriteBatch batch, MyImage img, int x, int y, int width, int height,
         float alpha) {
      Color prev = batch.getColor().cpy();
      batch.setColor(1f, 1f, 1f, alpha);
      drawImage(batch, img, x, y, width, height);
      batch.setColor(prev);
   }

   public static void drawTransparentSubImage(SpriteBatch batch, MySubImage img, int x, int y, int width, int height,
         float alpha) {
      Color prev = batch.getColor().cpy();
      batch.setColor(1f, 1f, 1f, alpha);
      drawSubImage(batch, img, x, y, width, height);
      batch.setColor(prev);
   }

   public static void drawImage(SpriteBatch batch, MyImage img, int x, int y, int width, int height) {
      Texture texture = img.getTexture();
      batch.draw(texture,
            x, (y + height),
            width, -height);
   }

   public static void drawSubImage(SpriteBatch batch, MySubImage img, int x, int y, int width, int height) {
      TextureRegion region = img.getImage();
      batch.draw(region,
            x, (y + height),
            width, -height);
   }

   public static void fillRect(SpriteBatch batch, MyColor color, float x, float y, float width, float height) {
      Color c = color.getColor();
      batch.setColor(c);
      batch.draw(Images.pixel.getTexture(), // a 1x1 white texture
            x, (y + height),
            width, -height);
      batch.setColor(Color.WHITE); // reset to avoid affecting other draws
   }

   public static void fillScreen(SpriteBatch batch, MyColor color) {
      fillRect(batch, color, 0, 0, Game.GAME_DEFAULT_WIDTH, Game.GAME_DEFAULT_HEIGHT);
   }

   public static void drawRect(SpriteBatch batch, MyColor color, float x, float y, float width, float height) {
      Color c = color.getColor();
      batch.setColor(c);
      Texture pixel = Images.pixel.getTexture();

      batch.draw(pixel, x, y, width, 1); // top
      batch.draw(pixel, x, y + height - 1, width, 1); // bottom
      batch.draw(pixel, x, y, 1, height); // left
      batch.draw(pixel, x + width - 1, y, 1, height); // right

      batch.setColor(Color.WHITE);
   }

   /**
    * Draws img rotated around the center of the given x/y/width/height box.
    * Unlike the MySubImage overload below (which centers on a hitbox and
    * always scales by x3, for boss parts), this takes explicit dimensions so
    * callers with their own per-type width/height/offsets (e.g. projectiles)
    * can reuse their existing draw geometry unchanged when rotation is 0.
    */
   public static void drawRotatedImage(SpriteBatch batch, MyImage img, int x, int y, int width, int height,
         double rotation) {
      Texture texture = img.getTexture();
      batch.draw(texture,
            x, y,
            width / 2f, height / 2f,
            width, height,
            1f, -1f,
            (float) (MathUtils.radiansToDegrees * rotation),
            0, 0, texture.getWidth(), texture.getHeight(),
            false, false);
   }

   public static void drawRotatedImage(SpriteBatch batch, MyRectangle hitbox,
         int dir, double rotation, MySubImage img) {
      float width = img.getWidth() * 3;
      float height = img.getHeight() * 3;
      float x = hitbox.centerX() - (width / 2f);
      float y = hitbox.centerY() - (height / 2f);
      if (dir == Enemy.LEFT) {
         x += width;
         width = -width; // flip horizontally
      }
      batch.draw(img.getImage(),
            x, y,
            width / 2,
            height / 2,
            width, height,
            1f, -1f,
            (float) (MathUtils.radiansToDegrees * rotation));
   }

   public static void drawText(SpriteBatch batch, MyColor color, BitmapFont font, String text, int x, int y) {
      font.setColor(color.getColor());
      layout.setText(font, text);
      float newY = y - layout.height * 2;
      font.draw(batch, layout, x, newY);
   }

   public static void drawCenteredText(SpriteBatch batch, String text, Rectangle rect, BitmapFont font,
         MyColor color) {
      font.setColor(color.getColor());
      layout.setText(font, text);
      float x = rect.x + (rect.width - layout.width) / 2;
      float y = rect.y - layout.height * 2 + rect.height;
      font.draw(batch, layout, x, y);
   }

   public static void drawRotatedPolygon(ShapeRenderer sr, Polygon polygon, MyColor color) {
      sr.setColor(color.getColor());
      sr.polygon(polygon.getTransformedVertices());
   }
}
