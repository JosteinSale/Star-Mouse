package audio;

import com.badlogic.gdx.audio.Sound;

import utils.Resource;

/**
 * A wrapper for a sound file, for use in audio playback.
 * It provides abstraction from the concrete sound implementation.
 */
public class MySound implements Resource {
   private Sound sound;

   public MySound(Sound sound) {
      this.sound = sound;
   }

   public Sound get() {
      return this.sound;
   }

   @Override
   public void flush() {
      sound.dispose();
   }

}
