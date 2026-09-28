package entities.animation;

import static entities.animation.Animation.Type.ONCE_FORWARDS;

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
public class AnimatedGlow {
   public static final int ORANGE_GLOW_BIG = 1;
   public static final int BLUE_GLOW_SMALL = 2;
   public static final int GREEN_GLOW_SMALL = 3;
   public static final int REAPER_GLOW = 4;

   private boolean active = false;
   private int tick;
   private int tickPerFrame = 2;

   private int glowType;
   private PositionedAnimation animation;

   public AnimatedGlow(int glowType, float scale) {
      this.glowType = glowType;
      this.animation = new PositionedAnimation(
            0, 0, tickPerFrame,
            0, 0, scale, scale);
   }

   public void setPos(float x, float y) {
      animation.xPos = x;
      animation.yPos = y;
   }

   public void start() {
      active = true;
   }

   public void update() {
      if (active) {
         tick++;
         animation.play(ONCE_FORWARDS, animationLength() - 1);
         if (tick >= duration()) {
            this.reset();
         }
      }
   }

   private int duration() {
      return tickPerFrame * animationLength();
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
      return animation.scaleW; // Same as scaleH
   }

   public int getAniIndex() {
      return animation.getCol();
   }

   public boolean isActive() {
      return active;
   }

   public float getX() {
      return animation.xPos;
   }

   public float getY() {
      return animation.yPos;
   }

   public void reset() {
      animation.reset();
      active = false;
      tick = 0;
   }

   public float getAlpha() {
      switch (glowType) {
         case ORANGE_GLOW_BIG:
         case REAPER_GLOW:
            return 1f - (animation.getCol() * 0.25f);
         case BLUE_GLOW_SMALL:
         case GREEN_GLOW_SMALL:
            return 1f - (animation.getCol() * 0.4f);
         default:
            return 1f;
      }
   }

}
