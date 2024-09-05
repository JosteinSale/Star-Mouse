package audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import utils.Constants.Audio;

public class AudioPlayer {
    private SFXPlayer sfxPlayer;
    private VoicePlayer voicePlayer;
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
            "Song - The Dark.wav",
            "Song - The Dark (Ending).wav",
            "Song - Cathedral.wav"
    };
    private String[] ambienceFileNames = {
            "Ambience - Silence.wav",
            "Ambience - RocketEngineQuiet.wav",
            "Ambience - Wind.wav",
            "Ambience - Alarm.wav",
            "Ambience - Hangar.wav",
            "Ambience - Cave.wav"
    };
    private File[] songs;
    private File[] ambienceTracks;
    private Integer curSongIndex; // Used to check if a given song is playing.
    private Integer curAmbienceIndex; // Used to check if a given ambience is playing.
    private boolean curSongLooping; // Is needed whenever we restart a song.
    private Clip curSong;
    private Clip curAmbience;
    private Clip silentTrack;
    private FloatControl songGainControl; // Is initially set in the StartScreen
    private FloatControl sfxGainControl; // Is initially set in the StartScreen
    private FloatControl ambienceGainControl; // Is initially set in the StartScreen

    private float setSongVolume = 0.85f; // The player's selected volume
    private float setAmbienceVolume = 0.91f;
    private float curSongVolume = 0.85f; // Used for fading
    private float curSfxVolume = 0.91f; // Needed for voices
    private float curAmbienceVolume = 0.91f;

    private float volumeFadeSpeed = 0.05f;
    private boolean fadeOutActive = false;
    private int waitTick = 0; // Used for fade
    private int tickPerFrame = 20;

    private static boolean singletonCreated = false; // Flag to determine singleton

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

    /**
     * The Clip and GainControl for ambience needs to be set.
     * This method handles that.
     */
    private void initAmbience() {
        this.startAmbienceLoop(Audio.AMBIENCE_WIND);
        this.curAmbience.stop();
    }

    private void loadAudio() {
        this.sfxPlayer = new SFXPlayer(curSfxVolume);
        this.voicePlayer = new VoicePlayer(curSfxVolume);

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
        
    }

    /**
     * Starts a silent track that loops continuously. Should be called at
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

    /**
     * Plays the SFX with the given index, using the SFXPlayer-object (see javadoc).
     * 
     * @param index
     */
    public void playSFX(int index) {
        this.sfxPlayer.play(index);
    }

    /**
     * Plays the voice clip with the given index, using the voicePlayer-object (see javadoc).
     * 
     * @param index
     */
    public void playVoiceClip(String name) {
        this.voicePlayer.play(name);
    }

    /**
     * Starts a song loop with the specified index.
     * Index = 99 means no song.
     */
    public void startSong(int index, float startPos, boolean shouldLoop) {
        if (index == 99) {
            return;
        }
        this.curSongIndex = index;
        this.curSongVolume = setSongVolume;
        this.curSongLooping = shouldLoop;
        stopFadeOutIfActive(); // In case fadeOut is happening
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(songs[index]);
            curSong = AudioSystem.getClip();
            curSong.open(audioInputStream);
            songGainControl = (FloatControl) curSong.getControl(FloatControl.Type.MASTER_GAIN);
            updateSongVolume();
            curSong.setMicrosecondPosition((int) startPos * 1000000);
            if (curSongLooping) {
                curSong.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                curSong.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the current ambience loop, and then starts a new
     * ambience loop with the specified index.
     * Index = 99 means no ambience.
     */
    public void startAmbienceLoop(int index) {
        if (index == 99) {
            return;
        } else if (index == Audio.AMBIENCE_SILENCE) {
            throw new IllegalArgumentException("Don't play silent track!");
        }
        this.curAmbienceIndex = index;
        this.curAmbienceVolume = setAmbienceVolume;
        this.stopFadeOutIfActive(); // In case fadeOut is happening
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

    /**
     * Sometimes we start a new song/ambience while a fadeOut is happening.
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

    public void update() {
        if (this.fadeOutActive) {
            updateFade();
        }
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
        float range = sfxGainControl.getMaximum() - sfxGainControl.getMinimum();
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
        return sfxPlayer.getVolume();
    }

    /** Is called from the OptionsMenu */
    public void setSongVolume(float volume) {
        this.setSongVolume = volume;
        this.curSongVolume = volume;
        updateSongVolume();
    }

    /** Is called from the OptionsMenu. Goes for both ambience and sfx */
    public void setSfxVolume(float volume) {
        this.curSfxVolume = volume;
        this.sfxPlayer.setGlobalVolume(volume);
        this.voicePlayer.setGlobalVolume(volume);

        this.setAmbienceVolume = volume;
        this.curAmbienceVolume = volume;
        updateAmbienceVolume();
    }

    public void stopSong() {
        if (curSong.isActive()) {
            curSong.stop();
        }
    }

    public void stopAmbience() {
        if (curAmbience.isActive()) {
            curAmbience.stop();
        }
    }

    public boolean isSongPlaying(Integer index) {
        return index.equals(curSongIndex) && curSong.isActive();
    }

    public boolean isAmbiencePlaying(Integer ambienceIndex) {
        return ambienceIndex.equals(curAmbienceIndex) && curAmbience.isActive();
    }

    /** Loops the current song if it should loop, else it just starts it. */
    public void continueCurrentSong() {
        if (curSongLooping) {
            this.curSong.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            this.curSong.start();
        }
    }

    /** Continues looping the current ambience */
    public void continueCurrentAmbience() {
        this.curAmbience.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
