package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Current suboptimal implementation of the following:
 *  -Volume adjustment (is done every time a new clip is played)
 *  -Potential memory leak if silent track isn't stopped upon playing a new ambience.
 */
public class AudioPlayer {
    private String[] SFXfileNames = {    // Only the soundfiles included in the game.
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
        "SFX - Death.wav"
    };
    private String[] songFileNames = {
        "Song - Tutorial (FINISHED)3.wav",
        "Song - The Academy ver2 (FINISHED).wav",
        "Song - Skies Over Apolis.wav",
        "Song - Main Menu.wav",
        "Song - Vyke.wav"
    };
    private String[] ambienceFileNames = {
        "Ambience - Silence.wav",
        "Ambience - RocketEngineQuiet.wav",
        "Ambience - Wind.wav",
        "Ambience - Alarm.wav",
        "Ambience - Hangar.wav"
    };
    private String[] voiceClipNames = {
        "VoiceClip - Max.wav",
        "VoiceClip - Oliver.wav",
        "VoiceClip - Lance.wav",
        "VoiceClip - Charlotte.wav",
        "VoiceClip - Nina.wav",
        "VoiceClip - ShadyPilot.wav",
        "VoiceClip - Speaker.wav",
        "VoiceClip - Sign.wav",
        "VoiceClip - Lt.Red.wav",
        "VoiceClip - Russel.wav",
        "VoiceClip - Emma.wav",
        "VoiceClip - Nathan.wav",
        "VoiceClip - Frida.wav",
        "VoiceClip - Skye.wav",
        "VoiceClip - Zack.wav",
        "VoiceClip - Gard.wav",
        "VoiceClip - Feno.wav"
    };
    private File[] SFX;
    private File[] voiceClips;
    private File[] songs;
    private File[] ambienceTracks;
    private Clip curSong;
    private Clip curAmbience;
    private FloatControl songGainControl;      // Is initially set in the MainMenu
    private FloatControl sfxGainControl;       // Is initially set in the MainMenu
    private FloatControl ambienceGainControl;  // Is initially set in the MainMenu

    private float setSongVolume = 0.85f;   // The player's selected volume
    private float setSfxVolume = 0.91f;    
    private float setAmbienceVolume = 0.91f;
    private float curSongVolume = 0.85f;   // Used for fading
    private float curSfxVolume = 0.91f;
    private float curAmbienceVolume = 0.91f;

    private float volumeFadeSpeed = 0.05f;
    private boolean fadeOutActive = false;
    private int waitTick = 0;                 // Used for fade
    private int tickPerFrame = 20;
    
    public AudioPlayer() {
        loadAudio();
    }

    private void loadAudio() {
        // Songs
        this.songs = new File[this.songFileNames.length];
        for (int i = 0; i < this.songs.length; i++) {
            File file = new File(System.getProperty("user.dir") + 
            "/src/resources/audio/" + songFileNames[i]);
            this.songs[i] = file;
        }
        // Ambience
        this.ambienceTracks = new File[this.ambienceFileNames.length];
        for (int i = 0; i < this.ambienceTracks.length; i++) {
            File file = new File(System.getProperty("user.dir") + 
            "/src/resources/audio/" + ambienceFileNames[i]);
            this.ambienceTracks[i] = file;
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
     * 
     * Foreløpig justeres volum hver eneste gang et nytt klipp avspilles.
     * @param index
     */
    public void playSFX(int index) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SFX[index]);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            sfxGainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            updateSfxVolume();
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Lager et nytt Clip-objekt hver gang metoden kalles.
     * Av en eller annen grunn: hvis det ikke er musikk i bakgrunnen, OG frekvensen
     * på SFX-avspillingen er lav, kommer det ikke noe lyd fra klippet.
     * 
     * Foreløpig justeres volum hver eneste gang et nytt klipp avspilles.
     * @param index
     */
    public void playVoiceClip(int index) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(voiceClips[index]);
            Clip clip = AudioSystem.getClip();	
            clip.open(audioInputStream);
            sfxGainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            updateSfxVolume();
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
        this.curSongVolume = setSongVolume;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(songs[index]);
            curSong = AudioSystem.getClip();
            curSong.open(audioInputStream);
            songGainControl = (FloatControl) curSong.getControl(FloatControl.Type.MASTER_GAIN);
            updateSongVolume();
            curSong.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Stops the current ambience loop, and then starts a new
     *  ambience loop with the specified index.
     *  Index = 0 means a silence track.
     *  Index = 99 means no ambience.
     */
    public void startAmbienceLoop(int index) {
        if (index == 99) {return;}
        this.curAmbienceVolume = setAmbienceVolume;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ambienceTracks[index]);
            curAmbience = AudioSystem.getClip();
            curAmbience.open(audioInputStream);
            ambienceGainControl = (FloatControl) curAmbience.getControl(FloatControl.Type.MASTER_GAIN);
            updateAmbienceVolume();
            curAmbience.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Abruptly stops the current song + ambience */
    public void stopAllLoops() {
        if (curSong.isActive()) {  
            curSong.stop();
        }
        if (curAmbience.isActive()) {  
            curAmbience.stop();
        }
    }

    /** Fades out the current song + ambience, and then stops them. */
    public void fadeOutAllLoops() {
        if ((curSong.isActive()) || (curAmbience.isActive())) { 
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
            curSongVolume -= volumeFadeSpeed;
            curAmbienceVolume -= volumeFadeSpeed;
            if (curSongVolume < 0) {
                curSong.stop();
                curAmbience.stop();
                curSongVolume = setSongVolume;
                curAmbienceVolume = setAmbienceVolume;
                this.fadeOutActive = false;
                waitTick = 0;
            }
        }
    }

    /** Adjusts songGainControl according to curSongVolume */
    private void updateSongVolume() {
        float range = songGainControl.getMaximum() - songGainControl.getMinimum();
        float gain = (range * curSongVolume) + songGainControl.getMinimum();
        songGainControl.setValue(gain);
    }

    /** Adjusts sfxGainControl according to curSfxVolume */
    private void updateSfxVolume() {
        float range = sfxGainControl.getMaximum() -  sfxGainControl.getMinimum();
        float gain = (range * curSfxVolume) + sfxGainControl.getMinimum();
        sfxGainControl.setValue(gain);
    }

    /** Adjusts ambienceGainControl according to curAmbienceVolume */
    private void updateAmbienceVolume() {
        float range = ambienceGainControl.getMaximum() - ambienceGainControl.getMinimum();
        float gain = (range * curAmbienceVolume) + ambienceGainControl.getMinimum();
        ambienceGainControl.setValue(gain);
    }

    /** Returns the volume set by player (not currentVolume) */
    public float getMusicVolume() {
        return this.setSongVolume;
    }

    /** Returns the sfx / ambience volume set by player (not currentVolume) */
    public float getSfxVolume() {
        return this.setSfxVolume;
    }

    public void setSongVolume(float volume) {
        this.setSongVolume = volume;
        this.curSongVolume = volume;
        updateSongVolume();
    }

    /** Goes for both ambience and sfx */
    public void setSfxVolume(float volume) {
        this.setSfxVolume = volume;
        this.curSfxVolume = volume;
        updateSfxVolume();
        
        this.setAmbienceVolume = volume;
        this.curAmbienceVolume = volume;
        updateAmbienceVolume();
    }
}
