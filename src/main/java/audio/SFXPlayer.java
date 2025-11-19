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
                "SFX - Lazer10.wav",
                "SFX - BombShoot.wav",
                "SFX - Teleport.wav",
                "SFX - ShipCrash1.5.wav",
                "SFX - SmallExplosion3.6.wav",
                "SFX - BigExplosion2.wav",
                "SFX - BombPickup.wav",
                "SFX - Powerup2.wav",
                "SFX - Powerup3.wav",
                "SFX - Cursor1.wav",
                "SFX - Select2.wav",
                "SFX - MenuSound.wav",
                "SFX - ItemPickup.wav",
                "SFX - Success.wav",
                "SFX - InfoBox2.wav",
                "SFX - BigExplosion3.wav",
                "SFX - Hurt2.wav",
                "SFX - Death.wav",
                "SFX - MetallicWarning.wav",
                "SFX - Rudinger1Death.wav",
                "SFX - CathedralShot.wav"
        };
        for (int i = 0; i < SFXfileNames.length; i++) {
            sfx.add(ResourceLoader.getSound(SFXfileNames[i]));
        }

        // Voiceclips
        this.voiceClips = new HashMap<>();
        HashMap<String, String> characterToSoundMap = new HashMap<>();
        characterToSoundMap.put("Max", "VoiceClip - Max.wav");
        characterToSoundMap.put("Oliver", "VoiceClip - Oliver.wav");
        characterToSoundMap.put("Lance", "VoiceClip - Feno.wav");
        characterToSoundMap.put("Charlotte", "VoiceClip - Charlotte.wav");
        characterToSoundMap.put("Nina", "VoiceClip - Nina.wav");
        characterToSoundMap.put("Shady pilot", "VoiceClip - ShadyPilot.wav");
        characterToSoundMap.put("Speaker", "VoiceClip - Speaker.wav");
        characterToSoundMap.put("Sign", "VoiceClip - Sign.wav");
        characterToSoundMap.put("Lt.Red", "VoiceClip - Lt.Red.wav");
        characterToSoundMap.put("Russel", "VoiceClip - Russel.wav");
        characterToSoundMap.put("Emma", "VoiceClip - Emma.wav");
        characterToSoundMap.put("Nathan", "VoiceClip - Nathan.wav");
        characterToSoundMap.put("Frida", "VoiceClip - Frida.wav");
        characterToSoundMap.put("Mechanic", "VoiceClip - ShadyPilot.wav");
        characterToSoundMap.put("Skye", "VoiceClip - Skye.wav");
        characterToSoundMap.put("Zack", "VoiceClip - Zack.wav");
        characterToSoundMap.put("Gard", "VoiceClip - Gard.wav");
        characterToSoundMap.put("Feno", "VoiceClip - Feno.wav");
        characterToSoundMap.put("Rudinger", "VoiceClip - Rudinger2.wav");
        characterToSoundMap.put("???", "VoiceClip - Rudinger2.wav");
        characterToSoundMap.put("Raze", "VoiceClip - Raze.wav");
        characterToSoundMap.put("????", "VoiceClip - Raze.wav");
        characterToSoundMap.put("Drone", "VoiceClip - Drone.wav");

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
