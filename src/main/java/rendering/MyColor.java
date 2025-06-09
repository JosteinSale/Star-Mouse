package rendering;

import com.badlogic.gdx.graphics.Color;

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
   private final Color color;

   // Standard colors
   public static final MyColor BLACK = new MyColor(Color.BLACK);
   public static final MyColor WHITE = new MyColor(Color.WHITE);
   public static final MyColor RED = new MyColor(Color.RED);
   public static final MyColor ORANGE = new MyColor(Color.ORANGE);
   public static final MyColor PINK = new MyColor(Color.PINK);
   public static final MyColor CYAN = new MyColor(Color.CYAN);
   public static final MyColor GREEN = new MyColor(Color.GREEN);
   public static final MyColor GRAY = new MyColor(Color.GRAY);
   public static final MyColor DARK_GRAY = new MyColor(new Color(0.25f, 0.25f, 0.25f, 1f));
   public static final MyColor DARKER_GRAY = new MyColor(new Color(0.15f, 0.15f, 0.15f, 1f));
   public static final MyColor LIGHT_GRAY = new MyColor(new Color(0.8f, 0.8f, 0.8f, 1f));
   public static final MyColor DARK_GREEN = new MyColor(new Color(0f, 0.4f, 0f, 1f));
   public static final MyColor LIGHT_BLUE = new MyColor(new Color(0.5f, 0.7f, 1f, 1f));
   public static final MyColor MAGENTA = new MyColor(Color.MAGENTA);
   public static final MyColor YELLOW = new MyColor(Color.YELLOW);

   public MyColor(Color color) {
      this.color = new Color(color); // defensive copy
   }

   /** Takes a float between 0 and 1s */
   public MyColor(float r, float g, float b, float a) {
      this.color = new Color(r, g, b, a);
   }

   /** Takes an int between 0 and 255 */
   public MyColor(int r, int g, int b, int a) {
      this.color = new Color(r / 255f, g / 255f, b / 255f, a / 255f);
   }

   public Color getColor() {
      return new Color(this.color); // return a copy to avoid mutation
   }

   public float getRed() {
      return color.r;
   }

   public float getGreen() {
      return color.g;
   }

   public float getBlue() {
      return color.b;
   }

   public float getAlpha() {
      return color.a;
   }

}
