package utils;

import rendering.MyColor;

/**
 * Fader utility class for handling fade-in and fade-out effects.
 */
public class Fader {
   private float alpha = 0.0f;
   private boolean fadingTo = false;
   private boolean fadingFrom = false;
   private float fadeSpeed;
   private Runnable onFadeComplete;
   private MyColor color;
   public static final float SLOW_FADE = 0.01f;
   public static final float MEDIUM_FAST_FADE = 0.02f;
   public static final float FAST_FADE = 0.04f;

   public Fader() {
   }

   /**
    * Starts a fade from the specified color.
    * When the fade is finished, it calls the onFadeComplete-method.
    */
   public void fadeFrom(MyColor color, float fadeSpeed, Runnable onFadeComplete) {
      this.fadingTo = true;
      this.fadingFrom = false;
      this.fadeSpeed = fadeSpeed;
      this.alpha = 1.0f; // Start fully opaque
      this.onFadeComplete = onFadeComplete;
      this.color = color;
   }

   /**
    * Starts a fade to the specified color
    * When the fade is finished, it calls the onFadeComplete-method.
    */
   public void fadeTo(MyColor color, float fadeSpeed, Runnable onFadeComplete) {
      this.fadingFrom = true;
      this.fadingTo = false;
      this.fadeSpeed = fadeSpeed;
      this.alpha = 0.0f; // Start fully transparent
      this.onFadeComplete = onFadeComplete;
      this.color = color;
   }

   public void update() {
      if (fadingTo) {
         alpha -= fadeSpeed;
         if (alpha <= 0.0f) {
            alpha = 0.0f;
            fadingTo = false;
            if (onFadeComplete != null) {
               onFadeComplete.run();
            }
         }
      } else if (fadingFrom) {
         alpha += fadeSpeed;
         if (alpha >= 1.0f) {
            alpha = 1.0f;
            fadingFrom = false;
            if (onFadeComplete != null) {
               onFadeComplete.run();
            }
         }
      }
   }

   public boolean isFading() {
      return fadingTo || fadingFrom;
   }

   public MyColor getColor() {
      return MyColor.getColorWithAlpha(color, alpha);
   }

   public static float FadeSpeedByInt(int speed) {
      return speed / 300f;
   }
}
