package audio;

import static audio.AudioUtils.getNewClip;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;

import utils.Singleton;

/**
 * This class handles all voice clips for the game.
 * All voice clips require rapid fire (i.e can be played in fast succession).
 * 
 * They are inialized in SamplePlayer-objects the first time the play-method
 * for that character is called (see javadoc for SamplePlayer).
 */
public class VoicePlayer extends Singleton {
    private final int sampleCopies = 3;
    private HashMap<String, SamplePlayer> fastSamples;
    private static HashMap<String, String> nameToClipMap; // Maps the character's name to the clip
    private float curVolume;

    static {
        nameToClipMap = new HashMap<>();
        nameToClipMap.put("Max", "VoiceClip - Max.wav");
        nameToClipMap.put("Oliver", "VoiceClip - Oliver.wav");
        nameToClipMap.put("Lance", "VoiceClip - Feno.wav");
        nameToClipMap.put("Charlotte", "VoiceClip - Charlotte.wav");
        nameToClipMap.put("Nina", "VoiceClip - Nina.wav");
        nameToClipMap.put("Shady pilot", "VoiceClip - ShadyPilot.wav");
        nameToClipMap.put("Speaker", "VoiceClip - Speaker.wav");
        nameToClipMap.put("Sign", "VoiceClip - Sign.wav");
        nameToClipMap.put("Lt.Red", "VoiceClip - Lt.Red.wav");
        nameToClipMap.put("Russel", "VoiceClip - Russel.wav");
        nameToClipMap.put("Emma", "VoiceClip - Emma.wav");
        nameToClipMap.put("Nathan", "VoiceClip - Nathan.wav");
        nameToClipMap.put("Frida", "VoiceClip - Frida.wav");
        nameToClipMap.put("Mechanic", "VoiceClip - ShadyPilot.wav");
        nameToClipMap.put("Skye", "VoiceClip - Skye.wav");
        nameToClipMap.put("Zack", "VoiceClip - Zack.wav");
        nameToClipMap.put("Gard", "VoiceClip - Gard.wav");
        nameToClipMap.put("Feno", "VoiceClip - Feno.wav");
        nameToClipMap.put("Rudinger", "VoiceClip - Rudinger2.wav");
        nameToClipMap.put("???", "VoiceClip - Rudinger2.wav");
        nameToClipMap.put("Raze", "VoiceClip - Raze.wav");
        nameToClipMap.put("????", "VoiceClip - Raze.wav");
        nameToClipMap.put("Drone", "VoiceClip - Drone.wav");
    }

    public VoicePlayer(float initialVolume) {
        this.curVolume = initialVolume;
        this.fastSamples = new HashMap<>();
    }

    private void loadSamplesForCharacter(String name, float initialVolume) {
        if (!nameToClipMap.containsKey(name)) {
            throw new IllegalArgumentException("No clip available for: " + name);
        }
        String clipName = nameToClipMap.get(name);
        SamplePlayer samplePlayer = new SamplePlayer(clipName, sampleCopies);
        ArrayList<Clip> clipList = new ArrayList<>();

        for (int i = 0; i < sampleCopies; i++) {
            clipList.add(getNewClip(clipName, initialVolume));
        }
        samplePlayer.setSamples(clipList);
        fastSamples.put(name, samplePlayer);
    }

    /**
     * Checks if the clip is loaded into memory. If not, it loads a new SamplePlayer
     * object with 3 voice clips into memory first. Then it plays the clip.
     */
    public void play(String name) {
        if (!fastSamples.containsKey(name)) {
            this.loadSamplesForCharacter(name, curVolume);
        }
        fastSamples.get(name).playSample();
    }

    public void setGlobalVolume(float newVolume) {
        this.curVolume = newVolume;
        for (SamplePlayer sp : fastSamples.values()) {
            sp.setVolume(newVolume);
        }
    }

    public float getVolume() {
        return this.curVolume;
    }

}
