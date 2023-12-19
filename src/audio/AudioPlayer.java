package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import main.Game;

// Only the soundfiles included in the game.
public class AudioPlayer {
    private Game game;
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
        "SFX - BigExplosion3.wav"
    };
    private String[] songFileNames = {
        "Song - Tutorial (FINISHED)3.wav",
        "Song - The Academy (FINISHED).wav"
    };
    private String[] ambienceFileNames = {
        "Ambience - Silence.wav",
        "Ambience - RocketEngineQuiet.wav",
        "Ambience - Wind.wav",
        "Ambience - Alarm.wav"
    };
    private String[] voiceClipNames = {
        "VoiceClip - Max.wav",
        "VoiceClip - Oliver.wav",
        "VoiceClip - Lance.wav",
        "VoiceClip - Charlotte.wav",
        "VoiceClip - Nina.wav",
        "VoiceClip - ShadyPilot.wav",
        "VoiceClip - Speaker.wav"
    };
    private File[] SFX;
    private File[] voiceClips;
    private Clip[] songs;
    private Clip[] ambienceTracks;
    private FloatControl songGainControl;
    private FloatControl ambienceGainControl;
    private int songIndex = 0;
    private int ambienceIndex = 0;
    private float maxSongVolume = 0.9f;
    private float songVolume = maxSongVolume;   // Må være mellom 0 og 1. For både ambience og song
    private float volumeFadeSpeed = 0.05f;
    private boolean fadeOutActive = false;
    private int waitTick = 0;                 // Brukes for å fade i steg.
    private int tickPerFrame = 20;
    
    public AudioPlayer(Game game) {
        this.game = game;
        loadAudio();
    }

    private void loadAudio() {
        // Songs
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
        // Ambience
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
        // SFX
        this.SFX = new File[this.SFXfileNames.length];
        for (int i = 0; i < this.SFX.length; i++) {
            File sample = new File(System.getProperty("user.dir") + 
            "/src/resources/audio/" + SFXfileNames[i]);
            this.SFX[i] = sample;
        }
        // VoiceClips
        this.voiceClips = new File[this.voiceClipNames.length];
        for (int i = 0; i < this.voiceClips.length; i++) {
            File sample = new File(System.getProperty("user.dir") + 
            "/src/resources/audio/" + voiceClipNames[i]);
            this.voiceClips[i] = sample;
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

    /** Lager et nytt Clip-objekt hver gang metoden kalles.
     * Av en eller annen grunn: hvis det ikke er musikk i bakgrunnen, OG frekvensen
     * på SFX-avspillingen er lav, kommer det ikke noe lyd fra klippet.
     * @param index
     */
    public void playVoiceClip(int index) {
        try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(voiceClips[index]);
        Clip clip = AudioSystem.getClip();	
        clip.open(audioInputStream);
        clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Starts a song loop with the specified index.
     *  Index = 99 means no song.
     */
    public void startSongLoop(int index) {
        if (index == 99) {return;}         
        this.songVolume = maxSongVolume;   // set to currenSongVolume later
        this.songIndex = index;
        songGainControl = (FloatControl) songs[songIndex].getControl(FloatControl.Type.MASTER_GAIN);
        updateSongVolume();
        this.songs[songIndex].loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Stops the current ambience loop, and then starts a new
     *  ambience loop with the specified index.
     *  Index = 0 means a silence track.
     *  Index = 99 means no ambience.
     */
    public void startAmbienceLoop(int index) {
        if (index == 99) {return;}
        this.songVolume = maxSongVolume;   // set to currenSongtVolume later
        ambienceTracks[ambienceIndex].stop();
        ambienceTracks[ambienceIndex].setMicrosecondPosition(0);
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
