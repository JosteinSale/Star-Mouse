package audio;

import com.badlogic.gdx.audio.Sound;

import main_classes.Testing;
import utils.ResourceLoader;
import utils.Singleton;

/**
 * Handles all music and sound for the game.
 * Provides methods for starting, stopping, continuing, pausing and fading of
 * music and sounds, as well as setting volume
 */
public class AudioPlayer extends Singleton {
   private SFXPlayer sfxPlayer;
   private MusicPlayer musicPlayer;

   // Volume
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
      if (Testing.testingMode && !Testing.playMusic) {
         setSongVolume(0f);
      }
      if (Testing.testingMode && !Testing.playSFX) {
         setSfxVolume(0f);
      }
   }

   private void loadAudio() {
      this.sfxPlayer = new SFXPlayer(setSfxVolume);
      this.musicPlayer = new MusicPlayer(setSongVolume, setAmbienceVolume);
   }

   /**
    * Plays the SFX with the given ID, using the SFXPlayer-object (see javadoc).
    * Each sound effect can only play once per frame.
    */
   public void playSFX(String sfxId) {
      sfxPlayer.playSfx(sfxId);
   }

   /**
    * Plays the voice clip with the given character name, using the voicePlayer-object (see
    * javadoc).
    */
   public void playVoiceClip(String name) {
      this.sfxPlayer.playVoiceClip(name);
   }

   /**
    * Stops the current song loop, and then starts a new song loop with the
    * specified identifier.
    */
   public void startSong(String songId, float startPos, boolean shouldLoop) {
      stopFadeOutIfActive();
      musicPlayer.startSong(songId, startPos, shouldLoop);
   }

   /**
    * Stops the current ambience loop, and then starts a new
    * ambience loop with the specified identifier.
    */
   public void startAmbienceLoop(String ambienceId) {
      stopFadeOutIfActive();
      musicPlayer.startAmbienceLoop(ambienceId);
   }

   /**
    * Sometimes we start a new song/ambience while a fadeOut is happening.
    * In such case we need to stop the fadeout and reset it, so that it
    * doesn't stop the next potential song/ambience.
    */
   private void stopFadeOutIfActive() {
      if (fadeOutActive) {
         resetFadeValues();
      }
   }

   /** Stops all loops and resets them to the beginning */
   public void stopAllLoops() {
      musicPlayer.stopAllLoops();
   }

   /**
    * Pauses all loops. When play is invoked again, it will start from where the
    * song was paused
    */
   public void pauseAllLoops() {
      musicPlayer.pauseAllLoops();
   }

   /** Fades out the current song + ambience, and then stops them. */
   public void fadeOutAllLoops() {
      if (musicPlayer.isLoopPlaying()) {
         this.fadeOutActive = true;
      }
   }

   public void update() {
      sfxPlayer.update();
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
         musicPlayer.setFadeVolume(songFadeVolume, ambienceFadeVolume);
         // Check if both songVolume and ambienceVolume are 0. We need to check both,
         // in case the user has turned down the volume for one of them.
         if (songFadeVolume == 0 && ambienceFadeVolume == 0) {
            musicPlayer.stopAllLoops();
            resetFadeValues();
         }
      }
   }

   private void resetFadeValues() {
      songFadeVolume = setSongVolume;
      ambienceFadeVolume = setAmbienceVolume;
      fadeOutActive = false;
      volumeFadeTick = 0;
   }

   /** Returns the volume set by player (not currentVolume) */
   public float getMusicVolume() {
      return this.setSongVolume;
   }

   /** Returns the sfx / ambience volume set by player (not currentVolume) */
   public float getSfxVolume() {
      return sfxPlayer.getVolume();
   }

   /** Adjusts volume to safe colume, sets it, and also resets any fading. */
   public void setSongVolume(float volume) {
      float adjustedVolume = adjustToSafeVolume(volume);
      this.setSongVolume = adjustedVolume;
      musicPlayer.setSongVolume(adjustedVolume);
      resetFadeValues();
   }

   /**
    * Goes for both ambience and sfx.
    * Adjusts volume to safe colume, sets it, and also resets any fading.
    */
   public void setSfxVolume(float volume) {
      float adjustedVolume = adjustToSafeVolume(volume);
      this.setSfxVolume = adjustedVolume;
      this.sfxPlayer.setVolume(adjustedVolume);
      this.setAmbienceVolume = adjustedVolume;
      musicPlayer.setAmbienceVolume(adjustedVolume);
      resetFadeValues();
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
      musicPlayer.stopSong();
   }

   public void stopAmbience() {
      musicPlayer.stopAmbience();
   }

   public boolean isSongPlaying(String songId) {
      return musicPlayer.isSongPlaying(songId);
   }

   public boolean isAmbiencePlaying(String ambienceId) {
      return musicPlayer.isAmbiencePlaying(ambienceId);
   }

   /** Loops the current song if it should loop, else it just starts it. */
   public void continueCurrentSong() {
      musicPlayer.continueCurrentSong();
   }

   /** Continues looping the current ambience */
   public void continueCurrentAmbience() {
      musicPlayer.continueCurrentAmbience();
   }

   public void flush() {
      musicPlayer.flush();
      sfxPlayer.flush();
   }
}
