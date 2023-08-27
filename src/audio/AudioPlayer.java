package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.Game;

public class AudioPlayer {
    private Game game;
    private String[] SFXfileNames = {
        "SFX - Lazer1.wav",
        "SFX - Lazer2.wav",
        "SFX - Lazer3.wav",
        "SFX - Lazer4.wav",
        "SFX - Lazer4.5.wav",
        "SFX - Lazer5.wav",
        "SFX - Lazer6.wav",
        "SFX - Lazer6.5.wav",
        "SFX - BombShoot.wav",
        "SFX - Teleport.wav",
        "SFX - ShipCrash1.wav",
        "SFX - ShipCrash1.5.wav",
        "SFX - ShipCrash2.wav",
        "SFX - ShipCrash3.wav",
        "SFX - ShipCrash3.5.wav",
        "SFX - SmallExplosion1.wav",
        "SFX - SmallExplosion2.wav",
        "SFX - SmallExplosion3.wav",
        "SFX - SmallExplosion3.5.wav",
        "SFX - BigExplosion1.wav",
        "SFX - BigExplosion2.wav",
        "SFX - BigExplosion3.wav",
        "SFX - BombPickup.wav",
        "SFX - Powerup1.wav",
        "SFX - Powerup2.wav",
        "SFX - Powerup3.wav",
        "SFX - Cursor1.wav",
        "SFX - Cursor2.wav",
        "SFX - Cursor3.wav",
        "SFX - Cursor4.wav",
        "SFX - Select1.wav",
        "SFX - Select2.wav",
        "SFX - Select3.wav",
        "SFX - Select4.wav",
        "SFX - Select5.wav",
        "SFX - Select6.wav",
        "SFX - MenuSound.wav",
        "SFX - ItemPickup.wav",
        "SFX - TradeCompleted.wav",
        "SFX - Success.wav",
    };
    private String[] songFileNames = {
        "Song - Tutorial (FINISHED)3.wav",
    };
    private String[] ambienceFileNames = {
        "Ambience - RocketEngineQuiet.wav",
    };
    private File[] SFX;
    private Clip[] songs;
    private Clip[] ambienceTracks;
    private FloatControl songGainControl;
    private FloatControl ambienceGainControl;
    private int songIndex = 0;
    private int ambienceIndex = 0;
    private float maxSongVolume = 0.9f;
    private float songVolume = maxSongVolume;   // Må være mellom 0 og 1
    private float volumeFadeSpeed = 0.05f;
    private boolean fadeOutActive = false;
    private int waitTick = 0;                 // Brukes for å fade i steg.
    private int tickPerFrame = 20;
    
    public AudioPlayer(Game game) {
        this.game = game;
        loadAudio();
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

        this.ambienceTracks = new Clip[this.ambienceFileNames.length];
        for (int i = 0; i < this.ambienceTracks.length; i++) {
            File file = new File(System.getProperty("user.dir") + 
            "/src/resources/audio/" + ambienceFileNames[i]);
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                this.ambienceTracks[i] = clip;
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

    /** Lager et nytt Clip-objekt hver gang metoden kalles.
     * Av en eller annen grunn: hvis det ikke er musikk i bakgrunnen, OG frekvensen
     * på SFX-avspillingen er lav, kommer det ikke noe lyd fra klippet.
     * @param index
     */
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
        songGainControl = (FloatControl) songs[songIndex].getControl(FloatControl.Type.MASTER_GAIN);
        updateSongVolume();
        this.songs[songIndex].loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void startAmbienceLoop(int index) {
        this.songVolume = maxSongVolume;   // set to currenSongtVolume later
        this.ambienceIndex = index;
        ambienceGainControl = (FloatControl) ambienceTracks[ambienceIndex].getControl(FloatControl.Type.MASTER_GAIN);
        updateAmbienceVolume();
        this.ambienceTracks[ambienceIndex].loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Abruptly stops the current song, and resets it */
    public void stopAllLoops() {
        if (songs[songIndex].isActive()) {  
            songs[songIndex].stop();
            songs[songIndex].setMicrosecondPosition(0);
        }
        if (ambienceTracks[ambienceIndex].isActive()) {  
            ambienceTracks[ambienceIndex].stop();
            ambienceTracks[ambienceIndex].setMicrosecondPosition(0);
        }
    }

    /** Fades out the current song, and then stops- and resets it */
    public void fadeOutAllLoops() {
        if ((songs[songIndex].isActive()) || (ambienceTracks[ambienceIndex].isActive())) { 
            this.fadeOutActive = true;
        }
    }

    public void update() {
        if (this.fadeOutActive) {updateFade();}
    }

    private void updateFade() {
        this.waitTick++;
        if (waitTick > tickPerFrame) {
            waitTick = 0;
            updateSongVolume();
            updateAmbienceVolume();
            songVolume -= volumeFadeSpeed;
            if (songVolume < 0) {
                songs[songIndex].stop();
                songs[songIndex].setMicrosecondPosition(0);
                ambienceTracks[ambienceIndex].stop();
                ambienceTracks[ambienceIndex].setMicrosecondPosition(0);
                this.fadeOutActive = false;
                waitTick = 0;
            }
        }
    }

    private void updateSongVolume() {
        float range = songGainControl.getMaximum() - songGainControl.getMinimum();
        float gain = (range * songVolume) + songGainControl.getMinimum();
        songGainControl.setValue(gain);
    }

    private void updateAmbienceVolume() {
        float range = ambienceGainControl.getMaximum() -  ambienceGainControl.getMinimum();
        float gain = (range * songVolume) + ambienceGainControl.getMinimum();
        ambienceGainControl.setValue(gain);
    }
}
