package audio;

import com.badlogic.gdx.audio.Music;

import utils.Resource;

/**
 * A wrapper for a music file, for use in audio playback.
 * It provides abstraction from the concrete music implementation.
 */
public class MyMusic implements Resource {
   private Music music;

   public MyMusic(Music music) {
      this.music = music;
   }

   public Music get() {
      return this.music;
   }

   @Override
   public void flush() {
      music.dispose();
   }

}
