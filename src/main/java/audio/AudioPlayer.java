package audio;

import com.badlogic.gdx.audio.Music;

import utils.ResourceLoader;
import utils.Singleton;

public class AudioPlayer extends Singleton {
    private SFXPlayer sfxPlayer;
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
    private Music[] songs;
    private Music[] ambienceTracks;
    private Integer curSongIndex = 0; // Used to check if a given song is playing.
    private Integer curAmbienceIndex = 0; // Used to check if a given ambience is playing.
    private boolean curSongLooping; // Is needed whenever we restart a song.
    private Music curSong;
    private Music curAmbience;

    private float setSongVolume = 0.71f; // The player's selected volume
    private float setAmbienceVolume = 0.91f;
    private float curSongVolume = 0.71f; // Used for fading
    private float curSfxVolume = 0.91f; // Needed for voices
    private float curAmbienceVolume = 0.91f;

    private float volumeFadeSpeed = 0.05f;
    private boolean fadeOutActive = false;
    private int waitTick = 0; // Used for fade
    private int tickPerFrame = 20;

    /** Private constructor, to ensure that we never make a new one. */
    public AudioPlayer() {
        loadAudio();
    }

    private void loadAudio() {
        this.sfxPlayer = new SFXPlayer(curSfxVolume);

        // Songs
        this.songs = new Music[this.songFileNames.length];
        for (int i = 0; i < this.songs.length; i++) {
            Music song = ResourceLoader.getSong(songFileNames[i]);
            this.songs[i] = song;
        }
        // Ambience
        this.ambienceTracks = new Music[this.ambienceFileNames.length];
        for (int i = 0; i < this.ambienceTracks.length; i++) {
            Music ambience = ResourceLoader.getSong(ambienceFileNames[i]);
            this.ambienceTracks[i] = ambience;
        }
        // Initial assignments
        this.curSong = songs[0];
        this.curAmbience = ambienceTracks[0];
    }

    /**
     * Plays the SFX with the given index, using the SFXPlayer-object (see javadoc).
     * 
     * @param index
     */
    public void playSFX(int index) {
        this.sfxPlayer.playSfx(index);
    }

    /**
     * Plays the voice clip with the given index, using the voicePlayer-object (see
     * javadoc).
     * 
     * @param index
     */
    public void playVoiceClip(String name) {
        this.sfxPlayer.playVoiceClip(name);
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
        curSong = songs[index];
        curSong.setVolume(curSongVolume);
        if (curSongLooping) {
            curSong.setLooping(true);
            curSong.play();
        } else {
            curSong.play();
        }
        curSong.setPosition(startPos);
    }

    /**
     * Stops the current ambience loop, and then starts a new
     * ambience loop with the specified index.
     * Index = 99 means no ambience.
     */
    public void startAmbienceLoop(int index) {
        if (index == 99) {
            return;
        }
        this.curAmbienceIndex = index;
        this.curAmbienceVolume = setAmbienceVolume;
        stopFadeOutIfActive(); // In case fadeOut is happening
        curAmbience = ambienceTracks[index];
        curAmbience.setVolume(curAmbienceVolume);
        curAmbience.setLooping(true);
        curAmbience.play();
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

    /** Stops all loops and resets them to the beginning */
    public void stopAllLoops() {
        curSong.stop();
        curAmbience.stop();
    }

    /**
     * Pauses all loops. When play is invoked again, it will start from where the
     * song was paused
     */
    public void pauseAllLoops() {
        curSong.pause();
        curAmbience.pause();
    }

    /** Fades out the current song + ambience, and then stops them. */
    public void fadeOutAllLoops() {
        if (curSong.isPlaying() || curAmbience.isPlaying()) {
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
            updateSongVolumeForFading();
            updateAmbienceVolumeForFading();
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
    private void updateSongVolumeForFading() {
        curSong.setVolume(curSongVolume);
    }

    private void updateAmbienceVolumeForFading() {
        curAmbience.setVolume(curAmbienceVolume);
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
        float adjustedVolume = adjustToSafeVolume(volume);
        this.setSongVolume = adjustedVolume;
        this.curSongVolume = adjustedVolume;
        curSong.setVolume(adjustedVolume);
    }

    /** Is called from the OptionsMenu. Goes for both ambience and sfx */
    public void setSfxVolume(float volume) {
        float adjustedVolume = adjustToSafeVolume(volume);
        this.curSfxVolume = adjustedVolume;
        this.sfxPlayer.setVolume(adjustedVolume);

        this.setAmbienceVolume = adjustedVolume;
        this.curAmbienceVolume = adjustedVolume;
        ambienceTracks[curAmbienceIndex].setVolume(adjustedVolume);
    }

    private float adjustToSafeVolume(float volume) {
        if (volume < 0f) {
            return 0f;
        } else if (volume > 1f) {
            return 1f;
        } else {
            return volume;
        }
    }

    public void stopSong() {
        curSong.stop();
    }

    public void stopAmbience() {
        curAmbience.stop();
    }

    public boolean isSongPlaying(Integer index) {
        return index.equals(curSongIndex) && curSong.isPlaying();
    }

    public boolean isAmbiencePlaying(Integer ambienceIndex) {
        return ambienceIndex.equals(curAmbienceIndex) && curAmbience.isPlaying();
    }

    /** Loops the current song if it should loop, else it just starts it. */
    public void continueCurrentSong() {
        if (curSongLooping) {
            this.curSong.setLooping(true);
            this.curSong.play();
        } else {
            this.curSong.play();
        }
    }

    /** Continues looping the current ambience */
    public void continueCurrentAmbience() {
        this.curAmbience.setLooping(true);
        this.curAmbience.play();
    }
}
