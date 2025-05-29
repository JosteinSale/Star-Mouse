package rendering;

import java.awt.Color;

/**
 * A wrapper for a color, for use in the rendering classes.
 * It provides absraction from the concrete color implementation.
 * 
 * Thus if we want to change the type of color being used in the game, we simply
 * have to change it once in this object + once in the DrawUtils-class,
 * instead of having to change it in every single rendering class.
 */
public class MyColor {
   // Color with specific rgb-values
   private Color color;

   // Standard colors
   public static final MyColor BLACK = getMyColor(Color.BLACK);
   public static final MyColor WHITE = getMyColor(Color.WHITE);
   public static final MyColor RED = getMyColor(Color.RED);
   public static final MyColor ORANGE = getMyColor(Color.ORANGE);
   public static final MyColor PINK = getMyColor(Color.PINK);
   public static final MyColor CYAN = getMyColor(Color.CYAN);
   public static final MyColor GREEN = getMyColor(Color.GREEN);
   public static final MyColor GRAY = getMyColor(Color.GRAY);
   public static final MyColor DARK_GRAY = getMyColor(Color.GRAY.darker());
   public static final MyColor DARKER_GRAY = getMyColor(Color.GRAY.darker().darker());
   public static final MyColor LIGHT_GRAY = getMyColor(Color.GRAY.brighter());
   public static final MyColor DARK_GREEN = getMyColor(Color.GREEN.darker());
   public static final MyColor LIGHT_BLUE = getMyColor(Color.BLUE.brighter());
   public static final MyColor MAGENTA = getMyColor(Color.MAGENTA);
   public static final MyColor YELLOW = getMyColor(Color.YELLOW);

   public MyColor(int r, int g, int b, int alpha) {
      this.color = new Color(r, g, b, alpha);
   }

   private static MyColor getMyColor(Color color) {
      return new MyColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
   }

   public Color getColor() {
      return new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha());
   }

   public int getRed() {
      return this.color.getRed();
   }

   public int getGreen() {
      return this.color.getGreen();
   }

   public int getBlue() {
      return this.color.getBlue();
   }

   public int getAlpha() {
      return this.color.getAlpha();
   }

}
