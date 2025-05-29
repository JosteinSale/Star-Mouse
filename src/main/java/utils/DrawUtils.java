package utils;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.FontMetrics;

import entities.bossmode.DefaultBossPart;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;

/** Provides common drawing methods */
public class DrawUtils {

   // Fonts: from biggest to smallest
   public static Font headerFont = ResourceLoader.getHeaderFont();
   public static Font nameFont = ResourceLoader.getNameFont();
   public static Font menuFont = ResourceLoader.getMenuFont();
   public static Font infoFont = ResourceLoader.getInfoFont();
   public static Font itemFont = ResourceLoader.getItemFont();

   /**
    * Draws the image scaled to Game.SCALE with int values.
    * The x- and y-coordinate must be pre-adjusted for drawOffset and levelOffset.
    */
   public static void drawImage(
         Graphics g, MyImage img, int x, int y,
         int width, int height) {
      g.drawImage(
            img.getImage(),
            (int) (x * Game.SCALE),
            (int) (y * Game.SCALE),
            (int) (width * Game.SCALE),
            (int) (height * Game.SCALE), null);
   }

   /**
    * Draws the sub image scaled to Game.SCALE with int values.
    * The x- and y-coordinate must be pre-adjusted for drawOffset and levelOffset.
    */
   public static void drawSubImage(
         Graphics g, MySubImage img, int x, int y,
         int width, int height) {
      g.drawImage(
            img.getImage(),
            (int) (x * Game.SCALE),
            (int) (y * Game.SCALE),
            (int) (width * Game.SCALE),
            (int) (height * Game.SCALE), null);
   }

   public static void drawText(
         Graphics g, MyColor c, Font font, String text, int x, int y) {
      g.setColor(c.getColor());
      g.setFont(font);
      g.drawString(
            text,
            (int) (x * Game.SCALE),
            (int) (y * Game.SCALE));
   }

   /** fillRect - int */
   public static void fillRect(
         Graphics g, MyColor c, int x, int y, int width, int height) {
      g.setColor(c.getColor());
      g.fillRect(
            (int) (x * Game.SCALE),
            (int) (y * Game.SCALE),
            (int) (width * Game.SCALE),
            (int) (height * Game.SCALE));
   }

   /** fillRect - double */
   public static void fillRect(
         Graphics g, MyColor c, double x, double y, double width, double height) {
      g.setColor(c.getColor());
      g.fillRect(
            (int) (x * Game.SCALE),
            (int) (y * Game.SCALE),
            (int) (width * Game.SCALE),
            (int) (height * Game.SCALE));
   }

   public static void fillScreen(Graphics g, MyColor c) {
      g.setColor(c.getColor());
      g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
   }

   public static void drawRect(
         Graphics g, MyColor c, int x, int y, int width, int height) {
      g.setColor(c.getColor());
      g.drawRect(
            (int) (x * Game.SCALE),
            (int) (y * Game.SCALE),
            (int) (width * Game.SCALE),
            (int) (height * Game.SCALE));
   }

   public static void drawRotatedBossPart(
         Graphics g, DefaultBossPart bp, Image[][] imgs) {
      Graphics2D g2 = (Graphics2D) g;
      utils.Inf101Graphics.drawCenteredImage(
            g2, imgs[bp.animAction][bp.aniIndex],
            bp.nonRotatedHitbox.getCenterX() * Game.SCALE,
            bp.nonRotatedHitbox.getCenterY() * Game.SCALE,
            Game.SCALE, bp.rotation);
   }

   public static void DrawCenteredString(Graphics g, String text, Rectangle rect, Font font, MyColor color) {
      FontMetrics metrics = g.getFontMetrics(font);
      int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
      int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
      g.setColor(color.getColor());
      g.setFont(font);
      g.drawString(text, x, y);
   }
}
