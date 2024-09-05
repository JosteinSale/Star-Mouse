package audio;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.Clip;
import static audio.AudioUtils.setClipVolume;
import static audio.AudioUtils.getNewClip;

/**
 * This class handles all SFX for the game.
 * 
 * It divides SFX into two categories:
 * - Samples that require rapid fire (i.e can be played in fast succession).
 * - Samples that don't require rapid fire.
 * 
 * Samples that require rapid fire will be inialized in SamplePlayer-objects
 * upon construction (see javadoc for SamplePlayer).
 * 
 * All other samples will be initialized upon need, and is kept
 * in memory until the game is shut down.
 */
public class SFXPlayer {
    // Only the soundfiles included in the game.
    // OBS: Don't change the indexes. These are coded into the Constants-class
    private String[] SFXfileNames = {
            "SFX - Lazer10.wav",
            "SFX - BombShoot.wav",
            "SFX - Teleport.wav",
            "SFX - ShipCrash1.5.wav",
            "SFX - SmallExplosion3.5.wav",
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
    private HashMap<Integer, SamplePlayer> fastSamples;
    private HashMap<Integer, Clip> slowSamples;
    private HashMap<Integer, String> indexToNameMap; // Is needed when we get new samples
    private float curVolume;

    public SFXPlayer(float initialVolume) {
        this.curVolume = initialVolume;
        loadIndexToNameMap();
        loadFastSamples(initialVolume);
        slowSamples = new HashMap<>();
    }

    private void loadIndexToNameMap() {
        this.indexToNameMap = new HashMap<>();
        for (int i = 0; i < SFXfileNames.length; i++) {
            indexToNameMap.put(i, SFXfileNames[i]);
        }
    }

    private void loadFastSamples(float initialVolume) {
        fastSamples = new HashMap<>();

        fastSamples.put(0, new SamplePlayer("SFX - Lazer10.wav", 7));
        fastSamples.put(1, new SamplePlayer("SFX - BombShoot.wav", 2));
        fastSamples.put(2, new SamplePlayer("SFX - Teleport.wav", 7));
        fastSamples.put(3, new SamplePlayer("SFX - ShipCrash1.5.wav", 3));
        fastSamples.put(4, new SamplePlayer("SFX - SmallExplosion3.5.wav", 4));
        fastSamples.put(9, new SamplePlayer("SFX - Cursor1.wav", 5));

        for (SamplePlayer sp : fastSamples.values()) {
            ArrayList<Clip> clipList = new ArrayList<>();
            for (int i = 0; i < sp.nrOfSamples; i++) {
                clipList.add(getNewClip(sp.sampleName, initialVolume));
            }
            sp.setSamples(clipList);
        }
    }

    public void play(int index) {
        // 1. Checks fast samples
        if (fastSamples.containsKey(index)) {
            fastSamples.get(index).playSample();
        }
        // 2. Checks slow samples
        else {
            checkIfSlowSampleAdded(index);
            playSlowSample(index);
        }
    }

    private void checkIfSlowSampleAdded(int index) {
        if (!slowSamples.containsKey(index)) {
            Clip newClip = getNewClip(indexToNameMap.get(index), curVolume);
            slowSamples.put(index, newClip);
        }
    }

    private void playSlowSample(int index) {
        Clip sample = this.slowSamples.get(index);
        sample.setMicrosecondPosition(0);
        sample.start();
    }

    public void setGlobalVolume(float newVolume) {
        this.curVolume = newVolume;
        for (int i = 0; i < SFXfileNames.length; i++) {
            this.adjustClipVolume(i, newVolume);
        }
    }

    public float getVolume() {
        return this.curVolume;
    }

    private void adjustClipVolume(int index, float volume) {
        // 1. Checks fast samples
        if (fastSamples.containsKey(index)) {
            fastSamples.get(index).setVolume(volume);
        }
        // 2. Checks slow samples
        else {
            if (slowSamples.containsKey(index)) {
                setClipVolume(slowSamples.get(index), volume);
            }
        }
    }

}
