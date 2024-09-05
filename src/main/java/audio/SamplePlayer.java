package audio;

import java.util.ArrayList;

import javax.sound.sampled.Clip;
import static audio.AudioUtils.setClipVolume;

/**
 * Class for samples that require rapid fire.
 * A single Clip-object takes some time before it can be reset
 * (depending on the length of the clip).
 * Therefore, to simulate the sample being played fast, we load several copies
 * of the same sample and then play them in succesion.
 * 
 * How many samples are needed depends on the expected rate of fire.
 * E.g. the lazerShoot might need 7, while the bombShoot might only need 2.
 */
public class SamplePlayer {
    protected String sampleName;
    private ArrayList<Clip> samples;
    private int currentIndex = 0;
    protected int nrOfSamples;

    public SamplePlayer(String sampleName, int nrOfSamples) {
        this.sampleName = sampleName;
        this.nrOfSamples = nrOfSamples;
    }

    public void setSamples(ArrayList<Clip> samples) {
        this.samples = samples;
    }

    protected void playSample() {
        Clip sample = samples.get(currentIndex);
        sample.setMicrosecondPosition(0);
        sample.start();
        currentIndex = (currentIndex + 1) % nrOfSamples;
    }

    public void setVolume(float volume) {
        for (Clip sample : samples) {
            setClipVolume(sample, volume);
        }
    }
}
