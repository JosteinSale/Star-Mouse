package audio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import main_classes.Testing;
import utils.Constants.Audio;
import utils.ResourceContainer;
import utils.ResourceLoader;
import utils.Singleton;
import utils.parsing.AudioParser;

public class AudioPlayer extends Singleton {
   public static final Map<String, String> SONG_MAP = new HashMap<>();
   public static final Map<String, String> AMBIENCE_MAP = new HashMap<>();

   static {
      // Misc
      SONG_MAP.put(Audio.SONG_MAIN_MENU, "Song - Main Menu.ogg");
      SONG_MAP.put(Audio.SONG_RUDINGER_THEME, "Song - Rudinger Theme.ogg");
      SONG_MAP.put(Audio.SONG_APO_EXPLODES, "Song - Apo Explodes.ogg");
      SONG_MAP.put(Audio.SONG_BURNING_PLANET, "Song - Burning Planet.ogg");

      // Flying
      SONG_MAP.put(Audio.SONG_FLY_LEVEL0, "Song - Tutorial (FINISHED)3.ogg");
      SONG_MAP.put(Audio.SONG_FLY_LEVEL1, "Song - Skies Over Apolis.ogg");
      SONG_MAP.put(Audio.SONG_FLY_LEVEL2, "Song - Vyke Ambush.ogg");
      SONG_MAP.put(Audio.SONG_FLY_LEVEL3, "Song - Asteroid Escape.ogg");
      SONG_MAP.put(Audio.SONG_FLY_LEVEL4, "Song - The Tunnel.ogg");
      SONG_MAP.put(Audio.SONG_FLY_LEVEL5, "Song - Holy Halls.ogg");

      // Exploring
      SONG_MAP.put(Audio.SONG_EXPLORING_VYKE, "Song - Vyke.ogg");
      SONG_MAP.put(Audio.SONG_EXPLORING_ACADEMY, "Song - The Academy ver3.ogg");
      SONG_MAP.put(Audio.SONG_EXPLORING_CATHEDRAL, "Song - Cathedral.ogg");

      // Bosses
      SONG_MAP.put(Audio.SONG_BOSS1, "Song - Grand Reaper.ogg");

      // Ambience
      AMBIENCE_MAP.put(Audio.AMBIENCE_ROCKET_ENGINE, "Ambience - RocketEngineQuiet.ogg");
      AMBIENCE_MAP.put(Audio.AMBIENCE_WIND, "Ambience - Wind.ogg");
      AMBIENCE_MAP.put(Audio.AMBIENCE_HANGAR, "Ambience - Hangar.ogg");
      AMBIENCE_MAP.put(Audio.AMBIENCE_CAVE, "Ambience - Cave.ogg");
   }

   private SFXPlayer sfxPlayer;
   private ResourceContainer<MyMusic> songs;
   private ResourceContainer<MyMusic> ambienceTracks;
   private String curSongId;
   private String curAmbienceId;
   private boolean curSongLooping;
   private Music curSong;
   private Music curAmbience;
   private Set<Integer> sfxPlayedThisFrame;

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
      sfxPlayedThisFrame = new HashSet<>();
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
      this.songs = new ResourceContainer<>(s -> ResourceLoader.getSong(s));
      this.ambienceTracks = new ResourceContainer<>(s -> ResourceLoader.getSong(s));

      // Initial assignments: needed to avoid nullpointers
      String mainMenuSong = SONG_MAP.get(Audio.SONG_MAIN_MENU);
      String rocketEngineAmbience = AMBIENCE_MAP.get(Audio.AMBIENCE_ROCKET_ENGINE);
      this.curSong = songs.getResource(mainMenuSong, false).get();
      this.curAmbience = ambienceTracks.getResource(rocketEngineAmbience, true).get();
   }

   /**
    * Plays the SFX with the given index, using the SFXPlayer-object (see javadoc).
    * Each sound effect can only play once per frame.
    * 
    * @param index
    */
   public void playSFX(int index) {
      if (!sfxPlayedThisFrame.contains(index)) {
         this.sfxPlayer.playSfx(index);
         sfxPlayedThisFrame.add(index);
      }
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
    * specified identifier.
    */
   public void startSong(String songId, float startPos, boolean shouldLoop) {
      if (songId.equals(Audio.NONE)) {
         return;
      }
      if (curSong.isPlaying()) {
         curSong.stop();
      }
      this.curSongId = songId;
      this.songFadeVolume = setSongVolume;
      this.curSongLooping = shouldLoop;
      stopFadeOutIfActive();
      String songFileName = AudioParser.ParseSongId(songId);
      curSong = songs.getResource(songFileName, false).get();
      curSong.setVolume(songFadeVolume);
      if (shouldLoop) {
         curSong.setLooping(true);
         curSong.play();
      } else {
         curSong.play();
      }
      curSong.setPosition(startPos); // Needs to be called after .play()
   }

   /**
    * Stops the current ambience loop, and then starts a new
    * ambience loop with the specified identifier.
    */
   public void startAmbienceLoop(String ambienceId) {
      if (ambienceId.equals(Audio.NONE)) {
         return;
      }
      if (curAmbience.isPlaying()) {
         curAmbience.stop();
      }
      this.curAmbienceId = ambienceId;
      this.ambienceFadeVolume = setAmbienceVolume;
      stopFadeOutIfActive();
      String ambienceFileName = AudioParser.ParseAmbienceId(ambienceId);
      curAmbience = ambienceTracks.getResource(ambienceFileName, false).get();
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
      sfxPlayedThisFrame.clear();
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

   public boolean isSongPlaying(String songId) {
      return songId.equals(curSongId) && curSong.isPlaying();
   }

   public boolean isAmbiencePlaying(String ambienceId) {
      return ambienceId.equals(curAmbienceId) && curAmbience.isPlaying();
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
