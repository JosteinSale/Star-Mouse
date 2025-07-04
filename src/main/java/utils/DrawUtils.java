package utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import entities.bossmode.DefaultBossPart;
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

   public static void drawImage(SpriteBatch batch, MyImage img, int x, int y, int width, int height) {
      Texture texture = img.getTexture();
      batch.draw(texture,
            x * Game.SCALE, (y + height) * Game.SCALE,
            width * Game.SCALE, -height * Game.SCALE);
   }

   public static void drawSubImage(SpriteBatch batch, MySubImage img, int x, int y, int width, int height) {
      TextureRegion region = img.getImage();
      batch.draw(region,
            x * Game.SCALE, (y + height) * Game.SCALE,
            width * Game.SCALE, -height * Game.SCALE);
   }

   public static void fillRect(SpriteBatch batch, MyColor color, float x, float y, float width, float height) {
      Color c = color.getColor();
      batch.setColor(c);
      batch.draw(Images.pixel.getTexture(), // a 1x1 white texture
            x * Game.SCALE, (y + height) * Game.SCALE,
            width * Game.SCALE, -height * Game.SCALE);
      batch.setColor(Color.WHITE); // reset to avoid affecting other draws
   }

   public static void fillScreen(SpriteBatch batch, MyColor color) {
      fillRect(batch, color, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
   }

   public static void drawRect(SpriteBatch batch, MyColor color, float x, float y, float width, float height) {
      Color c = color.getColor();
      batch.setColor(c);
      Texture pixel = Images.pixel.getTexture();

      float sx = x * Game.SCALE;
      float sy = y * Game.SCALE;
      float sw = width * Game.SCALE;
      float sh = height * Game.SCALE;

      batch.draw(pixel, sx, sy, sw, 1); // top
      batch.draw(pixel, sx, sy + sh - 1, sw, 1); // bottom
      batch.draw(pixel, sx, sy, 1, sh); // left
      batch.draw(pixel, sx + sw - 1, sy, 1, sh); // right

      batch.setColor(Color.WHITE);
   }

   public static void drawRotatedBossPart(SpriteBatch batch, DefaultBossPart bp, MySubImage[][] imgs) {
      TextureRegion img = imgs[bp.animAction][bp.aniIndex].getImage();
      float centerX = (float) bp.nonRotatedHitbox.getCenterX();
      float centerY = (float) bp.nonRotatedHitbox.getCenterY();
      float width = img.getRegionWidth() * 3;
      float height = img.getRegionHeight() * 3;

      batch.draw(img,
            (centerX - width / 2) * Game.SCALE,
            (centerY - height / 2) * Game.SCALE,
            (width / 2) * Game.SCALE,
            (height / 2) * Game.SCALE,
            width, height,
            1f, -1f,
            (float) (MathUtils.radiansToDegrees * bp.rotation));
   }

   public static void drawText(SpriteBatch batch, MyColor color, BitmapFont font, String text, int x, int y) {
      font.setColor(color.getColor());
      layout.setText(font, text);
      float newY = y - layout.height * 2;
      font.draw(batch, layout, x * Game.SCALE, newY * Game.SCALE);
   }

   public static void drawCenteredText(SpriteBatch batch, String text, Rectangle rect, BitmapFont font,
         MyColor color) {
      font.setColor(color.getColor());
      layout.setText(font, text);
      float x = rect.x + (rect.width - layout.width) / 2;
      float y = rect.y - layout.height * 2 + rect.height;
      font.draw(batch, layout, x * Game.SCALE, y * Game.SCALE);
   }
}
