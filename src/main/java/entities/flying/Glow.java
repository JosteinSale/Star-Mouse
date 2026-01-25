package entities.flying;

/**
 * A glowing effect in the game. Used for shooting effects and other visual
 * enhancements.
 * 
 * Call start() method to activate the glow effect.
 * Call update() method in the game loop to update the glow's animation.
 * Change the position of the glow using setPos(x, y) method.
 * 
 * To check if the glow is active (e.g. for rendering), use isActive() method.
 */
public class Glow {
   // If we make more glows in the future, we can add custom behaviors in
   // subclasses.
   public static final int ORANGE_GLOW_BIG = 1;
   public static final int BLUE_GLOW_SMALL = 2;
   public static final int GREEN_GLOW_SMALL = 3;
   public static final int REAPER_GLOW = 4;

   private int glowType;
   private float scale;

   private int aniTick = 0;
   private int aniSpeed = 2;
   private int aniIndex = 0;

   private float x;
   private float y;

   private boolean active = false;

   public Glow(int glowType, float scale) {
      this.glowType = glowType;
      this.scale = scale;
   }

   public void setPos(float x, float y) {
      this.x = x;
      this.y = y;
   }

   public void start() {
      active = true;
   }

   public void update() {
      if (active) {
         updateAnimationTick();
      }
   }

   private void updateAnimationTick() {
      aniTick++;
      if (aniTick > aniSpeed) {
         aniTick = 0;
         aniIndex++;
         if (aniIndex > GetLastAniIndex()) {
            active = false;
            aniIndex = 0;
         }
      }
   }

   private int GetLastAniIndex() {
      switch (glowType) {
         case ORANGE_GLOW_BIG:
         case REAPER_GLOW:
            return 3;
         case BLUE_GLOW_SMALL:
         case GREEN_GLOW_SMALL:
            return 1;
         default:
            return 1;
      }
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

   public int getAniIndex() {
      return aniIndex;
   }

   public boolean isActive() {
      return active;
   }

   public float getX() {
      return x;
   }

   public float getY() {
      return y;
   }

   public void reset() {
      aniIndex = 0;
      aniTick = 0;
      active = false;
   }

   public float getAlpha() {
      switch (glowType) {
         case ORANGE_GLOW_BIG:
         case REAPER_GLOW:
            return 1f - (aniIndex * 0.25f);
         case BLUE_GLOW_SMALL:
         case GREEN_GLOW_SMALL:
            return 1f - (aniIndex * 0.4f);
         default:
            return 1f;
      }
   }

}
