package audio;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.audio.Sound;

import utils.ResourceLoader;
import utils.Singleton;

/**
 * This class handles all SFX for the game.
 * At present it loads all SFX into memory upon start.
 */
public class SFXPlayer extends Singleton {
    private float curVolume;

    // Voiceclips are identified by name
    private HashMap<String, Sound> voiceClips;

    // SFX are identified by indexes (also hard-coded into the Constant class)
    private ArrayList<Sound> sfx;

    public SFXPlayer(float initialVolume) {
        this.curVolume = initialVolume;
        loadAllSounds();
    }

    // TODO - load sounds into memory on demand, kinda like with Images
    private void loadAllSounds() {
        // SFX:
        this.sfx = new ArrayList<>();
        String[] SFXfileNames = {
                "SFX - Lazer10.ogg",
                "SFX - BombShoot.ogg",
                "SFX - Teleport.ogg",
                "SFX - ShipCrash1.5.ogg",
                "SFX - SmallExplosion3.6.ogg",
                "SFX - BigExplosion2.ogg",
                "SFX - BombPickup.ogg",
                "SFX - Powerup2.ogg",
                "SFX - Powerup3.ogg",
                "SFX - Cursor1.ogg",
                "SFX - Select2.ogg",
                "SFX - MenuSound.ogg",
                "SFX - ItemPickup.ogg",
                "SFX - Success.ogg",
                "SFX - InfoBox2.ogg",
                "SFX - BigExplosion3.ogg",
                "SFX - Hurt2.ogg",
                "SFX - Death.ogg",
                "SFX - MetallicWarning.ogg",
                "SFX - Rudinger1Death.ogg",
                "SFX - CathedralShot.ogg"
        };
        for (int i = 0; i < SFXfileNames.length; i++) {
            sfx.add(ResourceLoader.getSound(SFXfileNames[i]));
        }

        // Voiceclips
        this.voiceClips = new HashMap<>();
        HashMap<String, String> characterToSoundMap = new HashMap<>();
        characterToSoundMap.put("Max", "VoiceClip - Max.ogg");
        characterToSoundMap.put("Oliver", "VoiceClip - Oliver.ogg");
        characterToSoundMap.put("Lance", "VoiceClip - Feno.ogg");
        characterToSoundMap.put("Charlotte", "VoiceClip - Charlotte.ogg");
        characterToSoundMap.put("Nina", "VoiceClip - Nina.ogg");
        characterToSoundMap.put("Shady pilot", "VoiceClip - ShadyPilot.ogg");
        characterToSoundMap.put("Speaker", "VoiceClip - Speaker.ogg");
        characterToSoundMap.put("Sign", "VoiceClip - Sign.ogg");
        characterToSoundMap.put("Lt.Red", "VoiceClip - Lt.Red.ogg");
        characterToSoundMap.put("Russel", "VoiceClip - Russel.ogg");
        characterToSoundMap.put("Emma", "VoiceClip - Emma.ogg");
        characterToSoundMap.put("Nathan", "VoiceClip - Nathan.ogg");
        characterToSoundMap.put("Frida", "VoiceClip - Frida.ogg");
        characterToSoundMap.put("Mechanic", "VoiceClip - ShadyPilot.ogg");
        characterToSoundMap.put("Skye", "VoiceClip - Skye.ogg");
        characterToSoundMap.put("Zack", "VoiceClip - Zack.ogg");
        characterToSoundMap.put("Gard", "VoiceClip - Gard.ogg");
        characterToSoundMap.put("Feno", "VoiceClip - Feno.ogg");
        characterToSoundMap.put("Rudinger", "VoiceClip - Rudinger2.ogg");
        characterToSoundMap.put("???", "VoiceClip - Rudinger2.ogg");
        characterToSoundMap.put("Raze", "VoiceClip - Raze.ogg");
        characterToSoundMap.put("????", "VoiceClip - Raze.ogg");
        characterToSoundMap.put("Drone", "VoiceClip - Drone.ogg");

        for (String characterName : characterToSoundMap.keySet()) {
            voiceClips.put(characterName,
                    ResourceLoader.getSound(characterToSoundMap.get(characterName)));
        }

    }

    public void playSfx(int index) {
        sfx.get(index).play(curVolume);
    }

    public void playVoiceClip(String name) {
        voiceClips.get(name).play(curVolume);
    }

    public void setVolume(float newVolume) {
        this.curVolume = newVolume;
    }

    public float getVolume() {
        return this.curVolume;
    }
}
