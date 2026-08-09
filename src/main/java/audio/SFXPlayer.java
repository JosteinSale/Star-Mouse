package audio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import utils.ResourceContainer;
import utils.ResourceLoader;
import utils.Singleton;

import static utils.Constants.Audio;

/**
 * This class handles basic functionality for MySounds (= SFX).
 */
public class SFXPlayer extends Singleton {
   private float curVolume;
   private Set<String> sfxPlayedThisFrame;

   private ResourceContainer<MySound> voiceClips;
   private ResourceContainer<MySound> sfx;

   public static final HashMap<String, String> CHARACTER_SOUND_MAP = new HashMap<>();
   static {
      CHARACTER_SOUND_MAP.put("Max", "VoiceClip - Max.ogg");
      CHARACTER_SOUND_MAP.put("Oliver", "VoiceClip - Oliver.ogg");
      CHARACTER_SOUND_MAP.put("Lance", "VoiceClip - Feno.ogg");
      CHARACTER_SOUND_MAP.put("Charlotte", "VoiceClip - Charlotte.ogg");
      CHARACTER_SOUND_MAP.put("Nina", "VoiceClip - Nina.ogg");
      CHARACTER_SOUND_MAP.put("Shady pilot", "VoiceClip - ShadyPilot.ogg");
      CHARACTER_SOUND_MAP.put("Speaker", "VoiceClip - Speaker.ogg");
      CHARACTER_SOUND_MAP.put("Sign", "VoiceClip - Sign.ogg");
      CHARACTER_SOUND_MAP.put("Lt.Red", "VoiceClip - Lt.Red.ogg");
      CHARACTER_SOUND_MAP.put("Russel", "VoiceClip - Russel.ogg");
      CHARACTER_SOUND_MAP.put("Emma", "VoiceClip - Emma.ogg");
      CHARACTER_SOUND_MAP.put("Nathan", "VoiceClip - Nathan.ogg");
      CHARACTER_SOUND_MAP.put("Frida", "VoiceClip - Frida.ogg");
      CHARACTER_SOUND_MAP.put("Mechanic", "VoiceClip - ShadyPilot.ogg");
      CHARACTER_SOUND_MAP.put("Skye", "VoiceClip - Skye.ogg");
      CHARACTER_SOUND_MAP.put("Zack", "VoiceClip - Zack.ogg");
      CHARACTER_SOUND_MAP.put("Gard", "VoiceClip - Gard.ogg");
      CHARACTER_SOUND_MAP.put("Feno", "VoiceClip - Feno.ogg");
      CHARACTER_SOUND_MAP.put("Rudinger", "VoiceClip - Rudinger2.ogg");
      CHARACTER_SOUND_MAP.put("???", "VoiceClip - Rudinger2.ogg");
      CHARACTER_SOUND_MAP.put("Raze", "VoiceClip - Raze.ogg");
      CHARACTER_SOUND_MAP.put("????", "VoiceClip - Raze.ogg");
      CHARACTER_SOUND_MAP.put("Drone", "VoiceClip - Drone.ogg");
   }
   public static final Map<String, String> SFX_MAP = new HashMap<>();
   static {
      SFX_MAP.put(Audio.SFX_LAZER, "SFX - Lazer10.ogg");
      SFX_MAP.put(Audio.SFX_BOMBSHOOT, "SFX - BombShoot.ogg");
      SFX_MAP.put(Audio.SFX_TELEPORT, "SFX - Teleport.ogg");
      SFX_MAP.put(Audio.SFX_COLLISION, "SFX - ShipCrash1.5.ogg");
      SFX_MAP.put(Audio.SFX_SMALL_EXPLOSION, "SFX - SmallExplosion3.6.ogg");
      SFX_MAP.put(Audio.SFX_BIG_EXPLOSION, "SFX - BigExplosion2.ogg");
      SFX_MAP.put(Audio.SFX_BOMB_PICKUP, "SFX - BombPickup.ogg");
      SFX_MAP.put(Audio.SFX_REPAIR, "SFX - Powerup2.ogg");
      SFX_MAP.put(Audio.SFX_POWERUP, "SFX - Powerup3.ogg");
      SFX_MAP.put(Audio.SFX_CURSOR, "SFX - Cursor1.ogg");
      SFX_MAP.put(Audio.SFX_CURSOR_SELECT, "SFX - MenuSound.ogg");
      SFX_MAP.put(Audio.SFX_STARTGAME, "SFX - Select2.ogg");
      SFX_MAP.put(Audio.SFX_INVENTORY_PICKUP, "SFX - ItemPickup.ogg");
      SFX_MAP.put(Audio.SFX_SUCCESS, "SFX - Success.ogg");
      SFX_MAP.put(Audio.SFX_INFOBOX, "SFX - InfoBox2.ogg");
      SFX_MAP.put(Audio.SFX_HURT, "SFX - Hurt2.ogg");
      SFX_MAP.put(Audio.SFX_DEATH, "SFX - Death.ogg");
      SFX_MAP.put(Audio.SFX_METALLIC_SOUND, "SFX - MetallicWarning.ogg");
      SFX_MAP.put(Audio.SFX_RUDINGER1_DEATH, "SFX - Rudinger1Death.ogg");
      SFX_MAP.put(Audio.CATHEDRAL_SHOT, "SFX - CathedralShot.ogg");
      SFX_MAP.put(Audio.MISSILE_STRIKE, "SFX - BigExplosion3.ogg");
   };

   public SFXPlayer(float initialVolume) {
      this.curVolume = initialVolume;
      voiceClips = new ResourceContainer<>(s -> (MySound) ResourceLoader.getSound(s));
      sfx = new ResourceContainer<>(s -> (MySound) ResourceLoader.getSound(s));
      sfxPlayedThisFrame = new HashSet<>();
   }

   public void playSfx(String sfxId) {
      sfxPlayedThisFrame.add(sfxId);
      String fileName = getSFXFileName(sfxId);
      MySound sound = sfx.getResource(fileName, false);
      sound.get().play(curVolume);
   }

   private String getSFXFileName(String sfxId) {
      if (!SFX_MAP.containsKey(sfxId)) {
         throw new IllegalArgumentException("No SFX loaded for sfxId: " + sfxId);
      }
      return SFX_MAP.get(sfxId);
   }

   public void playVoiceClip(String name) {
      if (!CHARACTER_SOUND_MAP.containsKey(name)) {
         throw new IllegalArgumentException("No voice clip loaded for character with name: " + name);
      }
      MySound sound = voiceClips.getResource(CHARACTER_SOUND_MAP.get(name), false);
      sound.get().play(curVolume);
   }

   public void setVolume(float newVolume) {
      this.curVolume = newVolume;
   }

   public float getVolume() {
      return this.curVolume;
   }

   public void flush() {
      sfx.flush();
      voiceClips.flush();
   }

   public void update() {
      sfxPlayedThisFrame.clear();
   }
}
