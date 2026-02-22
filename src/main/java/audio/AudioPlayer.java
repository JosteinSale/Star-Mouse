package audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import main_classes.Testing;
import utils.ResourceContainer;
import utils.ResourceLoader;
import utils.Singleton;

public class AudioPlayer extends Singleton {
   private SFXPlayer sfxPlayer;
   // These indexes can't be changed, or else stuff breaks >:[
   // TODO - find better solution
   private String[] songFileNames = {
         "Song - Tutorial (FINISHED)3.ogg",
         "Song - The Academy ver3.ogg",
         "Song - Skies Over Apolis.ogg",
         "Song - Main Menu.ogg",
         "Song - Vyke.ogg",
         "Song - Vyke Ambush.ogg",
         "Song - Grand Reaper.ogg",
         "Song - Rudinger Theme.ogg",
         "Song - Asteroid Escape.ogg",
         "Song - Apo Explodes.ogg",
         "Song - The Dark.ogg",
         "Song - The Dark (Ending).ogg",
         "Song - Cathedral.ogg"
   };
   private String[] ambienceFileNames = {
         "Ambience - RocketEngineQuiet.ogg",
         "Ambience - Wind.ogg",
         "Ambience - Alarm.ogg",
         "Ambience - Hangar.ogg",
         "Ambience - Cave.ogg"
   };
   private ResourceContainer<MyMusic> songs;
   private ResourceContainer<MyMusic> ambienceTracks;
   private Integer curSongIndex = 0;
   private Integer curAmbienceIndex = 0;
   private boolean curSongLooping;
   private Music curSong;
   private Music curAmbience;

   private float setSongVolume = 0.61f;
   private float setAmbienceVolume = 0.91f;
   private float setSfxVolume = 0.91f;

   // Fading stuff
   private float songFadeVolume = setSongVolume;
   private float ambienceFadeVolume = setAmbienceVolume;
   private float volumeFadeSpeed = 0.05f;
   private boolean fadeOutActive = false;
   private int volumeFadeTick = 0;
   private int volumeFadeChangeInterval = 20;

   // Silent track, which we play to keep audio drivers alive
   private Sound keepAliveSound;

   public AudioPlayer() {
      loadAudio();
      startKeepAliveSound();
   }

   private void startKeepAliveSound() {
      // Some audio drivers go to sleep if no sound is playing. This keeps them alive.
      keepAliveSound = ResourceLoader.getSound("keepAliveSound.wav").get();
      keepAliveSound.loop(1f); // Volume needs to be high
   }

   public void setTestVolumes() {
      if (!Testing.playMusic) {
         setSongVolume(0f);
      }
      if (!Testing.playSFX) {
         setSfxVolume(0f);
      }
   }

   private void loadAudio() {
      this.sfxPlayer = new SFXPlayer(setSfxVolume);
      this.songs = new ResourceContainer<>(s -> ResourceLoader.getSong(s));
      this.ambienceTracks = new ResourceContainer<>(s -> ResourceLoader.getSong(s));

      // Initial assignments: needed to avoid nullpointers
      String mainMenuSong = songFileNames[3];
      String rocketEngineAmbience = ambienceFileNames[0];
      this.curSong = songs.getResource(mainMenuSong, false).get();
      this.curAmbience = ambienceTracks.getResource(rocketEngineAmbience, true).get();
   }

   /**
    * Plays the SFX with the given index, using the SFXPlayer-object (see javadoc).
    * 
    * @param index
    */
   public void playSFX(int index) {
      this.sfxPlayer.playSfx(index);
   }

   /**
    * Plays the voice clip with the given index, using the voicePlayer-object (see
    * javadoc).
    * 
    * @param index
    */
   public void playVoiceClip(String name) {
      this.sfxPlayer.playVoiceClip(name);
   }

   /**
    * Stops the current song loop, and then starts a new song loop with the
    * specified index. Index = 99 means no song.
    */
   public void startSong(int index, float startPos, boolean shouldLoop) {
      if (index == 99) {
         return;
      }
      if (curSong.isPlaying()) {
         curSong.stop();
      }
      this.curSongIndex = index;
      this.songFadeVolume = setSongVolume;
      this.curSongLooping = shouldLoop;
      stopFadeOutIfActive(); // In case fadeOut is happening
      curSong = songs.getResource(songFileNames[index], false).get();
      curSong.setVolume(songFadeVolume);
      if (curSongLooping) {
         curSong.setLooping(true);
         curSong.play();
      } else {
         curSong.play();
      }
      curSong.setPosition(startPos); // Needs to be called after .play()
   }

   /**
    * Stops the current ambience loop, and then starts a new
    * ambience loop with the specified index.
    * Index = 99 means no ambience.
    */
   public void startAmbienceLoop(int index) {
      if (index == 99) {
         return;
      }
      if (curAmbience.isPlaying()) {
         curAmbience.stop();
      }
      this.curAmbienceIndex = index;
      this.ambienceFadeVolume = setAmbienceVolume;
      stopFadeOutIfActive(); // In case fadeOut is happening
      curAmbience = ambienceTracks.getResource(ambienceFileNames[index], false).get();
      curAmbience.setVolume(ambienceFadeVolume);
      curAmbience.setLooping(true);
      curAmbience.play();
   }

   /**
    * Sometimes we start a new song/ambience while a fadeOut is happening.
    * In such case we need to stop the fadeout and reset it, so that it
    * doesn't stop the new song/ambience.
    */
   private void stopFadeOutIfActive() {
      if (fadeOutActive) {
         fadeOutActive = false;
         volumeFadeTick = 0;
      }
   }

   /** Stops all loops and resets them to the beginning */
   public void stopAllLoops() {
      curSong.stop();
      curAmbience.stop();
   }

   /**
    * Pauses all loops. When play is invoked again, it will start from where the
    * song was paused
    */
   public void pauseAllLoops() {
      curSong.pause();
      curAmbience.pause();
   }

   /** Fades out the current song + ambience, and then stops them. */
   public void fadeOutAllLoops() {
      if (curSong.isPlaying() || curAmbience.isPlaying()) {
         this.fadeOutActive = true;
      }
   }

   public void update() {
      if (this.fadeOutActive) {
         updateFade();
      }
   }

   private void updateFade() {
      this.volumeFadeTick++;
      if (volumeFadeTick > volumeFadeChangeInterval) {
         volumeFadeTick = 0;
         songFadeVolume = Math.max(songFadeVolume - volumeFadeSpeed, 0);
         ambienceFadeVolume = Math.max(ambienceFadeVolume - volumeFadeSpeed, 0);
         curSong.setVolume(songFadeVolume);
         curAmbience.setVolume(ambienceFadeVolume);
         // Check if both songVolume and ambienceVolume are 0. We need to check both,
         // in case the user has turned down the volume for one of them.
         if (songFadeVolume == 0 && ambienceFadeVolume == 0) {
            curSong.stop();
            curAmbience.stop();
            songFadeVolume = setSongVolume;
            ambienceFadeVolume = setAmbienceVolume;
            this.fadeOutActive = false;
            volumeFadeTick = 0;
         }
      }
   }

   /** Returns the volume set by player (not currentVolume) */
   public float getMusicVolume() {
      return this.setSongVolume;
   }

   /** Returns the sfx / ambience volume set by player (not currentVolume) */
   public float getSfxVolume() {
      return sfxPlayer.getVolume();
   }

   /** Is called from the OptionsMenu */
   public void setSongVolume(float volume) {
      float adjustedVolume = adjustToSafeVolume(volume);
      this.setSongVolume = adjustedVolume;
      this.songFadeVolume = adjustedVolume;
      curSong.setVolume(adjustedVolume);
   }

   /** Is called from the OptionsMenu. Goes for both ambience and sfx */
   public void setSfxVolume(float volume) {
      float adjustedVolume = adjustToSafeVolume(volume);
      this.setSfxVolume = adjustedVolume;
      this.sfxPlayer.setVolume(adjustedVolume);

      this.setAmbienceVolume = adjustedVolume;
      this.ambienceFadeVolume = adjustedVolume;
      curAmbience.setVolume(adjustedVolume);
   }

   private float adjustToSafeVolume(float volume) {
      if (volume < 0f) {
         return 0f;
      } else if (volume > 1f) {
         return 1f;
      } else {
         return volume;
      }
   }

   public void stopSong() {
      curSong.stop();
   }

   public void stopAmbience() {
      curAmbience.stop();
   }

   public boolean isSongPlaying(Integer index) {
      return index.equals(curSongIndex) && curSong.isPlaying();
   }

   public boolean isAmbiencePlaying(Integer ambienceIndex) {
      return ambienceIndex.equals(curAmbienceIndex) && curAmbience.isPlaying();
   }

   /** Loops the current song if it should loop, else it just starts it. */
   public void continueCurrentSong() {
      if (curSongLooping) {
         this.curSong.setLooping(true);
         this.curSong.play();
      } else {
         this.curSong.play();
      }
   }

   /** Continues looping the current ambience */
   public void continueCurrentAmbience() {
      this.curAmbience.setLooping(true);
      this.curAmbience.play();
   }

   public void flush() {
      songs.flush();
      ambienceTracks.flush();
      sfxPlayer.flush();
   }
}
