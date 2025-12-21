package utils;

/**
 * Fader utility class for handling fade-in and fade-out effects.
 */
public class Fader {
   private float alpha = 0.0f;
   private boolean fadingFromBlack = false;
   private boolean fadingToBlack = false;
   private float fadeSpeed;
   private Runnable onFadeComplete;
   public static final float SLOW_FADE = 0.01f;
   public static final float MEDIUM_FAST_FADE = 0.02f;
   public static final float FAST_FADE = 0.04f;

   public Fader() {
   }

   /**
    * Starts a fade from black.
    * When the fade is finished, it calls the onFadeComplete-method.
    */
   public void fadeFromBlack(float fadeSpeed, Runnable onFadeComplete) {
      this.fadingFromBlack = true;
      this.fadingToBlack = false;
      this.fadeSpeed = fadeSpeed;
      this.alpha = 1.0f; // Start fully opaque
      this.onFadeComplete = onFadeComplete;
   }

   /**
    * Starts a fade to black
    * When the fade is finished, it calls the onFadeComplete-method.
    */
   public void fadeToBlack(float fadeSpeed, Runnable onFadeComplete) {
      this.fadingToBlack = true;
      this.fadingFromBlack = false;
      this.fadeSpeed = fadeSpeed;
      this.alpha = 0.0f; // Start fully transparent
      this.onFadeComplete = onFadeComplete;
   }

   public void update() {
      if (fadingFromBlack) {
         alpha -= fadeSpeed;
         if (alpha <= 0.0f) {
            alpha = 0.0f;
            fadingFromBlack = false;
            if (onFadeComplete != null) {
               onFadeComplete.run();
            }
         }
      } else if (fadingToBlack) {
         alpha += fadeSpeed;
         if (alpha >= 1.0f) {
            alpha = 1.0f;
            fadingToBlack = false;
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
      return fadingFromBlack || fadingToBlack;
   }
}
