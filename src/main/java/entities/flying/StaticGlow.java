package entities.flying;

/**
 * A glowing effect in the game. Used for shooting effects and other visual
 * enhancements.
 * 
 * Call start() method to activate the glow effect
 * Call update() method in the game loop to update the glow's animation
 * Change the position of the glow using setPos(x, y) method.
 * 
 * To check if an animated glow is active (e.g. for rendering), use isActive()
 */
public class StaticGlow {
   public static final int WHITE_GLOW_SMALL = 5;
   public static final int WHITE_GLOW_MEDIUM = 6;
   public static final int WHITE_GLOW_BIG = 7;
   public static final int WHITE_GLOW_BIGGEST = 8;

   private int glowType;
   private float alpha = 1f;
   private float scale;

   private float x;
   private float y;

   public StaticGlow(int glowType, float scale, float alpha) {
      this.glowType = glowType;
      this.scale = scale;
      this.alpha = alpha;
   }

   public void setPos(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public void setGlowType(int glowType) {
      this.glowType = glowType;
   }

   public int getGlowType() {
      return glowType;
   }

   public float getScale() {
      return scale;
   }

   public float getX() {
      return x;
   }

   public float getY() {
      return y;
   }

   public float getAlpha() {
      return alpha;
   }

}
