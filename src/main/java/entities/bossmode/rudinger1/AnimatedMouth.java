package entities.bossmode.rudinger1;

import java.util.HashMap;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimationInfo;

/**
 * The AnimatedMouth handles animations pertaining to the mouth of Rudinger1.
 * It's distinct from an AnimatedComponent in that it has a separate
 * damageAnimation()-method, that should be called whenever the boss takes
 * damage.
 * The damage animation is drawn ontop of whatever animation is beneath, to
 * avoid messing with the animation state.
 */
public class AnimatedMouth extends AnimatedComponent {
   private boolean isBlinking = false;
   public boolean shouldDrawBlink = false;
   private int blinkTick = 0;
   private int blinkDuration = 20;

   // Animation states
   public static final String IDLE = "IDLE";
   public static final String DAMAGE = "DAMAGE";
   public static final String OPEN_UP = "OPEN_UP";
   public static final String CLOSE = "CLOSE";

   public AnimatedMouth(String spriteSheet, int spriteW, int spriteH, int rows, int cols,
         HashMap<String, AnimationInfo> aniInfo, float xPos, float yPos) {
      super(spriteSheet, aniInfo, IDLE, spriteW, spriteH, rows, cols, xPos, yPos);
   }

   /** Call this method (once) when the boss takes damage */
   public void damageAnimation() {
      this.isBlinking = true;
      this.blinkTick = blinkDuration;
   }

   @Override
   public void updateAnimations() {
      super.updateAnimations(); // Update animations normally
      this.updateBlinkTick(); // Also update blinking
   }

   // If the boss has taken damage
   private void updateBlinkTick() {
      if (isBlinking) {
         blinkTick--;
         if (blinkTick == 0) {
            isBlinking = false;
         }
      }
      if (isBlinking && (blinkTick % 8 > 3)) {
         shouldDrawBlink = true;
      } else {
         shouldDrawBlink = false;
      }
   }
}
