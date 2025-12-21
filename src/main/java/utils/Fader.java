package utils;

/**
 * Fader utility class for handling fade-in and fade-out effects.
 */
public class Fader {
   private float alpha = 0.0f;
   private boolean fadingIn = false;
   private boolean fadingOut = false;
   private float fadeSpeed;
   private Runnable onFadeComplete;
   public static final float SLOW_FADE = 0.01f;
   public static final float MEDIUM_FAST_FADE = 0.02f;
   public static final float FAST_FADE = 0.04f;

   public Fader() {
   }

   public void startFadeIn(float fadeSpeed, Runnable onFadeComplete) {
      this.fadingIn = true;
      this.fadingOut = false;
      this.fadeSpeed = fadeSpeed;
      this.alpha = 1.0f; // Start fully opaque
      this.onFadeComplete = onFadeComplete;
   }

   public void startFadeOut(float fadeSpeed, Runnable onFadeComplete) {
      this.fadingOut = true;
      this.fadingIn = false;
      this.fadeSpeed = fadeSpeed;
      this.alpha = 0.0f; // Start fully transparent
      this.onFadeComplete = onFadeComplete;
   }

   public void update() {
      if (fadingIn) {
         alpha -= fadeSpeed;
         if (alpha <= 0.0f) {
            alpha = 0.0f;
            fadingIn = false;
            if (onFadeComplete != null) {
               onFadeComplete.run();
            }
         }
      } else if (fadingOut) {
         alpha += fadeSpeed;
         if (alpha >= 1.0f) {
            alpha = 1.0f;
            fadingOut = false;
            if (onFadeComplete != null) {
               onFadeComplete.run();
            }
         }
      }
   }

   public float getAlpha() {
      return alpha;
   }

   public boolean isFading() {
      return fadingIn || fadingOut;
   }
}
