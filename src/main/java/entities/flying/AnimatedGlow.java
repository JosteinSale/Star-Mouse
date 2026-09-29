package entities.flying;

import static entities.animation.Animation.Type.ONCE_FORWARDS;

import entities.animation.PositionedAnimation;

/**
 * A glowing effect in the game. Used for shooting effects and other visual
 * enhancements.
 * 
 * Call start() method to activate the glow effect.
 * Call update() method in the game loop to update the glow's animation.
 * Change the position of the glow using setPos(x, y) method.
 * 
 * To check if an animated glow is active (e.g. for rendering), use isActive()
 * method.
 */
public class AnimatedGlow extends PositionedAnimation {
   private int glowType;
   private boolean active = false;

   // Glow types
   public static final int ORANGE_GLOW_BIG = 1;
   public static final int BLUE_GLOW_SMALL = 2;
   public static final int GREEN_GLOW_SMALL = 3;
   public static final int REAPER_GLOW = 4;

   public AnimatedGlow(int glowType, float scale) {
      super(0, 0, 3, 0, 0, scale, scale);
      this.glowType = glowType;
   }

   public void setPos(float x, float y) {
      this.xPos = x;
      this.yPos = y;
   }

   public void start() {
      active = true;
   }

   public void update() {
      if (active) {
         play(ONCE_FORWARDS, animationLength() - 1);
         if (playedOnce()) {
            reset();
         }
      }
   }

   private int animationLength() {
      switch (glowType) {
         case ORANGE_GLOW_BIG:
         case REAPER_GLOW:
            return 4;
         case BLUE_GLOW_SMALL:
         case GREEN_GLOW_SMALL:
            return 2;
         default:
            return 2;
      }
   }

   public void setGlowType(int glowType) {
      this.glowType = glowType;
   }

   public int getGlowType() {
      return glowType;
   }

   public float getScale() {
      return scaleW; // Same as scaleH
   }

   public int getAniIndex() {
      return this.getCol();
   }

   public boolean isActive() {
      return active;
   }

   public float getX() {
      return xPos;
   }

   public float getY() {
      return yPos;
   }

   public void reset() {
      super.reset();
      active = false;
   }

   public float getAlpha() {
      switch (glowType) {
         case ORANGE_GLOW_BIG:
         case REAPER_GLOW:
            return 1f - (getCol() * 0.25f);
         case BLUE_GLOW_SMALL:
         case GREEN_GLOW_SMALL:
            return 1f - (getCol() * 0.4f);
         default:
            return 1f;
      }
   }

}
