package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioUtils {

    public static void setClipVolume(Clip clip, float volume) {
        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    public static Clip getNewClip(String sampleName, float volume) {
        File file = new File(System.getProperty("user.dir") +
                "/src/main/resources/audio/" + sampleName);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip sample = AudioSystem.getClip();
            sample.open(audioInputStream);
            setClipVolume(sample, volume);
            return sample;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Couldn't load clip: " + sampleName);
    }

}
