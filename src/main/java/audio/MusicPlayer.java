package audio;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.audio.Music;

import utils.Constants.Audio;
import utils.ResourceContainer;
import utils.ResourceLoader;
import utils.parsing.AudioParser;

/**
 * This class handles basic functionality for MyMusics (= songs and ambience).
 */
public class MusicPlayer {
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

   private ResourceContainer<MyMusic> songs;
   private ResourceContainer<MyMusic> ambienceTracks;
   private String curSongId;
   private String curAmbienceId;
   private boolean curSongLooping;
   private Music curSong;
   private Music curAmbience;

   private float curSongVolume;
   private float curAmbienceVolume;

   public MusicPlayer(float initialSongVolume, float initialAmbienceVolume) {
      this.songs = new ResourceContainer<>(s -> ResourceLoader.getSong(s));
      this.ambienceTracks = new ResourceContainer<>(s -> ResourceLoader.getSong(s));
      this.curSongVolume = initialSongVolume;
      this.curAmbienceVolume = initialAmbienceVolume;

      // Initial assignments: needed to avoid nullpointers
      curSongId = SONG_MAP.get(Audio.SONG_MAIN_MENU);
      curAmbienceId = AMBIENCE_MAP.get(Audio.AMBIENCE_ROCKET_ENGINE);
      this.curSong = songs.getResource(curSongId, false).get();
      this.curAmbience = ambienceTracks.getResource(curAmbienceId, true).get();
   }

   public void startSong(String songId, float startPos, boolean shouldLoop) {
      if (songId.equals(Audio.NONE)) {
         return;
      }
      if (curSong.isPlaying()) {
         curSong.stop();
      }
      this.curSongId = songId;
      this.curSongLooping = shouldLoop;
      String songFileName = AudioParser.ParseSongId(songId);
      curSong = songs.getResource(songFileName, false).get();
      curSong.setVolume(curSongVolume);
      if (shouldLoop) {
         curSong.setLooping(true);
         curSong.play();
      } else {
         curSong.play();
      }
      curSong.setPosition(startPos); // Needs to be called after .play()
   }

   public void startAmbienceLoop(String ambienceId) {
      if (ambienceId.equals(Audio.NONE)) {
         return;
      }
      if (curAmbience.isPlaying()) {
         curAmbience.stop();
      }
      this.curAmbienceId = ambienceId;
      String ambienceFileName = AudioParser.ParseAmbienceId(ambienceId);
      curAmbience = ambienceTracks.getResource(ambienceFileName, false).get();
      curAmbience.setVolume(curAmbienceVolume);
      curAmbience.setLooping(true);
      curAmbience.play();
   }

   public void stopAllLoops() {
      curSong.stop();
      curAmbience.stop();
   }

   public void pauseAllLoops() {
      curSong.pause();
      curAmbience.pause();
   }

   /** Sets volume only for the current song and ambience */
   public void setFadeVolume(float songVolume, float ambienceVolume) {
      curSong.setVolume(songVolume);
      curAmbience.setVolume(ambienceVolume);
   }

   public boolean isLoopPlaying() {
      return curSong.isPlaying() || curAmbience.isPlaying();
   }

   public void setSongVolume(float volume) {
      curSongVolume = volume;
      curSong.setVolume(volume);
   }

   public void setAmbienceVolume(float volume) {
      curAmbienceVolume = volume;
      curAmbience.setVolume(volume);
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

   public void continueCurrentSong() {
      if (curSongLooping) {
         this.curSong.setLooping(true);
         this.curSong.play();
      } else {
         this.curSong.play();
      }
   }

   public void continueCurrentAmbience() {
      this.curAmbience.setLooping(true);
      this.curAmbience.play();
   }

   public void flush() {
      songs.flush();
      ambienceTracks.flush();
   }
}
