package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioPlayer {
    private String[] SFXfileNames = {
        "SFX_Lazer1.wav", 
        "SFX_Lazer2.wav"
    };
    private String[] songFileNames = {
        "Spacesong.wav"
    }; 
    private File[] SFX;
    private Clip[] songs;
    private FloatControl gainControl;
    private int songIndex = 0;
    private float maxSongVolume = 0.9f;
    private float songVolume = maxSongVolume;   // Må være mellom 0 og 1
    private float volumeFadeSpeed = 0.05f;
    private boolean fadeOutActive = false;
    private int waitTick = 0;                 // Brukes for å fade i steg.
    private int tickPerFrame = 20;
    
    public AudioPlayer() {
        loadAudio();
        gainControl = (FloatControl) songs[songIndex].getControl(FloatControl.Type.MASTER_GAIN);
    }

    private void loadAudio() {
        this.songs = new Clip[this.songFileNames.length];
        for (int i = 0; i < this.songs.length; i++) {
            File file = new File(System.getProperty("user.dir") + 
            "/src/resources/audio/" + songFileNames[i]);
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                this.songs[i] = clip;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        this.SFX = new File[this.SFXfileNames.length];
        for (int i = 0; i < this.SFX.length; i++) {
            File sample = new File(System.getProperty("user.dir") + 
            "/src/resources/audio/" + SFXfileNames[i]);
            this.SFX[i] = sample;
        }
    }

    /** Lager et nytt Clip-objekt hver gang metoden kalles */
    public void playSFX(int index) {
        try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SFX[index]);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSongLoop(int index) {
        this.songVolume = maxSongVolume;   // set to currenSongtVolume later
        this.songIndex = index;
        updateSongVolume();
        this.songs[songIndex].loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Abruptly stops the current song, and resets it */
    public void stopSongLoop() {
        if (songs[songIndex].isActive()) {  
            songs[songIndex].stop();
            songs[songIndex].setMicrosecondPosition(0);
        }
    }

    /** Fades out the current song, and then stops- and resets it */
    public void fadeOutSongLoop() {
        if (songs[songIndex].isActive()) { 
            this.fadeOutActive = true;
        }
    }

    public void update() {
        if (this.fadeOutActive) {
            this.waitTick++;
            if (waitTick > tickPerFrame) {
                waitTick = 0;
                updateSongVolume();
                songVolume -= volumeFadeSpeed;
                if (songVolume < 0) {
                    songs[songIndex].stop();
                    songs[songIndex].setMicrosecondPosition(0);
                    this.fadeOutActive = false;
                    waitTick = 0;
                }
            }
        }
    }

    private void updateSongVolume() {
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * songVolume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }
}
