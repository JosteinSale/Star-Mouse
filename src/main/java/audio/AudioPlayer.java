package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import utils.Constants.Audio;

/**
 * Current suboptimal implementation of the following:
 *  -Volume adjustment (is done every time a new clip is played)
 *  -Potential memory leak if silent track isn't stopped upon playing a new ambience.
 */
public class AudioPlayer {
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
    private String[] songFileNames = {
        "Song - Tutorial (FINISHED)3.wav",
        "Song - The Academy ver3.wav",
        "Song - Skies Over Apolis.wav",
        "Song - Main Menu.wav",
        "Song - Vyke.wav",
        "Song - Vyke Ambush.wav",
        "Song - Grand Reaper.wav",
        "Song - Rudinger Theme.wav",
        "Song - Asteroid Escape.wav",
        "Song - Apo Explodes.wav",
        "Song - Back to Apo.wav",
    };
    private String[] ambienceFileNames = {
        "Ambience - Silence.wav",
        "Ambience - RocketEngineQuiet.wav",
        "Ambience - Wind.wav",
        "Ambience - Alarm.wav",
        "Ambience - Hangar.wav",
        "Ambience - Cave.wav"
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
        "VoiceClip - Feno.wav",
        "VoiceClip - Rudinger2.wav",
        "VoiceClip - Raze.wav",
        "VoiceClip - Drone.wav"
    };
    private File[] SFX;
    private File[] voiceClips;
    private File[] songs;
    private File[] ambienceTracks;
    private Integer curSongIndex;           // Used to check if a given song is playing.
    private Clip curSong;
    private Clip curAmbience;
    private Clip silentTrack;
    private FloatControl songGainControl;      // Is initially set in the StartScreen
    private FloatControl sfxGainControl;       // Is initially set in the StartScreen
    private FloatControl ambienceGainControl;  // Is initially set in the StartScreen

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

    private static boolean singletonCreated = false;  // Flag to determine singleton
    
    /** Private constructor, to ensure that we never make a new one. */
    private AudioPlayer() {
        if (singletonCreated) {
            throw new IllegalArgumentException("Singleton created. Don't create new AudioPlayer!");
        }
        AudioPlayer.singletonCreated = true;
        loadAudio();
        initAmbience();
        this.startSilentTrack();
    }

    public static AudioPlayer getSingletonAudioPlayer() {
        return new AudioPlayer();  
    }

    /** The Clip and GainControl for ambience needs to be set. 
     * This method handles that. */
    private void initAmbience() {
        this.startAmbienceLoop(Audio.AMBIENCE_WIND);
        this.curAmbience.stop();
    }

    private void loadAudio() {
        // Songs
        this.songs = new File[this.songFileNames.length];
        for (int i = 0; i < this.songs.length; i++) {
            File file = new File(System.getProperty("user.dir") + 
            "/src/main/resources/audio/" + songFileNames[i]);
            this.songs[i] = file;
        }
        // Ambience
        this.ambienceTracks = new File[this.ambienceFileNames.length];
        for (int i = 0; i < this.ambienceTracks.length; i++) {
            File file = new File(System.getProperty("user.dir") + 
            "/src/main/resources/audio/" + ambienceFileNames[i]);
            this.ambienceTracks[i] = file;
        }
        // SFX
        this.SFX = new File[this.SFXfileNames.length];
        for (int i = 0; i < this.SFX.length; i++) {
            File sample = new File(System.getProperty("user.dir") + 
            "/src/main/resources/audio/" + SFXfileNames[i]);
            this.SFX[i] = sample;
        }
        // VoiceClips
        this.voiceClips = new File[this.voiceClipNames.length];
        for (int i = 0; i < this.voiceClips.length; i++) {
            File sample = new File(System.getProperty("user.dir") + 
            "/src/main/resources/audio/" + voiceClipNames[i]);
            this.voiceClips[i] = sample;
        }
    }

    /** Starts a silent track that loops continuously. Should be called at 
     * game startup, and should run in the background until the game is exited.
     * This is needed because sfx-clips do not start unless there is another clip
     * running in the background, for some reason.
     */
    private void startSilentTrack() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ambienceTracks[Audio.AMBIENCE_SILENCE]);
            silentTrack = AudioSystem.getClip();
            silentTrack.open(audioInputStream);
            silentTrack.loop(Clip.LOOP_CONTINUOUSLY);
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
    public void startSongLoop(int index, float startPos) {
        if (index == 99) {return;}
        this.curSongIndex = index;
        this.curSongVolume = setSongVolume;
        stopFadeOutIfActive();   // In case fadeOut is happening 
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(songs[index]);
            curSong = AudioSystem.getClip();
            curSong.open(audioInputStream);
            songGainControl = (FloatControl) curSong.getControl(FloatControl.Type.MASTER_GAIN);
            updateSongVolume();
            curSong.setMicrosecondPosition((int) startPos *1000000);
            curSong.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** Stops the current ambience loop, and then starts a new
     *  ambience loop with the specified index.
     *  Index = 99 means no ambience.
     */
    public void startAmbienceLoop(int index) {
        if (index == 99) {return;}
        else if (index == Audio.AMBIENCE_SILENCE) {
            throw new IllegalArgumentException("Don't play silent track!");
        }
        this.curAmbienceVolume = setAmbienceVolume;
        this.stopFadeOutIfActive();  // In case fadeOut is happening 
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

    /** Sometimes we start a new song/ambience while a fadeOut is happening.
     * In such case we need to stop the fadeout and reset it, so that it
     * doesn't stop the new song/ambience.
     */
    private void stopFadeOutIfActive() {
        if (fadeOutActive) {
            fadeOutActive = false;
            waitTick = 0;
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

    /** Should be called whenever you want to stop/start both music and ambience.
     * (We might phase out this method later, since it only works properly for Flying.
     * Consider using the strategy used in bossMode instead.)
     */
    public void flipAudioOnOff() {
        if (this.curSong.isActive() || this.curAmbience.isActive()) {
            this.curSong.stop();
            this.curAmbience.stop();
        } else {
            this.curSong.loop(Clip.LOOP_CONTINUOUSLY);
            this.curAmbience.loop(Clip.LOOP_CONTINUOUSLY);
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

    /** Is called from the OptionsMenu */
    public void setSongVolume(float volume) {
        this.setSongVolume = volume;
        this.curSongVolume = volume;
        updateSongVolume();
    }

    /** Is called from the OptionsMenu. Goes for both ambience and sfx */
    public void setSfxVolume(float volume) {
        this.setSfxVolume = volume;
        this.curSfxVolume = volume;
        updateSfxVolume();
        
        this.setAmbienceVolume = volume;
        this.curAmbienceVolume = volume;
        updateAmbienceVolume();
    }

    public void stopAmbience() {
        if (curAmbience.isActive()) {
            curAmbience.stop();
        }
    }

    public boolean isSongPlaying(Integer index) {
        return index.equals(curSongIndex) && curSong.isActive();
    }

    public void continueCurrentSong() {
        this.curSong.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void continueCurrentAmbience() {
      this.curAmbience.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
